//package mul.cam.e.rabbit;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/vi")
//public class MessageJsonController {
//
//    private RabbitMQJsonProducer rabbitMQJsonProducer;
//
//    public MessageJsonController(RabbitMQJsonProducer rabbitMQJsonProducer) {
//        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
//    }
//
//    @PostMapping("/publish")
//    public ResponseEntity<String> sendJsonMessage(UserDto userDto) {
//        rabbitMQJsonProducer.sendJsonMessage(userDto);
//        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
//    }
//}











//package mul.cam.e.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.GetResponse;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import mul.cam.e.controller.NotificationController;
//import mul.cam.e.dao.MyAssetDao;
//import mul.cam.e.dao.UserDao;
//import mul.cam.e.dto.StockHoldingsDto;
//import mul.cam.e.security.SecurityUser;
//import mul.cam.e.util.StockSymbolProcessor;
//import org.apache.xerces.impl.dv.dtd.IDREFDatatypeValidator;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Slf4j
//@Service
//public class RabbitService {
//    private final RabbitTemplate rabbitTemplate;
//    private final Queue queue;
//    private final NotificationController notificationController;
//    private final ObjectMapper objectMapper;
//    private final UserDao userDao;
//    private final MyAssetDao myAssetDao;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    public RabbitService(RabbitTemplate rabbitTemplate, Queue queue, NotificationController notificationController, ObjectMapper objectMapper, UserDao userDao, MyAssetDao myAssetDao) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.queue = queue;
//        this.notificationController = notificationController;
//        this.objectMapper = objectMapper;
//        this.userDao = userDao;
//        this.myAssetDao = myAssetDao;
//    }
//
////    public void sendMessage(String message) {
////        log.info("sending message: {}", message);
////        log.info(queue.getName());
//////        rabbitTemplate.convertAndSend(queue.getName(), message);
////        rabbitTemplate.convertAndSend("q.test", message);
////    }
//
//    // 여기에서 포트폴리오 생성 로직 처리하면 됨
//    public void createPortfolio(String userName) {
//        try {
////            Thread.sleep(5000);
//            log.info("Portfolio created for user: {}", userName);
//            Map<String, Object> data = new HashMap<>();
//
//            data.put("assetTotal", userAssetTotal(userName));
//            data.put("detailAsset", userDetailAsset(userName));
//            data.put("stockRevenue", userStockRevenue(userName));
//
//            // 직렬화 한번 거쳐서 rabbit에 넣어줘야 함
//            String json = objectMapper.writeValueAsString(data);
//
//            String userQueueName = "portfolio_" + userName;
//
//
//            // userQueueName으로 routerkey값이 없으면 rabbit에 새로 생성
//            rabbitTemplate.execute(channel -> {
//                channel.queueDeclare(userQueueName, true, false, false, null);
//                return null;
//            });
//
////            rabbitTemplate.convertAndSend(userQueueName, json);
//            // 라우팅 키를 userName으로 설정해버리면..?
//            rabbitTemplate.convertAndSend(exchange, userName, json);
//
//            // 사용자에게 알람 전송
//            notificationController.notifyUser("Portfolio creation completed for user: " + userName);
//
////            fetchMessageFromQueue("portfolio_" + SecurityContextHolder.getContext().getAuthentication().getName());
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    @RabbitListener(queues = "q.test")
//    public void receiveMessage(String message) {
//        log.info("Received Message : {}", message);
//    }
//
////    @RabbitListener(queues = "user_portfolio")
////    public void receiveUserPortfolio(String message) {
////        try {
////            // JSON 문자열을 Map으로 역직렬화
////            Map<String, Object> messagePayload = objectMapper.readValue(message, Map.class);
////            String userQueueName = (String) messagePayload.get("queueName");
////            Map<String, Integer> data = (Map<String, Integer>) messagePayload.get("data");
////
////            // Log the received message
////            log.info("Received User Portfolio from queue {}: {}", userQueueName, data);
////
////            // 필요한 추가 처리 수행
////            // 예를 들어, 포트폴리오 데이터를 저장하거나 알림 전송
////        } catch (JsonProcessingException e) {
////            log.error("Error processing received message", e);
////        }
////    }
//
//    public void fetchMessageFromQueue(String userQueueName) {
//        try {
//            // 큐에서 메시지를 가져옴
//            GetResponse response = rabbitTemplate.execute(channel -> channel.basicGet(userQueueName, false));
//
//            if (response != null) {
//                String messageBody = new String(response.getBody(), StandardCharsets.UTF_8);
//                // 역직렬화
//                Map<String, Object> messagePayload = objectMapper.readValue(messageBody, Map.class);
//
//                // 메시지 처리
//                log.info("Fetched message from queue {}: {}", userQueueName, messagePayload);
//
//                // 메시지를 처리한 후 ACK 보내기
//                long deliveryTag = response.getEnvelope().getDeliveryTag();
//                rabbitTemplate.execute(channel -> {
//                    channel.basicAck(deliveryTag, false);
//                    return null;
//                });
//            } else {
//                log.info("No message available in queue {}", userQueueName);
//            }
//        } catch (Exception e) {
//            log.error("Error fetching message from queue {}", userQueueName, e);
//        }
//    }
//
//    // 자산 총액
//    public Map<String, Object> userAssetTotal(String userName) {
//        log.info("execute userAssetTotal~~~");
//        Map<String, Object> answer = new HashMap<>();
////        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("totalAccount", myAssetDao.totalAccount(userName));
//        map.put("totalStock", myAssetDao.totalStock(userName));
//        map.put("totalRealestate", myAssetDao.totalRealestate(userName));
//        map.put("totalCar", myAssetDao.totalCar(userName));
//
//        answer.put("assetTotal", map);
//
//        return answer;
//    }
//
//    // 자산 세부 항목
//    public Map<String, Object> userDetailAsset(String userName) {
//        log.info("execute userDetailAsset~~~");
//        Map<String, Object> answer = new HashMap<>();
////        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("userAccount", myAssetDao.userAccounts(userName));
//        map.put("userStock", myAssetDao.userStockSymbol(userName));
//        map.put("userCar", myAssetDao.userCar(userName));
//        map.put("userRealestate", myAssetDao.userRealestate(userName));
//
//        answer.put("detailAsset", map);
//
//        return answer;
//    }
//
//
//    // 3-a 주식 자산 종목명, 보유 수량, 현재 가치, 매입가, 수익률
//    public Map<String, Object> userStockRevenue(String userName) throws IOException {
//        log.info("execute userStockRevenue~~~");
//        Map<String, Object> answer = new HashMap<>();
////        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Map<String, Object> map = new HashMap<>();
//
//        int userId = userDao.getUserId(userName);
//
//        List<StockHoldingsDto> lists = myAssetDao.getStock(userId);
//        for (StockHoldingsDto list : lists) {
//            String stockSymbol = list.getStockSymbol();
//            String stockName = list.getStockName();
//            int purchasePrice = list.getPurchasePrice();
//            int quantity = list.getQuantity();
//            Date purchaseDate = list.getPurchaseDate();
//
//            String p = StockSymbolProcessor.recentStock(stockSymbol);
//            int recentPrice = Integer.parseInt(p.replace(",", ""));
//
//            double revenue = ((double) recentPrice / purchasePrice - 1) * 100;
//            String revenueWithPercentage = revenue + "%";
//
//            Map<String, Object> map1 = new HashMap<>();
//            map1.put("stockSymbol", stockSymbol);
//            map1.put("stockName", stockName);
//            map1.put("purchasePrice", purchasePrice);
//            map1.put("quantity", quantity);
//            map1.put("purchaseDate", purchaseDate);
//            map1.put("revenue", revenueWithPercentage);
//
//            map.put(stockName, map1);
//        }
//
//        answer.put("stockRevenue", map);
//
//        return answer;
//    }
//
//
//
//}





















