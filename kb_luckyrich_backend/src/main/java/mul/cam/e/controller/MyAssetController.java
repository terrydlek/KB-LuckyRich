package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.service.MyAssetService;
import mul.cam.e.util.AccountType;
import mul.cam.e.util.AccountUtils;
import mul.cam.e.util.BankName;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j
@RequestMapping("/myasset")
public class MyAssetController {

    private final MyAssetService myAssetService;
    private final AccountUtils accountUtils;

    public MyAssetController(MyAssetService myAssetService, AccountUtils accountUtils) {
        this.myAssetService = myAssetService;
        this.accountUtils = accountUtils;
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
    public ResponseEntity<List<AccountDto>> getMyAccount() {
        System.out.println("getMyAccount execute~~~~~");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<AccountDto> accounts = new ArrayList<>();

        for (int i=0; i<5; i++) {
            BankName rBankName = accountUtils.getRandomBankName();
            String rAccountNum = accountUtils.getAccountNum();
            int rBalance = accountUtils.getRandomBalance();
            AccountType rAccountType = accountUtils.getRandomAccountType();

            accounts.add(new AccountDto(rAccountNum, 0, rBankName.getNum(), rAccountType.getNum(), rBalance));
        }

//        try {
//            boolean b = myAssetService.setMyAccount(rAccountNum, email, rBankName.getNum(), rAccountType.getNum(), 1000000);
//        } catch (Exception e) {
//            System.out.println(e.getMessage() + "\n저장안됨...ㅜㅜ");
//        }
//        System.out.println("rBankName: " + rBankName);
//        System.out.println("rAccountNum: " + rAccountNum);
//        System.out.println("rBalance: " + rBalance);
//        System.out.println("rAccountType: " + rAccountType);

        return ResponseEntity.ok(accounts);
    }
}
