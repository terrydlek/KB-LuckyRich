package mul.cam.e.controller;

import mul.cam.e.security.SecurityUser;
import mul.cam.e.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("inf")
    public ResponseEntity<SecurityUser> fetchUserInfo() {
        System.out.println("fetchUserInfo -------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        SecurityUser user = userService.loadUserByUsername(id);

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

        boolean isUpdated = userService.updateUserInfo(id, request);
//        System.out.println(isUpdated);

        if (isUpdated) {
            return ResponseEntity.ok("User information updated successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to update user information");
        }
    }
}
