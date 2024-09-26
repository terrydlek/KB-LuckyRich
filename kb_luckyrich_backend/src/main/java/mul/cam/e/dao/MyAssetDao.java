package mul.cam.e.dao;

import mul.cam.e.dto.*;
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
                         @Param("userName") String userName,
                         @Param("bankId") int bankId,
                         @Param("accountTypeId") int accountTypeId,
                         @Param("balance") int balance);

    boolean setTransaction(TransactionDto transactionDto);

    int getAccountNum(String accountNumber);
}
