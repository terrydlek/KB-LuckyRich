package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
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
    private final RandUtils randUtils;
    private final TransactionGenerator transactionGenerator;

    public MyAssetController(MyAssetService myAssetService, RandUtils randUtils, TransactionGenerator transactionGenerator) {
        this.myAssetService = myAssetService;
        this.randUtils = randUtils;
        this.transactionGenerator = transactionGenerator;
    }

    @GetMapping("/getMyStock")
    public ResponseEntity<List<MyStockDto>> getMyStock() {
        System.out.println("getMyStock execute~~~~~");
        return ResponseEntity.ok(myAssetService.getMyStock(10));
    }

    @GetMapping("/getMyEstate")
    public ResponseEntity<List<MyEstateDto>> getMyEstate() {
        System.out.println("getMyEstate execute~~~~~");
        return ResponseEntity.ok(myAssetService.getMyEstate(10));
    }

    @GetMapping("/getMyCar")
    public ResponseEntity<List<MyCarDto>> getMyCar() {
        System.out.println("getMyCar execute~~~~~");
        return ResponseEntity.ok(myAssetService.getMyCar(10));
    }

    @GetMapping("/getMyAccount")
    public ResponseEntity<List<Map<String, Object>>> getMyAccount() {
        System.out.println("getMyAccount execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

//        List<AccountDto> accounts = new ArrayList<>();
        List<Map<String, Object>> accounts = new ArrayList<>();

        for (int i=0; i<5; i++) {
            BankName rBankName = randUtils.getRandomBankName();
            String rAccountNum = randUtils.getAccountNum();
            int rBalance = randUtils.getRandomBalance();
            AccountType rAccountType = randUtils.getRandomAccountType();

//            accounts.add(new AccountDto(rAccountNum, 0, rBankName.getNum(), rAccountType.getNum(), rBalance));

            Map<String, Object> account = new HashMap<>();
            account.put("accountNumber", rAccountNum);
            account.put("bankName", rBankName.getName());
            account.put("accountType", rAccountType.getType());
            account.put("balance", rBalance);

            accounts.add(account);

//            myAssetService.setTransaction(transactionGenerator.generateRandomTransactionDto(1));
        }

        return ResponseEntity.ok(accounts);
    }

    @PostMapping("fetchaccount")
    public ResponseEntity<String> updateUserInfo(@RequestBody Map<String, Object> requestBody) {
        System.out.println("fetchAccount -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println(email);

        try {
            boolean b = myAssetService.setMyAccount((String) requestBody.get("accountNumber"), email, BankName.valueOf((String) requestBody.get("bankName")).getNum(), AccountType.valueOf((String) requestBody.get("accountType")).getNum(), (Integer) requestBody.get("balance"));
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n저장안됨...ㅜㅜ");
        }

        int account_id = myAssetService.getAccountNum((String) requestBody.get("accountNumber"));

        for (int i = 0; i < 10; i++) {
            TransactionDto transactionDto = transactionGenerator.generateRandomTransactionDto(account_id);
            myAssetService.setTransaction(transactionDto);
        }


        return ResponseEntity.ok("User information updated successfully");
    }
}
