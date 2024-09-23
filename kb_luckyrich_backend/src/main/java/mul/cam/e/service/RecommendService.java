package mul.cam.e.service;

import mul.cam.e.dao.DepositDao;
import mul.cam.e.dao.FundDao;
import mul.cam.e.dto.DepositDto;
import mul.cam.e.dto.FundDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class RecommendService {

    private final FundService fundService;
    private final DepositDao depositDao;
    private final RedisService redisService;

    @Autowired
    public RecommendService(FundService fundService, DepositDao depositDao, RedisService redisService) {
        this.fundService = fundService;
        this.depositDao = depositDao;
        this.redisService = redisService;
    }

    public List<FundDto> conservativeList() throws IOException {
        return fundService.getFundsByRiskRating(2);
    }

    public List<FundDto> neutralList() throws IOException {
        return fundService.getFundsByRiskRating(3);
    }

    // 제욱님 예적금
    public List<DepositDto> depositList() {
        return depositDao.getDeposit();
    }

    public DepositDto getDepositByProdname(String prodname) {
        return depositDao.getDepositByProdname(prodname);
    }

}
