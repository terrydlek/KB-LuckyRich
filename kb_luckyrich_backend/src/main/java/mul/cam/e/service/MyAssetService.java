package mul.cam.e.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.*;
import mul.cam.e.util.KeyDecrypt;
import mul.cam.e.util.StockSymbolProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class MyAssetService {

    private final MyAssetDao myAssetDao;

    private final KeyDecrypt keyDecrypt;

    public boolean setMyAccount(AccountDto accountDto) {
        return myAssetDao.setMyAccount(accountDto);
    }

    public void setTransaction(TransactionDto transactionDto) {
        myAssetDao.setTransaction(transactionDto);
    }

    public int getAccountNum(String account_number) { return myAssetDao.getAccountNum(account_number); }

    public void setStockHoldings(StockHoldingsDto stockHoldings) {
        myAssetDao.setStockHoldings(stockHoldings);
    }

    public List<AccountDto> getAccounts(int userId) { return myAssetDao.getAccounts(userId); }

    public List<TransactionDto> getTransactions(int userId) {
        return myAssetDao.getTransactions(userId);
    }

    public List<StockHoldingsDto> getStocks(int userId) {
        return myAssetDao.getStock(userId);
    }
        
    public List<BankTransactionDto> getBankTransactions(int userId) {

        List<BankTransactionDto> bankTransactions = myAssetDao.getBankTransactions(userId);

        for (BankTransactionDto account : bankTransactions) {
            String decryptedAccountNumber = account.getDecryptedAccountNumber(keyDecrypt);
            System.out.println(decryptedAccountNumber);
            account.setAccountNumber(decryptedAccountNumber);
        }

        return bankTransactions;
    }

    public int totalAccount(int userId) {
        return myAssetDao.totalAccount(userId);
    }

    public int totalStock(int userId) {
        return myAssetDao.totalStock(userId);
    }

    public BigDecimal getCurrentTotalStockValue(String userName) throws IOException {
        List<StockHoldingsDto> userStocks = myAssetDao.userStockSymbol(userName);
        BigDecimal totalValue = BigDecimal.ZERO;

        for (StockHoldingsDto stock : userStocks) {
            String stockSymbol = stock.getStockSymbol();
            int quantity = stock.getQuantity();

            String currentPriceStr = StockSymbolProcessor.recentStock(stockSymbol);
            BigDecimal currentPrice = new BigDecimal(currentPriceStr.replace(",", ""));

            totalValue = totalValue.add(currentPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        return totalValue;
    }

    public long totalRealestate(int userId) {
        return myAssetDao.totalRealestate(userId);
    }

    public int totalCar(int userId) {
        return myAssetDao.totalCar(userId);
    }

    public List<AccountDto> userAccounts(String userName) {
        List<AccountDto> accounts = myAssetDao.userAccounts(userName);

        for (AccountDto account : accounts) {
            String decryptedAccountNumber = account.getDecryptedAccountNumber(keyDecrypt);
            System.out.println(decryptedAccountNumber + "Decrypt");
            account.setAccountNumber(decryptedAccountNumber);
            log.info("Set Decrypt Account");
        }

        return accounts;
    }

    public List<Map<String, Object>> transactionTen(String userName) {
        return myAssetDao.transactionTen(userName);
    }

    public List<Map<String, Object>> getCategoryExpenses(int userId) {
        return myAssetDao.getCategoryExpenses(userId);
    }

    public Map<String, Map<String, List<String>>> userStockSymbol(String userName) {

        List<StockHoldingsDto> lists = myAssetDao.userStockSymbol(userName);
        Map<String, Map<String, List<String>>> map = new HashMap<>();

        for (StockHoldingsDto symbol : lists) {
            try {
                Map<String, List<String>> s = StockSymbolProcessor.processStockSymbol(symbol.getStockSymbol(), symbol.getQuantity());
                map.put(symbol.getStockSymbol(), s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void setRealAsset(int randEstateNo, int userId) {
        myAssetDao.setRealEstateAsset(randEstateNo, userId);
    }
    public void setCarAsset(int randCarNo, int userId) {
        myAssetDao.setCarAsset(randCarNo, userId);
    }


    public List<Integer> getCars() {
        return myAssetDao.getCars();
    }

    public List<Integer> getReals() {
        return myAssetDao.getReals();
    }
}
