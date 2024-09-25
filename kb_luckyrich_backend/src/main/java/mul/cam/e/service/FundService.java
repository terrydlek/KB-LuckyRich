package mul.cam.e.service;

import mul.cam.e.dto.FundDto;
import mul.cam.e.scraper.FundSourceScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FundService {

    private final FundSourceScraper scraper;
    private final RedisService redisService;

    @Autowired
    public FundService(FundSourceScraper scraper, RedisService redisService) {
        this.scraper = scraper;
        this.redisService = redisService;
    }

    // 실제 웹 페이지에서 데이터를 가져와 Redis에 캐시
    public List<FundDto> getAndCacheFundsByRiskRating(int riskRating) throws IOException {
        // 웹에서 모든 펀드 데이터 스크래핑
        List<FundDto> allFunds = scraper.scrapeFunds();

        // 지정된 위험 등급 펀드 필터링 (2, 3)
        List<FundDto> filteredFunds = allFunds.stream().filter(fund -> fund.getRiskRating() == riskRating).collect(Collectors.toList());

        // 필터링된 데이터 Redis에 캐시
        String redisKey = "riskRating: " + riskRating;
        redisService.setData(redisKey, filteredFunds, 1440);

        return filteredFunds;
    }

    // 지정된 위험 등급의 펀드 목록 조회
    // Redis 캐시 확인 -> 없으면 새로운 데이터 가져와서 캐시
    public List<FundDto> getFundsByRiskRating(int riskRating) throws IOException {
        String redisKey = "riskRating: " + riskRating;

        long startTime = System.currentTimeMillis();

        List<FundDto> cachedData = redisService.getFundData(redisKey);

        if (cachedData == null) {
            // 캐시된 데이터 없으면 새로 가져오기

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("Redis 미 사용 시 실행 시간: " + executionTime + " 밀리초");

            return getAndCacheFundsByRiskRating(riskRating);
        } else {
            // Redis에 캐시된 데이터가 있는 경우 그대로 반환
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("Redis 사용 시(캐시) 실행 시간: " + executionTime + " 밀리초");

            return cachedData;
        }
    }

    public void invalidateFundCache() {
        redisService.invalidateCache("riskRating: 2");
        redisService.invalidateCache("riskRating: 3");
    }

    public FundDto getFundByUrl(String url) throws IOException {
        System.out.println("FundService getFundByUrl execute~~~");

        String completeUrl = "https://www.investing.com" + url;
        System.out.println(completeUrl);
        FundDto fund = new FundDto();

        // TODO: complete the logic to fetch fund details using completeUrl
        return fund;
    }

}
