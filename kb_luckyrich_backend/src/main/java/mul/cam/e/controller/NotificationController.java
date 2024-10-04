package mul.cam.e.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
        System.out.println("Sending portfolio update~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        this.messagingTemplate.convertAndSend("/topic/portfolio/luckyrich", portfolioData);
    }
}
