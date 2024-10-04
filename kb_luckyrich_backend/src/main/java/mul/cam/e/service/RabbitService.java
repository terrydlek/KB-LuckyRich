package mul.cam.e.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.controller.NotificationController;
import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dao.UserDao;
import mul.cam.e.dto.StockHoldingsDto;
import mul.cam.e.util.StockSymbolProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    public RabbitService(RabbitTemplate rabbitTemplate, Queue queue, NotificationController notificationController,
                         ObjectMapper objectMapper, UserDao userDao, MyAssetDao myAssetDao) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationController = notificationController;
        this.objectMapper = objectMapper;
        this.userDao = userDao;
        this.myAssetDao = myAssetDao;
    }

    // 포트폴리오 생성 메서드
    public void createPortfolio(String userName) throws InterruptedException {
        log.info("Creating portfolio for user: {}", userName);

        try {
            Map<String, Object> portfolioData = generatePortfolioData(userName);
            // 직렬화 한번 거쳐야 함
            String jsonMessage = objectMapper.writeValueAsString(portfolioData);
            String userQueueName = "portfolio_" + userName;

            rabbitTemplate.execute(channel -> {
                channel.queueDeclare(userQueueName, true, false, false, null);
                return null;
            });
                // 1. a 엄청 많은 작업을 요청
                // 2. 바로 직후에 b 사용자가 시간이 엄청 적게 걸리는 작업 요청

            rabbitTemplate.convertAndSend(userQueueName, jsonMessage);
            notificationController.notifyUser("Portfolio creation completed for user: " + userName);
            fetchMessageFromQueue(userQueueName);
        } catch (JsonProcessingException e) {
            log.error("Error serializing portfolio data for user {}", userName, e);
        } catch (IOException e) {
            log.error("Error during RabbitMQ operation for user {}", userName, e);
        }
    }

    private Map<String, Object> generatePortfolioData(String userName) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("assetTotal", userAssetTotal(userName));
        data.put("detailAsset", userDetailAsset(userName));
        data.put("stockRevenue", userStockRevenue(userName));
        return data;
    }

    @RabbitListener(queues = "q.test")
    public void receiveMessage(String message) {
        log.info("Received Message: {}", message);
    }

    public void fetchMessageFromQueue(String userQueueName) {
        try {
            GetResponse response = rabbitTemplate.execute(channel -> channel.basicGet(userQueueName, false));

            if (response != null) {
                String messageBody = new String(response.getBody(), StandardCharsets.UTF_8);
                // 받을 때는 역직렬화 한번 거쳐야 함
                Map<String, Object> messagePayload = objectMapper.readValue(messageBody, Map.class);
                log.info("Fetched message from queue {}: {}", userQueueName, messagePayload);

                long deliveryTag = response.getEnvelope().getDeliveryTag();
                rabbitTemplate.execute(channel -> {
                    channel.basicAck(deliveryTag, false);
                    return null;
                });
            } else {
                log.info("No message available in queue {}", userQueueName);
            }
        } catch (Exception e) {
            log.error("Error fetching message from queue {}", userQueueName, e);
        }
    }

    // 자산 총액
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

        detailAsset.put("userAccount", myAssetDao.userAccounts(userName));
        detailAsset.put("userStock", myAssetDao.userStockSymbol(userName));
        detailAsset.put("userCar", myAssetDao.userCar(userName));
        detailAsset.put("userRealestate", myAssetDao.userRealestate(userName));

        return Collections.singletonMap("detailAsset", detailAsset);
    }

    // 주식 자산 수익률
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
}
