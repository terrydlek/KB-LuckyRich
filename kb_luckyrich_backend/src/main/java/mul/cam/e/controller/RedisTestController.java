package mul.cam.e.controller;

import mul.cam.e.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RedisTestController {

    private final RedisService redisService;

    @Autowired
    public RedisTestController(RedisService redisService) {
        this.redisService = redisService;
    }
}