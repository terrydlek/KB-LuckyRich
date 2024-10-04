package mul.cam.e.service;

import lombok.AllArgsConstructor;
import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.*;
import mul.cam.e.util.StockSymbolProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
public class MyAssetService {

    private final MyAssetDao myAssetDao;


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
        return myAssetDao.getBankTransactions(userId);
    }

    public int totalAccount(String userName) {
        return myAssetDao.totalAccount(userName);
    }

    public int totalStock(String userName) {
        return myAssetDao.totalStock(userName);
    }

    public int totalRealestate(String userName) {
        return myAssetDao.totalRealestate(userName);
    }

    public int totalCar(String userName) {
        return myAssetDao.totalCar(userName);
    }

    public List<AccountDto> userAccounts(String userName) {
        return myAssetDao.userAccounts(userName);
    }

    public List<Map<String, Object>> transactionTen(String userName) {
        return myAssetDao.transactionTen(userName);
    }

    public List<Map<String, Object>> getCategoryExpenses(int userId) {
        return myAssetDao.getCategoryExpenses(userId);
    }

    public Map<String, Map<String, List<String>>> userStockSymbol(String userName) throws IOException {

        List<StockHoldingsDto> lists = myAssetDao.userStockSymbol(userName);
        Map<String, Map<String, List<String>>> map = new HashMap<>();

        for (StockHoldingsDto symbol : lists) {
            try {
//                String url = "https://finance.naver.com/item/sise_day.naver?code=" + symbol.getStockSymbol();
//                Document doc = Jsoup.connect(url).get();
//
//                List<String> dates = new ArrayList<>();
//                List<String> closingPrices = new ArrayList<>();
//
//                Elements rows = doc.select("table.type2 tr");
//
//                for (Element row : rows) {
//                    Elements dateElement = row.select("tr td span.tah.p10.gray03");
//                    Elements priceElement = row.select("tr td.num span.tah.p11");
//
//                    if (!dateElement.isEmpty() && !priceElement.isEmpty()) {
//                        String date = dateElement.text();
//                        String closingPrice = priceElement.get(0).text();
//
//                        dates.add(date);
//                        closingPrices.add(closingPrice);
//
//                        if (dates.size() == 10 && closingPrices.size() == 10) break;
//                    }
//                }
//
//                List<String> q = new ArrayList<>();
//                q.add(symbol.getQuantity() + "");

                Map<String, List<String>> s = StockSymbolProcessor.processStockSymbol(symbol.getStockSymbol(), symbol.getQuantity());
//                s.put("Dates", dates);
//                s.put("ClosingPrices", closingPrices);
//                s.put("quantity", q);

//                System.out.println("Dates: " + dates);
//                System.out.println("Closing Prices: " + closingPrices);

                map.put(symbol.getStockSymbol(), s);
//                map = StockSymbolProcessor.processStockSymbol(symbol.getStockSymbol(), symbol.getQuantity());
//                System.out.println(map);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}
