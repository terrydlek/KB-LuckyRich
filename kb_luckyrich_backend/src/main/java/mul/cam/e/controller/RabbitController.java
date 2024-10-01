package mul.cam.e.controller;

import mul.cam.e.service.RabbitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    private final RabbitService rabbitService;

    public RabbitController(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage() {
        String testMessage = "Hello Rabbitmq";
        rabbitService.sendMessage(testMessage);
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

}
