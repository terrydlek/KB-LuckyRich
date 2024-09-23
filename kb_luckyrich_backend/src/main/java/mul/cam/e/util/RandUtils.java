package mul.cam.e.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Random;

@Component
public class RandUtils {
    Random random = new Random();

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
        String[] transactionTypes = {"입금", "출금"};
        return transactionTypes[random.nextInt(transactionTypes.length)];
    }

    // 거래 액수 1000 - 100000
    public int getRandomTransactionAmount() {
        return (random.nextInt(100) + 1) * 1000;
    }

    // 거래 날짜
    public Timestamp getRandomTransactionDate() {
        return new Timestamp(System.currentTimeMillis() - random.nextInt(1000000000));
    }
}


