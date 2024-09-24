package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.service.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return ResponseEntity.ok(fundService.getFundsByRiskRating(2));
    }

    @GetMapping("/neutral")
    public ResponseEntity<List<FundDto>> getNeutralFunds() throws IOException {
        return ResponseEntity.ok(fundService.getFundsByRiskRating(3));
    }

    @GetMapping("/active")
    public ResponseEntity<List<StockDto>> getActiveStocks() throws IOException {
        return ResponseEntity.ok(stockService.getStock());
    }

    @GetMapping("/active/{stockCode}")
    public ResponseEntity<StockDetailDto> getStockByStockCode(@PathVariable String stockCode) throws IOException {
        return ResponseEntity.ok(stockService.getStockDetail(stockCode));
    }

    @GetMapping("/steadiness")
    public ResponseEntity<List<DepositDto>> getSteadinessDeposits() throws IOException {
        return ResponseEntity.ok(depositService.getDeposit());
    }

    @GetMapping("/steadiness/{prodname}")
    public ResponseEntity<DepositDto> getDepositByProdname(@PathVariable String prodname) throws IOException {
        return ResponseEntity.ok(depositService.getDepositDetail(prodname));
    }

}
