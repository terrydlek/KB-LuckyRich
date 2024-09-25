package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.service.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

@RestController
@Log4j
@RequestMapping("/recommend")
public class RecommendController {

    private final RedisService redisService;
    private final RecommendService recommendService;
    private final RabbitService rabbitService;
    private final FundService fundService;
    private final StockService stockService;
    private final DepositService depositService;

    @Autowired
    public RecommendController(RedisService redisService, RecommendService recommendService, RabbitService rabbitService, FundService fundService, StockService stockService, DepositService depositService) {
        this.redisService = redisService;
        this.recommendService = recommendService;
        this.rabbitService = rabbitService;
        this.fundService = fundService;
        this.stockService = stockService;
        this.depositService = depositService;
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

    @GetMapping("/funds/{url}")
    public ResponseEntity<FundDto> getFundByUrl(@PathVariable String url) throws IOException {
        try {
            String decodedUrl = URLDecoder.decode(url, "UTF-8");
            System.out.println("Decoded URL: " + decodedUrl); // 디버그용 로그
            FundDto fundDetail = fundService.getFundByUrl(decodedUrl);

            if (fundDetail == null) {
                System.out.println("Fund data not found for URL: " + decodedUrl);
            } else {
                System.out.println("Fund data retrieved: " + fundDetail.toString());
            }

            return ResponseEntity.ok(fundDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<StockDto>> getActiveStocks() throws IOException {
        return ResponseEntity.ok(stockService.getStock());
    }

    @GetMapping("/active/{stockCode}")
    public ResponseEntity<StockDetailDto> getStockByStockCode(@PathVariable String stockCode) throws IOException {
        return ResponseEntity.ok(stockService.getStockDetail(stockCode));
    }

    @GetMapping("/active/time/{stockCode}")
    public ResponseEntity<StockTimeDto> getStockByStockCodeTime(@PathVariable String stockCode) throws IOException {
        return ResponseEntity.ok(stockService.getStockDetailTime(stockCode));
    }


    @GetMapping("/steadiness")
    public ResponseEntity<List<DepositDto>> getSteadinessDeposits() throws IOException {
        return ResponseEntity.ok(depositService.getDeposit());
    }

    @GetMapping("/steadiness/{prodname}")
    public ResponseEntity<DepositDto> getDepositByProdname(@PathVariable String prodname) throws IOException {
        return ResponseEntity.ok(depositService.getDepositDetail(prodname));
    }

    @GetMapping("/delete")
    public void delete() {
        fundService.invalidateFundCache();
    }

}
