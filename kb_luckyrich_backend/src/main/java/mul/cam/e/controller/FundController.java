package mul.cam.e.controller;

import mul.cam.e.dto.FundDto;
import mul.cam.e.scraper.FundSourceScraper;
import mul.cam.e.service.FundService;
import mul.cam.e.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    private final FundService fundService;
    private final RedisService redisService;

    // 생성자를 통한 의존성 주입을 위한 어노테이션
    @Autowired
    public FundController(FundService fundService, RedisService redisService) {
        this.fundService = fundService; // 주입 받은 Service 인스턴스를 필드에 할당
        this.redisService = redisService;
    }

    @GetMapping
    public ResponseEntity<List<FundDto>> getFunds(@RequestParam(required = false) Integer riskRating) throws IOException {
        if (riskRating == null) {
            System.out.println("Risk rating is null");
            return null;
        }
        return ResponseEntity.ok(fundService.getFundsByRiskRating(riskRating));
    }

    @GetMapping("/conservative")
    public ResponseEntity<List<FundDto>> getConservativeFunds() throws IOException {
        redisService.invalidateCache("riskRating: 2");
        redisService.invalidateCache("riskRating: 3");

        return ResponseEntity.ok(fundService.getFundsByRiskRating(2));
    }

    @GetMapping("/neutral")
    public ResponseEntity<List<FundDto>> getNeutralFunds() throws IOException {
        return ResponseEntity.ok(fundService.getFundsByRiskRating(3));
    }

    /*

    // GET 메서드로도 캐시를 삭제할 수 있도록 추가
    @GetMapping("/delete")
    public ResponseEntity<String> deleteFundCacheGet() {
        fundService.invalidateFundCache();
        return ResponseEntity.ok("Fund cache deleted successfully");
    }

    */
}
