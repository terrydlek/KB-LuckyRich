package mul.cam.e.controller;

import mul.cam.e.security.SecurityUser;
import mul.cam.e.security.SecurityUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SecurityUserService securityUserService;

    public UserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping("inf")
    public ResponseEntity<SecurityUser> fetchUserInfo() {
        System.out.println("fetchUserInfo -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        SecurityUser user = securityUserService.loadUserByUsername(userName);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("update")
    public ResponseEntity<String> updateUserInfo(@RequestBody Map<String, String> request) {
        System.out.println("updateUserInfo -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        boolean isUpdated = securityUserService.updateUserInfo(id, request);
//        System.out.println(isUpdated);

        if (isUpdated) {
            return ResponseEntity.ok("User information updated successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to update user information");
        }
    }
}
