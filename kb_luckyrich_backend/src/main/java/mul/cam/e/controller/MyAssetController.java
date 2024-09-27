package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.MyAssetService;
import mul.cam.e.util.AccountType;
import mul.cam.e.util.RandUtils;
import mul.cam.e.util.BankName;
import mul.cam.e.util.TransactionGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j
@RequestMapping("/myasset")
public class MyAssetController {

    private final MyAssetService myAssetService;
    private final SecurityUserService securityUserService;
    private final RandUtils randUtils;
    private final TransactionGenerator transactionGenerator;

    public MyAssetController(MyAssetService myAssetService, SecurityUserService securityUserService, RandUtils randUtils, TransactionGenerator transactionGenerator) {
        this.myAssetService = myAssetService;
        this.securityUserService = securityUserService;
        this.randUtils = randUtils;
        this.transactionGenerator = transactionGenerator;
    }

    @GetMapping("/getMyAccount")
    public ResponseEntity<List<Map<String, Object>>> getMyAccount() {
        System.out.println("getMyAccount execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        List<Map<String, Object>> accounts = new ArrayList<>();

        for (int i=0; i<5; i++) {
            BankName rBankName = randUtils.getRandomBankName();
            String rAccountNum = randUtils.getAccountNum();
            int rBalance = randUtils.getRandomBalance();
            AccountType rAccountType = randUtils.getRandomAccountType();

            Map<String, Object> account = new HashMap<>();
            account.put("accountNumber", rAccountNum);
            account.put("bankName", rBankName.getName());
            account.put("accountType", rAccountType.getType());
            account.put("balance", rBalance);

            accounts.add(account);
        }

        return ResponseEntity.ok(accounts);
    }

    @PostMapping("fetchaccount")
    public ResponseEntity<String> updateUserInfo(@RequestBody Map<String, Object> requestBody) {
        System.out.println("fetchAccount -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);

        try {
            boolean b = myAssetService.setMyAccount(
                    (String) requestBody.get("accountNumber"),
                    userName,
                    BankName.valueOf((String) requestBody.get("bankName")).getNum(),
                    AccountType.valueOf((String) requestBody.get("accountType")).getNum(),
                    (Integer) requestBody.get("balance"));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n저장안됨...ㅜㅜ");
        }

        int account_id = myAssetService.getAccountNum((String) requestBody.get("accountNumber"));

        // random 10 transaction
        for (int i = 0; i < 10; i++) {
            TransactionDto transactionDto = transactionGenerator.generateRandomTransactionDto(account_id);
            myAssetService.setTransaction(transactionDto);
        }

        // random 2 stock
        try {
            for (int i=0; i<2; i++){
                StockDto stock = randUtils.getRandomStockDto();
                StockHoldingsDto stockHoldings = StockHoldingsDto.builder()
                        .userId(userId)
                        .stockSymbol(stock.getStockCode())
                        .stockName(stock.getStockName())
                        .purchasePrice(randUtils.getRandomStockPrice(stock.getStockPrice()))
                        .quantity(randUtils.getRandomNumber())
                        .purchaseDate(randUtils.getRandomPerchaseDate())
                        .build();

                myAssetService.setStockHoldings(stockHoldings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("User information updated successfully");
    }

    @GetMapping("gettransaction")
    ResponseEntity<List<TransactionDto>> getTransaction() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);

        List<TransactionDto> transactions = myAssetService.getTransactions(userId);

        return ResponseEntity.ok(transactions);
    }
}
