package mul.cam.e.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.controller.NotificationController;
import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.StockHoldingsDto;
import mul.cam.e.gpt.GPTController;
import mul.cam.e.util.StockSymbolProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final NotificationController notificationController;
    private final ObjectMapper objectMapper;
    private final UserDao userDao;
    private final MyAssetDao myAssetDao;
    private final MyAssetService myAssetService;
    private final GPTController gptController;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public RabbitService(RabbitTemplate rabbitTemplate, NotificationController notificationController,
                         ObjectMapper objectMapper, UserDao userDao, MyAssetDao myAssetDao, MyAssetService myAssetService, GPTController gptController) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationController = notificationController;
        this.objectMapper = objectMapper;
        this.userDao = userDao;
        this.myAssetDao = myAssetDao;
        this.myAssetService = myAssetService;
        this.gptController = gptController;
    }

    // 포트폴리오 생성 메서드
    public void createPortfolio(String userName) throws InterruptedException {
        log.info("Creating portfolio for user: {}", userName);

        try {
            // 포트폴리오 데이터 생성
            Map<String, Object> portfolioData = generatePortfolioData(userName);

            // 직렬화
            String jsonMessage = objectMapper.writeValueAsString(portfolioData);

            // 유저별 큐와 바인딩 동적 생성
            rabbitTemplate.execute(channel -> {
                channel.queueDeclare("portfolio_" + userName, true, false, false, null);
                return null;
            });

            // Exchange와 유저 큐 바인딩 동적 설정
            createUserBinding(userName);

            // 메시지 전송
            rabbitTemplate.convertAndSend("userPortfolioExchange", "portfolio_" + userName, jsonMessage);

            notificationController.notifyUser("Portfolio creation completed for user: " + userName);
            fetchMessageFromQueue("portfolio_" + userName, userName);
        } catch (JsonProcessingException e) {
            log.error("Error serializing portfolio data for user {}", userName, e);
        } catch (IOException e) {
            log.error("Error during RabbitMQ operation for user {}", userName, e);
        }
    }

    // 포트폴리오 데이터 생성
    private Map<String, Object> generatePortfolioData(String userName) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("assetTotal", userAssetTotal(userName));
        data.put("detailAsset", userDetailAsset(userName));
        data.put("stockRevenue", userStockRevenue(userName));
        data.put("idTrend", idTrend(userName));
        // TODO gpt 사용할 때는 풀고 사용하시면 됩니다.
        // data.put("advice", advice(userName));
        return data;
    }

    // 큐에서 메시지 가져오기
    public void fetchMessageFromQueue(String userQueueName, String userName) {
        try {
            GetResponse response = rabbitTemplate.execute(channel -> channel.basicGet(userQueueName, false));

            if (response != null) {
                String messageBody = new String(response.getBody(), StandardCharsets.UTF_8);
                // 역직렬화
                Map<String, Object> messagePayload = objectMapper.readValue(messageBody, Map.class);
                log.info("Fetched message from queue {}: {}", userQueueName, messagePayload);

                long deliveryTag = response.getEnvelope().getDeliveryTag();
                rabbitTemplate.execute(channel -> {
                    channel.basicAck(deliveryTag, false);
                    return null;
                });

                notificationController.sendPortfolioUpdate(userName, messagePayload);
            } else {
                log.info("No message available in queue {}", userQueueName);
            }
        } catch (Exception e) {
            log.error("Error fetching message from queue {}", userQueueName, e);
        }
    }

    // 자산 총액 계산
    public Map<String, Object> userAssetTotal(String userName) {
        log.info("Executing userAssetTotal for user: {}", userName);
        Map<String, Object> assetTotal = new HashMap<>();
        assetTotal.put("totalAccount", myAssetDao.totalAccount(userName));
        assetTotal.put("totalStock", myAssetDao.totalStock(userName));
        assetTotal.put("totalRealestate", myAssetDao.totalRealestate(userName));
        assetTotal.put("totalCar", myAssetDao.totalCar(userName));
        return Collections.singletonMap("assetTotal", assetTotal);
    }

    // 자산 세부 항목
    public Map<String, Object> userDetailAsset(String userName) {
        log.info("Executing userDetailAsset for user: {}", userName);
        Map<String, Object> detailAsset = new HashMap<>();
        detailAsset.put("userAccount", myAssetService.userAccounts(userName));
        detailAsset.put("userStock", myAssetDao.userStockSymbol(userName));
        detailAsset.put("userCar", myAssetDao.userCar(userName));
        detailAsset.put("userRealestate", myAssetDao.userRealestate(userName));
        return Collections.singletonMap("detailAsset", detailAsset);
    }

    // 주식 자산 수익률 계산
    public Map<String, Object> userStockRevenue(String userName) throws IOException {
        log.info("Executing userStockRevenue for user: {}", userName);
        Map<String, Object> stockRevenue = new HashMap<>();
        int userId = userDao.getUserId(userName);
        List<StockHoldingsDto> stockList = myAssetDao.getStock(userId);

        for (StockHoldingsDto stock : stockList) {
            String stockSymbol = stock.getStockSymbol();
            String stockName = stock.getStockName();
            int purchasePrice = stock.getPurchasePrice();
            int quantity = stock.getQuantity();
            Date purchaseDate = stock.getPurchaseDate();

            String recentPriceStr = StockSymbolProcessor.recentStock(stockSymbol);
            int recentPrice = Integer.parseInt(recentPriceStr.replace(",", ""));
            double revenue = ((double) recentPrice / purchasePrice - 1) * 100;

            Map<String, Object> stockData = new HashMap<>();
            stockData.put("stockSymbol", stockSymbol);
            stockData.put("stockName", stockName);
            stockData.put("purchasePrice", purchasePrice);
            stockData.put("quantity", quantity);
            stockData.put("purchaseDate", purchaseDate);
            stockData.put("revenue", revenue + "%");

            stockRevenue.put(stockName, stockData);
        }

        return Collections.singletonMap("stockRevenue", stockRevenue);
    }

    // 자산 증감 추이 계산
    public Map<String, BigInteger> idTrend(String userName) throws IOException {
        List<Map<String, Object>> transaction = myAssetService.transactionTen(userName);
        Map<String, Map<String, List<String>>> symbol = myAssetService.userStockSymbol(userName);
        Map<String, BigInteger> answer = StockSymbolProcessor.calculateAssetTrend(transaction, symbol);
        return answer;
    }

    // 동적 큐 생성
    private void createUserBinding(String userName) {
        rabbitTemplate.execute(channel -> {
            channel.queueBind("portfolio_" + userName, "userPortfolioExchange", "portfolio_" + userName);
            return null;
        });
    }

    public Map<String, Object> advice(String userName) throws IOException {
        Map<String, Object> advice = new HashMap<>();

        String financePlan = gptController.chat(userAssetTotal(userName) + " 재무설계사처럼 재무계획을 300자 이내로 작성해줘");
        String investPlan = gptController.chat(idTrend(userName) + " 투자전문가처럼 조언을 300자 이내로 자세하게 작성해줘 ");
        String stockPlan = gptController.chat(userStockRevenue(userName) + " 주식전문가처럼 조언을 300자 이내로 자세하게 작성해줘");
        String mzTrend = gptController.chat("MZ 세대의 자산 관리 트렌드에 대해 300자 이내로 자세하게 작성해줘.");
        String financeTrend = gptController.chat("현재 금융 시장의 트렌드에 대해 300자 이내로 자세하게 작성해줘");
        String investAdvice = gptController.chat("투자 시장의 트렌드에 맞게 투자 상품을 300자 이내로 자세하게 추천해줘");

        advice.put("financePlan", financePlan);
        advice.put("investPlan", investPlan);
        advice.put("stockPlan", stockPlan);
        advice.put("mzTrend", mzTrend);
        advice.put("financeTrend", financeTrend);
        advice.put("investAdvice", investAdvice);

        return advice;
    }
}
