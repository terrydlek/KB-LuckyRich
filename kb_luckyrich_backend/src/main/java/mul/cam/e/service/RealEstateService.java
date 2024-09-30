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
            estateList = realEstateDao.getRealEstates();
            redisService.setData(redisKey, estateList, 1);
        }
        return estateList;
    }
}
