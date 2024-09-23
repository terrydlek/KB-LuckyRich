package mul.cam.e.controller;

import mul.cam.e.service.RedisTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class RedisTestController {

    private final RedisTestService redisTestService;

    @Autowired
    public RedisTestController(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }
}