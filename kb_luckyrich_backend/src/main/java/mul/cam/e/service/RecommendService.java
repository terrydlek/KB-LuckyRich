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

import java.util.List;

@Service
@Transactional
public class RecommendService {

    private final FundDao fundDao;
    private final DepositDao depositDao;

    public RecommendService(FundDao fundDao, DepositDao depositDao) {
        this.fundDao = fundDao;
        this.depositDao = depositDao;
    }

    public List<FundDto> conservativeList() {
        return fundDao.conservativeList();
    }

    public List<FundDto> neutrilityList() {
        return fundDao.neutrilityList();
    }

    public List<DepositDto> depositList() {
        return depositDao.getDeposit();
    }


}
