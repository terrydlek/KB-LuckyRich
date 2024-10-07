package mul.cam.e.controller;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.security.SecurityUser;
import mul.cam.e.security.SecurityUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final SecurityUserService securityUserService;

    public UserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping
    public ResponseEntity<SecurityUser> fetchUserInfo() {
        log.info("fetchUserInfo -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        SecurityUser user = securityUserService.loadUserByUsername(userName);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> updateUserInfo(@RequestBody Map<String, String> request) {
        log.info("updateUserInfo -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        boolean isUpdated = securityUserService.updateUserInfo(id, request);

        if (isUpdated) {
            return ResponseEntity.ok("User information updated successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to update user information");
        }
    }

    @DeleteMapping("/withdrawUser/{userId}")
    public ResponseEntity<String> withdrawUser(@PathVariable("userId") String userId) {
        log.info("withdrawUser -------------------------");

        boolean isWithdraw = securityUserService.withdrawUser(Integer.parseInt(userId));

        if (isWithdraw) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(500).body("Failed to withdraw user");
        }
    }
}
