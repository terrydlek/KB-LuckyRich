package mul.cam.e.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.controller.NotificationController;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;
    private NotificationController notificationController;

    public RabbitService(RabbitTemplate rabbitTemplate, Queue queue, NotificationController notificationController) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
        this.notificationController = notificationController;
    }

    public void sendMessage(String message) {
        log.info("sending message: {}", message);
//        rabbitTemplate.convertAndSend(queue.getName(), message);
        rabbitTemplate.convertAndSend("q.test", message);
    }

    // 여기에서 포트폴리오 생성 로직 처리하면 됨
    public void createPortfolio(String userId) {
        try {
            Thread.sleep(5000);
            log.info("Portfolio created for user: {}", userId);

            // 사용자에게 알람 전송
            notificationController.notifyUser("Portfolio creation compoleted for user: " + userId);
        } catch (InterruptedException e) {
            log.error("Error creating portfolio", e);
        }
    }


    @RabbitListener(queues = "q.test")
    public void receiveMessage(String message) {
        log.info("Received Message : {}", message);
    }

}
