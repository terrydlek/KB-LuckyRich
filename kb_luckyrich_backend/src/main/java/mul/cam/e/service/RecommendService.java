package mul.cam.e.service;

import mul.cam.e.dao.FundDao;
import mul.cam.e.dto.FundDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecommendService {

    private final FundDao fundDao;

    public RecommendService(FundDao fundDao) {
        this.fundDao = fundDao;
    }

    public List<FundDto> conservativeList() {
        return fundDao.conservativeList();
    }

    public List<FundDto> neutrilityList() {
        return fundDao.neutrilityList();
    }


}
