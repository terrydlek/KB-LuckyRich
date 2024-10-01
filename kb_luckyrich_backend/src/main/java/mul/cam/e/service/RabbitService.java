package mul.cam.e.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public void sendMessage(String message) {
        log.info("sending message: {}", message);
//        rabbitTemplate.convertAndSend(queue.getName(), message);
        rabbitTemplate.convertAndSend("q.test", message);
    }

    @RabbitListener(queues = "q.test")
    public void receiveMessage(String message) {
        log.info("Received Message : {}", message);
    }

    public void sendStockCrawlRequest() {
        rabbitTemplate.convertAndSend(queue.getName(), "Start stock crawling");
    }
}
