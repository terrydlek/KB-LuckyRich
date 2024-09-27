package mul.cam.e.service;

import lombok.AllArgsConstructor;
import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MyAssetService {

    private final MyAssetDao myAssetDao;


    public boolean setMyAccount(String account_number, String userName, int bank_id, int account_type_id, int balance) {
        return myAssetDao.setMyAccount(account_number, userName, bank_id, account_type_id, balance);
    }

    public void setTransaction(TransactionDto transactionDto) {
        myAssetDao.setTransaction(transactionDto);
    }

    public int getAccountNum(String account_number) { return myAssetDao.getAccountNum(account_number); }

    public void setStockHoldings(StockHoldingsDto stockHoldings) {
        myAssetDao.setStockHoldings(stockHoldings);
    }

    public List<TransactionDto> getTransactions(int userId) {
        return myAssetDao.getTransactions(userId);
    }
}
