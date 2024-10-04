package mul.cam.e.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.controller.NotificationController;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private final NotificationController notificationController;
    private final ObjectMapper objectMapper;

    public RabbitService(RabbitTemplate rabbitTemplate, Queue queue, NotificationController notificationController, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
        this.notificationController = notificationController;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String message) {
        log.info("sending message: {}", message);
        log.info(queue.getName());
//        rabbitTemplate.convertAndSend(queue.getName(), message);
        rabbitTemplate.convertAndSend("q.test", message);
    }

    // 여기에서 포트폴리오 생성 로직 처리하면 됨
    public void createPortfolio(String userId) {
        try {
            Thread.sleep(3000);
            log.info("Portfolio created for user: {}", userId);
            Map<String, Integer> map = new HashMap<>();
            map.put("1", 1);
            map.put("2", 2);
            map.put("3", 3);


            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            // String userQueueName = "portfolio_" + userName;
            String userQueueName = "portfolio";


            // 직렬화 한번 거쳐서 rabbit에 넣어줘야 함
            String json = objectMapper.writeValueAsString(map);

            // userQueueName으로 routerkey값이 없으면 rabbit에 새로 생성
            rabbitTemplate.execute(channel -> {
                channel.queueDeclare(userQueueName, true, false, false, null);
                return null;
            });

            rabbitTemplate.convertAndSend(userQueueName, json);
            // 사용자에게 알람 전송
            notificationController.notifyUser("Portfolio creation completed for user: " + userId);

            fetchMessageFromQueue("portfolio_" + SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (InterruptedException e) {
            log.error("Error creating portfolio", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = "q.test")
    public void receiveMessage(String message) {
        log.info("Received Message : {}", message);
    }

//    @RabbitListener(queues = "user_portfolio")
//    public void receiveUserPortfolio(String message) {
//        try {
//            // JSON 문자열을 Map으로 역직렬화
//            Map<String, Object> messagePayload = objectMapper.readValue(message, Map.class);
//            String userQueueName = (String) messagePayload.get("queueName");
//            Map<String, Integer> data = (Map<String, Integer>) messagePayload.get("data");
//
//            // Log the received message
//            log.info("Received User Portfolio from queue {}: {}", userQueueName, data);
//
//            // 필요한 추가 처리 수행
//            // 예를 들어, 포트폴리오 데이터를 저장하거나 알림 전송
//        } catch (JsonProcessingException e) {
//            log.error("Error processing received message", e);
//        }
//    }

    public void fetchMessageFromQueue(String userQueueName) {
        try {
            // 큐에서 메시지를 가져옴
            GetResponse response = rabbitTemplate.execute(channel -> channel.basicGet(userQueueName, false));

            if (response != null) {
                String messageBody = new String(response.getBody(), StandardCharsets.UTF_8);
                Map<String, Object> messagePayload = objectMapper.readValue(messageBody, Map.class);

                // 메시지 처리
                log.info("Fetched message from queue {}: {}", userQueueName, messagePayload);

                // 메시지를 처리한 후 ACK 보내기
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



}
