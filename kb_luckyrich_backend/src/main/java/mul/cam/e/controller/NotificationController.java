package mul.cam.e.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyUser(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void sendPortfolioUpdate(String userName, Map<String, Object> portfolioData) {
        log.info("Sending portfolio update~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        this.messagingTemplate.convertAndSend("/topic/portfolio/luckyrich", portfolioData);
    }
}
