package mul.cam.e.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.MyAssetService;
import mul.cam.e.service.StockService;
import mul.cam.e.util.AccountType;
import mul.cam.e.util.RandUtils;
import mul.cam.e.util.BankName;
import mul.cam.e.util.TransactionGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/myasset")
public class MyAssetController {

    private final MyAssetService myAssetService;
    private final SecurityUserService securityUserService;
    private final RandUtils randUtils;
    private final TransactionGenerator transactionGenerator;
    private final StockService stockService;

    @GetMapping("/getMyAccount")
    public ResponseEntity<List<Map<String, Object>>> getMyAccount() {
        System.out.println("getMyAccount --------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        List<Map<String, Object>> accounts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
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
    public ResponseEntity<String> updateUserInfo(@RequestBody List<Map<String, Object>> requestBody) {
        System.out.println("fetchAccount -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);

        List<Map<String, Object>> accounts = requestBody;

        for (Map<String, Object> account : accounts) {
            String accountNum = (String) account.get("accountNumber");
            String bankName = (String) account.get("bankName");
            String accountType = (String) account.get("accountType");
            int balance = (int) account.get("balance");

            AccountDto accountDto = AccountDto.builder()
                    .accountNumber(accountNum)
                    .userId(userId)
                    .bankId(BankName.valueOf(bankName).getNum())
                    .accountTypeId(AccountType.valueOf(accountType).getNum())
                    .balance(balance)
                    .build();

            myAssetService.setMyAccount(accountDto);

            int account_id = myAssetService.getAccountNum(accountNum);

            // random 10 transaction
            for (int i = 0; i < 10; i++) {
                TransactionDto transactionDto = transactionGenerator.generateRandomTransactionDto(account_id);
                myAssetService.setTransaction(transactionDto);
            }
        }

        // random 2 stock
        try {
            for (int i = 0; i < 2; i++) {
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

    @GetMapping("getstock")
    ResponseEntity<Map<String, Object>> getStock() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);

        List<StockHoldingsDto> myStocks = myAssetService.getStocks(userId);

        List<StockDto> stocks = stockService.getStock();

        Map<String, Object> res = new HashMap<>();
        res.put("myStocks", myStocks);
        res.put("stocks", stocks);

        return ResponseEntity.ok(res);
    }
    
    @GetMapping("getbanktransaction")
    ResponseEntity<List<BankTransactionDto>> getBankTransaction() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);
        List<BankTransactionDto> bankTransactions = myAssetService.getBankTransactions(userId);

        return ResponseEntity.ok(bankTransactions);
    }

    @GetMapping("/total")
        public ResponseEntity<Map<String, Object>> getAssetTotal() {
        System.out.println("getAssetTotal execute~~~~~");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Map<String, Object> map = new HashMap<>();
        System.out.println(myAssetService.totalCar(userName));
        map.put("Bank Balance", myAssetService.totalAccount(userName));
        map.put("Stock Total", myAssetService.totalStock(userName));
        map.put("Car", myAssetService.totalCar(userName));
        map.put("real estate", myAssetService.totalRealestate(userName));

        return ResponseEntity.ok(map);
    }

}