//package mul.cam.e.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.GetResponse;
//import lombok.extern.slf4j.Slf4j;
//import mul.cam.e.controller.NotificationController;
//import mul.cam.e.dao.MyAssetDao;
//import mul.cam.e.dao.UserDao;
//import mul.cam.e.dto.StockHoldingsDto;
//import mul.cam.e.util.StockSymbolProcessor;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Slf4j
//@Service
//public class RabbitService {
//    private final RabbitTemplate rabbitTemplate;
//    private final Queue queue;
//    private final NotificationController notificationController;
//    private final ObjectMapper objectMapper;
//    private final UserDao userDao;
//    private final MyAssetDao myAssetDao;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchange;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    public RabbitService(RabbitTemplate rabbitTemplate, Queue queue, NotificationController notificationController, ObjectMapper objectMapper, UserDao userDao, MyAssetDao myAssetDao) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.queue = queue;
//        this.notificationController = notificationController;
//        this.objectMapper = objectMapper;
//        this.userDao = userDao;
//        this.myAssetDao = myAssetDao;
//    }
//
//    /**
//     * 사용자 포트폴리오를 생성한 후 RabbitMQ 큐에 전송하는 메서드
//     */
//    public void createPortfolio(String userName) {
//        try {
//            log.info("Portfolio created for user: {}", userName);
//
//            Map<String, Object> data = new HashMap<>();
//            data.put("assetTotal", userAssetTotal(userName));
//            data.put("detailAsset", userDetailAsset(userName));
//            data.put("stockRevenue", userStockRevenue(userName));
//
//            // 데이터를 JSON 형태로 직렬화
//            String json = objectMapper.writeValueAsString(data);
//            String userQueueName = "portfolio_" + userName;
//
//            // 라우팅 키를 사용자 이름으로 설정하여 메시지를 전송
//            rabbitTemplate.execute(channel -> {
//                channel.queueDeclare(userQueueName, true, false, false, null);
//                return null;
//            });
//
//            rabbitTemplate.convertAndSend(exchange, routingKey, json);
//
//            // 사용자에게 알림 전송
//            notificationController.notifyUser("Portfolio creation completed for user: " + userName);
//
//        } catch (JsonProcessingException e) {
//            log.error("Error serializing portfolio data", e);
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            log.error("Error during RabbitMQ operation", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * RabbitMQ 큐에서 메시지를 수신하는 리스너
//     */
//    public void receiveMessageForUser(String message) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null) {
//            log.warn("No authentication available");
//            return;
//        }
//
//        String userName = authentication.getName();
//        String userQueueName = "portfolio_" + userName;
//
//        log.info("Received Message from user queue {}: {}", userQueueName, message);
//
//        try {
//            Map<String, Object> messagePayload = objectMapper.readValue(message, Map.class);
//            notificationController.notifyUser("Received portfolio data: " + messagePayload);
//        } catch (JsonProcessingException e) {
//            log.error("Error deserializing received message", e);
//        }
//    }
//
//
//    /**
//     * RabbitMQ 큐에서 메시지를 수동으로 가져오는 메서드
//     */
//    public void fetchMessageFromQueue(String userQueueName) {
//        try {
//            GetResponse response = rabbitTemplate.execute(channel -> channel.basicGet(userQueueName, false));
//
//            if (response != null) {
//                String messageBody = new String(response.getBody(), StandardCharsets.UTF_8);
//                Map<String, Object> messagePayload = objectMapper.readValue(messageBody, Map.class);
//
//                log.info("Fetched message from queue {}: {}", userQueueName, messagePayload);
//
//                long deliveryTag = response.getEnvelope().getDeliveryTag();
//                rabbitTemplate.execute(channel -> {
//                    channel.basicAck(deliveryTag, false);
//                    return null;
//                });
//            } else {
//                log.info("No message available in queue {}", userQueueName);
//            }
//        } catch (Exception e) {
//            log.error("Error fetching message from queue {}", userQueueName, e);
//        }
//    }
//
//    // 자산 총액
//    public Map<String, Object> userAssetTotal(String userName) {
//        Map<String, Object> assetTotal = new HashMap<>();
//        assetTotal.put("totalAccount", myAssetDao.totalAccount(userName));
//        assetTotal.put("totalStock", myAssetDao.totalStock(userName));
//        assetTotal.put("totalRealestate", myAssetDao.totalRealestate(userName));
//        assetTotal.put("totalCar", myAssetDao.totalCar(userName));
//        return Collections.singletonMap("assetTotal", assetTotal);
//    }
//
//    // 자산 세부 항목
//    public Map<String, Object> userDetailAsset(String userName) {
//        Map<String, Object> detailAsset = new HashMap<>();
//        detailAsset.put("userAccount", myAssetDao.userAccounts(userName));
//        detailAsset.put("userStock", myAssetDao.userStockSymbol(userName));
//        detailAsset.put("userCar", myAssetDao.userCar(userName));
//        detailAsset.put("userRealestate", myAssetDao.userRealestate(userName));
//        return Collections.singletonMap("detailAsset", detailAsset);
//    }
//
//    // 주식 수익률 계산
//    public Map<String, Object> userStockRevenue(String userName) throws IOException {
//        Map<String, Object> stockRevenue = new HashMap<>();
//        int userId = userDao.getUserId(userName);
//        List<StockHoldingsDto> holdings = myAssetDao.getStock(userId);
//
//        for (StockHoldingsDto holding : holdings) {
//            String stockSymbol = holding.getStockSymbol();
//            int purchasePrice = holding.getPurchasePrice();
//            int quantity = holding.getQuantity();
//            String recentPriceStr = StockSymbolProcessor.recentStock(stockSymbol);
//            int recentPrice = Integer.parseInt(recentPriceStr.replace(",", ""));
//            double revenue = ((double) recentPrice / purchasePrice - 1) * 100;
//            stockRevenue.put(holding.getStockName(), Map.of(
//                    "stockSymbol", stockSymbol,
//                    "purchasePrice", purchasePrice,
//                    "quantity", quantity,
//                    "revenue", revenue + "%"
//            ));
//        }
//        return Collections.singletonMap("stockRevenue", stockRevenue);
//    }
//}
