package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.service.*;
import mul.cam.e.util.KeyDecrypt;
import mul.cam.e.util.KeyEncrypt;
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

@CrossOrigin(origins = "http://localhost:5173")
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
    private final KeyEncrypt keyEncrypt;
    private final KeyDecrypt keyDecrypt;

    @Autowired
    public RecommendController(RedisService redisService, RecommendService recommendService, RabbitService rabbitService, FundService fundService, StockService stockService, DepositService depositService, KeyEncrypt keyEncrypt, KeyDecrypt keyDecrypt) {
        this.redisService = redisService;
        this.recommendService = recommendService;
        this.rabbitService = rabbitService;
        this.fundService = fundService;
        this.stockService = stockService;
        this.depositService = depositService;
        this.keyEncrypt = keyEncrypt;
        this.keyDecrypt = keyDecrypt;
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
    public String getFundByUrl(@PathVariable String url) throws IOException {
        System.out.println("controller get fund by url execute~~~");

        String completeUrl = "https://www.investing.com/funds/" + url;
        Document doc = Jsoup.connect(completeUrl).get();

        doc.select("header, footer").remove();

        Elements links = doc.select("a");
        for (Element link : links) {
            link.attr("href", "javascript:void(0);");
        }

        doc.select(".chartWrap").remove();
        doc.select("#rightColumn").remove();

        return doc.outerHtml();
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

    @GetMapping("/cipher")
    public String cipher() {
        String text = "hello";
        System.out.println("text : " + text);
        String ci = keyEncrypt.encrypt(text);
        System.out.println("cipher : " + ci);
        String di = keyDecrypt.decrypt(ci);
        System.out.println("di : " + di);
        return "dd";
    }

}
