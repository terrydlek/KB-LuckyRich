package mul.cam.e.controller;

import mul.cam.e.dto.FundDto;
import mul.cam.e.scraper.FundSourceScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    private final FundSourceScraper scraper; // Scraper 타입의 필드 설정

    // 생성자를 통한 의존성 주입을 위한 어노테이션
    @Autowired
    public FundController(FundSourceScraper scraper) {
        this.scraper = scraper; // 주입 받은 Scraper 인스턴스를 필드에 할당
    }

    // HTTP GET 요청 처리
    // @RequestParam URL의 쿼리 파라미터에서 riskRating 값 가져옴
    @GetMapping
    public List<FundDto> getFunds(@RequestParam int riskRating) throws IOException {
        // 모든 펀드 데이터 스크래핑
        List<FundDto> allFunds = scraper.scrapeFunds();

        return allFunds.stream()
                .filter(fund -> fund.getRiskRating() == riskRating) // 위험 등급 일치하는 펀드만 필터링
                .collect(Collectors.toList()); // 필터링된 결과를 리스트로 가져와서 반환
    }
}
