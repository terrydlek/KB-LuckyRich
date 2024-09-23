package mul.cam.e.controller;

import mul.cam.e.service.RabbitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asset")
public class RabbitController {
    private final RabbitService rabbitService;

    public RabbitController(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        rabbitService.sendMessage(message);
        return "Message sent to RabbitMQ!";
    }

}
