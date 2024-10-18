package mul.cam.e.controller;

import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.RabbitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    private final RabbitService rabbitService;
    private final SecurityUserService securityUserService;

    public RabbitController(RabbitService rabbitService, SecurityUserService securityUserService) {
        this.rabbitService = rabbitService;
        this.securityUserService = securityUserService;
    }

    @PostMapping("/portfolios")
    public ResponseEntity<String> sendMessage() throws InterruptedException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        rabbitService.createPortfolio(userName, securityUserService.getUserId(userName));
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

}
