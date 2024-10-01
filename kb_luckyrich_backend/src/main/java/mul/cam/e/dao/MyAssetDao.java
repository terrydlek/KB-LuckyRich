package mul.cam.e.dao;

import mul.cam.e.dto.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MyAssetDao {

    boolean setMyAccount(AccountDto accountDto);

    void setTransaction(TransactionDto transactionDto);

    int getAccountNum(String accountNumber);

    void setStockHoldings(StockHoldingsDto stockHoldings);

    List<TransactionDto> getTransactions(int userId);

    List<StockHoldingsDto> getStock(int userId);
    
    List<BankTransactionDto> getBankTransactions(int userId);

    int totalAccount(String userName);

    int totalStock(String userName);

    int totalRealestate(String userName);

    int totalCar(String userName);

    List<AccountDto> userAccounts(String userName);

    @MapKey("transaction_date")
    List<Map<String, Object>> transactionTen(String userName);

    List<StockHoldingsDto> userStockSymbol(String userName);

}
