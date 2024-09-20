package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.service.MyAssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j
@RequestMapping("/myasset")
public class MyAssetController {

    private final MyAssetService myAssetService;


    public MyAssetController(MyAssetService myAssetService) {
        this.myAssetService = myAssetService;
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
    public ResponseEntity<List<MyAccountDto>> getMyAccount() {
        System.out.println("getMyAccount execute~~~~~");
        return ResponseEntity.ok(myAssetService.getMyAccount(10));
    }
}
