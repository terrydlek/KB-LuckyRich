package mul.cam.e.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RedisTestService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisTestService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testRedisConnection(Object object) {
        redisTemplate.opsForValue().set("testKey", object);
        Object result = redisTemplate.opsForValue().get("testKey");

        List<String> value = null;

        if (result instanceof List) {
            value = (List<String>) result;
        }

        log.info("Value:" + value);
    }
}