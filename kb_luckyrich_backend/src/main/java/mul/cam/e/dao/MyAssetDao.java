package mul.cam.e.dao;

import mul.cam.e.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MyAssetDao {

    boolean setMyAccount(@Param("account_number") String account_number,
                         @Param("userName") String userName,
                         @Param("bankId") int bankId,
                         @Param("accountTypeId") int accountTypeId,
                         @Param("balance") int balance);

    void setTransaction(TransactionDto transactionDto);

    int getAccountNum(String accountNumber);

    void setStockHoldings(StockHoldingsDto stockHoldings);
}
