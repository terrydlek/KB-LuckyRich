package mul.cam.e.controller;

import mul.cam.e.service.MyAssetService;
import mul.cam.e.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyAssetController {

    @Autowired
    private MyAssetService myAssetService;

    @GetMapping("account/accountRegi/MyBank")
    public List<AccountDTO> getAccounts() {
        return myAssetService.getAccounts();
    }
}
