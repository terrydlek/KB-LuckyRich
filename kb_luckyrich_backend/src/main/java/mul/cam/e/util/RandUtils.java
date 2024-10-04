package mul.cam.e.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import mul.cam.e.dto.StockDto;
import mul.cam.e.enumrate.AccountType;
import mul.cam.e.enumrate.BankName;
import mul.cam.e.enumrate.TransactionType;
import mul.cam.e.service.StockService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

@Component
public class RandUtils {

    Random random = new Random();
    ObjectMapper objectMapper = new ObjectMapper();

    private final StockService stockService;

    public RandUtils(StockService stockService) {
        this.stockService = stockService;
    }

    // 랜덤 계좌 ****-**-****** 형식
    public String getAccountNum() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        sb.append("-");
        for (int i = 0; i < 2; i++) {
            sb.append(random.nextInt(10));
        }
        sb.append("-");
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    // 은행 이름
    public BankName getRandomBankName() {
        BankName[] bankNames = BankName.values();
        return bankNames[random.nextInt(bankNames.length)];
    }

    // 계좌 타입
    public AccountType getRandomAccountType() {
        AccountType[] accountTypes = AccountType.values();
        return accountTypes[random.nextInt(accountTypes.length)];
    }

    // 잔액 1000 - 10000000
    public int getRandomBalance() {
        return (random.nextInt(10000) + 1) * 1000;
    }

    // 거래 타입
    public String getRandomTransactionType() {
        TransactionType[] transactionTypes = TransactionType.values();
        return String.valueOf(transactionTypes[random.nextInt(transactionTypes.length)]);
    }

    // 거래 액수 1000 - 100000
    public int getRandomTransactionAmount() {
        return (random.nextInt(100) + 1) * 1000;
    }

    // 거래 날짜
    public Timestamp getRandomTransactionDate() {
        return new Timestamp(System.currentTimeMillis() - random.nextInt(2000000000));
    }

    // Stock
    public StockDto getRandomStockDto() {
        int r = random.nextInt(100);

        List<StockDto> stocks;

        try {
            stocks = stockService.getStock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        StockDto rStock = stocks.get(r);
        StockDto rStock = objectMapper.convertValue(stocks.get(r), StockDto.class);
        return rStock;
    }

    public int getRandomStockPrice(String stockPrice) {
        String cleanInput = stockPrice.replaceAll(",", "");
        int price = Integer.parseInt(cleanInput);

        int min = -30;
        int max = 30;
        int randomNumber = random.nextInt((max - min) + 1) + min;

        int diff = (((price * randomNumber)/100) + price);
        return diff;
    }

    public Date getRandomPerchaseDate() {
        return new Date(System.currentTimeMillis() - random.nextInt(1000000000));
    }

    public int getRandomNumber() {
        return random.nextInt(30) + 1;
    }
}


