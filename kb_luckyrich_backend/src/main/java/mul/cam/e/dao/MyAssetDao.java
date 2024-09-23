package mul.cam.e.dao;

import mul.cam.e.dto.AccountDto;
import mul.cam.e.dto.MyCarDto;
import mul.cam.e.dto.MyEstateDto;
import mul.cam.e.dto.MyStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MyAssetDao {
    List<MyStockDto> getMyStock(int userId);

    List<MyEstateDto> getMyEstate(int userId);

    List<MyCarDto> getMyCar(int userId);

    List<AccountDto> getMyAccount(int userId);

    boolean setMyAccount(@Param("account_number") String account_number,
                         @Param("email") String email,
                         @Param("bankId") int bankId,
                         @Param("accountTypeId") int accountTypeId,
                         @Param("balance") int balance);
}
