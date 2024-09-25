package mul.cam.e.service;

import mul.cam.e.dao.RealEstateDao;
import mul.cam.e.dto.RealEstateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RealEstateService {
    private final RealEstateDao realEstateDao;
    private final RedisService redisService;

    public RealEstateService(RealEstateDao realEstateDao, RedisService redisService) {
        this.realEstateDao = realEstateDao;
        this.redisService = redisService;
    }


    public List<RealEstateDto> getEstate() {
        String redisKey = "realestateList";
        List<RealEstateDto> estateList = redisService.getRealEstateData(redisKey);
        if (estateList == null || estateList.isEmpty()) {
            // Redis에 데이터가 없을 경우 데이터베이스에서 조회
            estateList = realEstateDao.getRealEstates();
            // 조회한 데이터를 Redis에 캐싱 (유효 기간: 5분)
            redisService.setData(redisKey, estateList, 100);
        }
        return estateList;
    }
}
