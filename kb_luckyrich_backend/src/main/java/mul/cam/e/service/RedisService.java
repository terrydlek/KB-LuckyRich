package mul.cam.e.service;

import mul.cam.e.dto.DepositDto;
import mul.cam.e.dto.FundDto;
import mul.cam.e.dto.RealEstateDto;
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

    public void setData(String key, Object object, int time) {
        redisTemplate.opsForValue().set(key, object, time,TimeUnit.MINUTES);
    }

    public List<FundDto> getFundData(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result instanceof List<?>) {
            return (List<FundDto>) result;
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

    public List<DepositDto> getDepositData(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result instanceof List<?>) {
            return (List<DepositDto>) result;
        }
        return null;
    }

    public List<RealEstateDto> getRealEstateData(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result instanceof List<?>) {
            return (List<RealEstateDto>) result;
        }
        return null;
    }

    public void invalidateCache(String key) {
        redisTemplate.delete(key);
    }

}
