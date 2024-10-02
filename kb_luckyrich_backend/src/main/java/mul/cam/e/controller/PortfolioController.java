package mul.cam.e.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.security.SecurityUser;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.service.MyAssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/portfolio")
@AllArgsConstructor
public class PortfolioController {

    private final SecurityUserService securityUserService;
    private final MyAssetService myAssetService;


    @GetMapping("/userInfo")
    public ResponseEntity<SecurityUser> getUserInfo() {
        log.info("execute portfolio/userInfo ~~~");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return ResponseEntity.ok(securityUserService.loadUserByUsername(userName));
    }


}
