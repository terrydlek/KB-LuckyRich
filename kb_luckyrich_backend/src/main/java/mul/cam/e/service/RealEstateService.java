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

    public RealEstateService(RealEstateDao realEstateDao) {
        this.realEstateDao = realEstateDao;
    }


    public List<RealEstateDto> getEstate() {
        return realEstateDao.getRealEstates();
    }
}
