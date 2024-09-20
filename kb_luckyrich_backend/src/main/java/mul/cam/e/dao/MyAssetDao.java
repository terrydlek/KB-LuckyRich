package mul.cam.e.dao;

import mul.cam.e.dto.MyAccountDto;
import mul.cam.e.dto.MyCarDto;
import mul.cam.e.dto.MyEstateDto;
import mul.cam.e.dto.MyStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MyAssetDao {
    List<MyStockDto> getMyStock(int userId);

    List<MyEstateDto> getMyEstate(int userId);

    List<MyCarDto> getMyCar(int userId);

    List<MyAccountDto> getMyAccount(int userId);
}
