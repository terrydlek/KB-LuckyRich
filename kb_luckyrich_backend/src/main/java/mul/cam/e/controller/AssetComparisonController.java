package mul.cam.e.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import mul.cam.e.service.AssetComparisonService;
import mul.cam.e.security.SecurityUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/myasset")
public class AssetComparisonController {

    private final AssetComparisonService assetComparisonService;
    private final SecurityUserService securityUserService; // 의존성 주입을 위한 필드 추가

    @GetMapping("/their-assets")
    public ResponseEntity<Map<String, Double>> getPeerAssets() {
        System.out.println("getPeerAssets~~~~~~~~~~~~");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        int userId = securityUserService.getUserId(userName); // securityUserService 사용

        System.out.println("User ID: " + userId);

        Map<String, Double> peerAverageAssets = assetComparisonService.getPeerAverageAssets(userId);

        System.out.println("Peer Average Assets: " + peerAverageAssets);

        return ResponseEntity.ok(peerAverageAssets);
    }
}