package mul.cam.e.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public RabbitService(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(queue.getName(), message);
    }

    public void sendStockCrawlRequest() {
        rabbitTemplate.convertAndSend(queue.getName(), "Start stock crawling");
    }
}
