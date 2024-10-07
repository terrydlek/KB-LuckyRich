package mul.cam.e.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.*;
import mul.cam.e.enumrate.AccountType;
import mul.cam.e.enumrate.BankName;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.MyAssetService;
import mul.cam.e.service.StockService;
import mul.cam.e.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final KeyEncrypt keyEncrypt;

    @GetMapping("/myAccount")
    public ResponseEntity<List<Map<String, Object>>> getMyAccount() {
        log.info("getMyAccount --------------------");

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

    @PostMapping("/myAccount")
    public ResponseEntity<String> updateUserInfo(@RequestBody List<Map<String, Object>> requestBody) {
        log.info("fetchAccount -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);

        List<Map<String, Object>> accounts = requestBody;

        for (Map<String, Object> account : accounts) {
            String accountNum = keyEncrypt.encrypt((String) account.get("accountNumber"));
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

            // random transaction
            for (int i = 0; i < 30; i++) {
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

    @GetMapping("/transaction")
    ResponseEntity<List<TransactionDto>> getTransaction() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);

        List<TransactionDto> transactions = myAssetService.getTransactions(userId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/stock")
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

    @GetMapping("/bankTransaction")
    ResponseEntity<List<BankTransactionDto>> getBankTransaction() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName);
        List<BankTransactionDto> bankTransactions = myAssetService.getBankTransactions(userId);

        return ResponseEntity.ok(bankTransactions);
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getAssetTotal() {
        log.info("getAssetTotal execute~~~~~");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Map<String, Object> map = new HashMap<>();
        map.put("Bank Balance", myAssetService.totalAccount(userName));
        map.put("Stock Total", myAssetService.totalStock(userName));
        map.put("Car", myAssetService.totalCar(userName));
        map.put("real estate", myAssetService.totalRealestate(userName));

        System.out.println(map);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> accounts() {
        log.info("accounts execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        List<AccountDto> dto = myAssetService.userAccounts(userName);

//        System.out.println(dto + "              ~~``dckqopckqdwopdwcqkopdwkcopcwdqkpdcwqkpodcwq ");
//
//        Map<String, Object> map = new HashMap<>();
//        for (AccountDto account : dto) {
//            if (account.getBankId() == 1) {
//                map.put("국민은행", account.getBalance());
//            } else if (account.getBankId() == 2) {
//                map.put("카카오뱅크", account.getBalance());
//            } else if (account.getBankId() == 3) {
//                map.put("신한은행", account.getBalance());
//            }
//        }
//        log.info(map);
        System.out.println(dto + "       ~~~~~~~~~~~~~~");
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/idTrend")
    public ResponseEntity<Map<String, BigInteger>> idTrend() throws IOException {
        log.info("idTrend execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        List<Map<String, Object>> transaction = myAssetService.transactionTen(userName);
        log.info(transaction);

        Map<String, Map<String, List<String>>> symbol = myAssetService.userStockSymbol(userName);
        log.info(symbol);

        Map<String, BigInteger> answer = StockSymbolProcessor.calculateAssetTrend(transaction, symbol);

        return ResponseEntity.ok(answer);
    }

    @GetMapping("/categoryExpenses")
    public ResponseEntity<Map<String, Object>> getCategoryExpenses() {
        log.info("getCategoryExpenses execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        int userId = securityUserService.getUserId(userName);

        List<Map<String, Object>> categoryExpenses = myAssetService.getCategoryExpenses(userId);

        // 총 지출액 계산
        double totalExpense = categoryExpenses.stream()
                .mapToDouble(expense -> ((Number) expense.get("amount")).doubleValue())
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("categoryExpenses", categoryExpenses);
        response.put("totalExpense", totalExpense);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/totalInvestment")
    public ResponseEntity<Map<String, Object>> getCurrentTotalStockValue() {
        log.info("getTotalInvestment execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Map<String, Object> response = new HashMap<>();

        try {
            BigDecimal currentTotalStockValue = myAssetService.getCurrentTotalStockValue(userName);
            response.put("currentTotalStockValue", currentTotalStockValue);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error while fetching current total stock value for user: " + userName, e);
            response.put("status", "error");
            response.put("message", "Failed to fetch current stock value. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
