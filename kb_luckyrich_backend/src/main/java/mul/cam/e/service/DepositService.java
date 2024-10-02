package mul.cam.e.service;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.DepositDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DepositService {

    private final RedisService redisService;
    private final RecommendService recommendService;

    public DepositService(RedisService redisService, RecommendService recommendService) {
        this.redisService = redisService;
        this.recommendService = recommendService;
    }

    public List<DepositDto> getDeposit() {
        log.info("DepositService getDeposit execute~~~");

        // Redis에서 먼저 확인하고 없으면 DB에서 가져오기
        String redisKey = "depositList";
        List<DepositDto> depositList = redisService.getDepositData(redisKey);

        if (depositList == null || depositList.isEmpty()) {
            log.info("There is no data in Redis. Retrieved from DB.");
            depositList = recommendService.depositList();
            redisService.setData(redisKey, depositList, 1440);
        } else log.info("Retrieving data from Redis.");

        return depositList;
    }

    public DepositDto getDepositDetail(String prodname) {
        log.info("DepositService getDepositDetail execute~~~");
        return recommendService.getDepositByProdname(prodname);
    }


}
