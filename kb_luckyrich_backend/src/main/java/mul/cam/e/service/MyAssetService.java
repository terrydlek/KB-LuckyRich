package mul.cam.e.service;

import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.MyAccountDto;
import mul.cam.e.dto.MyCarDto;
import mul.cam.e.dto.MyEstateDto;
import mul.cam.e.dto.MyStockDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MyAssetService {

    private final MyAssetDao myAssetDao;


    public MyAssetService(MyAssetDao myAssetDao) {
        this.myAssetDao = myAssetDao;
    }

    public List<MyStockDto> getMyStock(int userId) {
        return myAssetDao.getMyStock(userId);
    }

    public List<MyEstateDto> getMyEstate(int userId) {
        return myAssetDao.getMyEstate(userId);
    }

    public List<MyCarDto> getMyCar(int userId) {
        return myAssetDao.getMyCar(userId);
    }

    public List<MyAccountDto> getMyAccount(int userId) {
        return myAssetDao.getMyAccount(userId);
    }

}
