package mul.cam.e.service;

import mul.cam.e.dto.DepositDto;
import mul.cam.e.dto.StockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setData(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

    public void setStockData(String key, Object object) {
        // 5분 간격으로 Redis 갱신
        redisTemplate.opsForValue().set(key, object, 5, TimeUnit.MINUTES);
    }

    public void invalidateCache(String key) {
        redisTemplate.delete(key);
    }

    public List<DepositDto> getDepositData(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result instanceof List<?>) {
            return (List<DepositDto>) result;
        }
        return null;
    }

    public List<StockDto> getStockData(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result instanceof List<?>) {
            return (List<StockDto>) result;
        }
        return null;
    }



    public void testRedisConnection(Object object) {

        redisTemplate.opsForValue().set("testKey", object);

        Object result = redisTemplate.opsForValue().get("testKey");
        List<String> value = null;
        if (result instanceof List) {
            value = (List<String>) result;
        }
        System.out.println("Value: " + value);
    }
}
