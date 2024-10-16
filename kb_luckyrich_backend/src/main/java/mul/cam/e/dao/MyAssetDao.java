package mul.cam.e.dao;

import mul.cam.e.dto.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    int totalAccount(int userId);

    int totalStock(int userId);

    long totalRealestate(int userId);

    int totalCar(int userId);

    // 특정 사용자의 또래 유저 목록 가져오기
    List<UserDto> getPeerUsersByAge(int userId);

    // 특정 userId에 대한 Bank Balance 가져오기
    double getBankBalanceByUserId(int userId);

    // 특정 userId에 대한 Stock Total 가져오기
    double getStockTotalByUserId(int userId);

    // 특정 userId에 대한 Car Price 가져오기
    double getCarByUserId(int userId);

    // 특정 userId에 대한 Real Estate Price 가져오기
    double getRealestateByUserId(int userId);

    List<AccountDto> userAccounts(String userName);

    @MapKey("transaction_date")
    List<Map<String, Object>> transactionTen(String userName);

    List<StockHoldingsDto> userStockSymbol(String userName);

    MyCarDto userCar(int userId);

    RealEstateDto userRealestate(int userId);

    List<AccountDto> getAccounts(int userId);

    // 카테고리별 소비 지출
    @MapKey("category")
    List<Map<String, Object>> getCategoryExpenses(int userId);

    List<Integer> getCars();

    void setRealEstateAsset(@Param("randEstateNo") int randEstateNo, @Param("userId") int userId);

    void setCarAsset(@Param("randCarNo") int randCarNo, @Param("userId") int userId);

    List<Integer> getReals();
}
