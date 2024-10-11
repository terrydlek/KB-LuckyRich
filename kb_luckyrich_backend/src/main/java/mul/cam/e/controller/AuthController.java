package mul.cam.e.controller;

import mul.cam.e.jwt.JwtTokenProvider;
import mul.cam.e.security.SecurityUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUserService securityUserService;

    public AuthController(JwtTokenProvider jwtTokenProvider, SecurityUserService securityUserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityUserService = securityUserService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken != null && jwtTokenProvider.validateRefreshToken(refreshToken)) {
            String id = jwtTokenProvider.getIdFromRefreshToken(refreshToken);
            String newAccessToken = jwtTokenProvider.createToken(id);
            String newRefreshToken = jwtTokenProvider.createRefreshToken(id);

            // 새로운 refreshToken 저장
            securityUserService.updateRefreshToken(Integer.parseInt(id), newRefreshToken);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
