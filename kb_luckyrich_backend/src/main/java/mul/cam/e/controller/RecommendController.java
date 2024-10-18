package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.gpt.GPTRequest;
import mul.cam.e.gpt.GPTResponse;
import mul.cam.e.service.*;
import mul.cam.e.util.KeyDecrypt;
import mul.cam.e.util.KeyEncrypt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

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

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public RecommendController(RedisService redisService, RecommendService recommendService, RabbitService rabbitService, FundService fundService, StockService stockService, DepositService depositService, KeyEncrypt keyEncrypt, KeyDecrypt keyDecrypt, RestTemplate restTemplate) {
        this.redisService = redisService;
        this.recommendService = recommendService;
        this.rabbitService = rabbitService;
        this.fundService = fundService;
        this.stockService = stockService;
        this.depositService = depositService;
        this.keyEncrypt = keyEncrypt;
        this.keyDecrypt = keyDecrypt;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/funds")
    public ResponseEntity<List<FundDto>> getFunds(@RequestParam(required = false) Integer riskRating) throws IOException {
        if (riskRating == null) {
            log.info("Risk rating is null");
            return null;
        }
        return ResponseEntity.ok(fundService.getFundsByRiskRating(riskRating));
    }

    @GetMapping("/conservative")
    public ResponseEntity<List<FundDto>> getConservativeFunds() throws IOException {
//        redisService.invalidateCache("riskRating: 2");
//        redisService.invalidateCache("riskRating: 3");
        return ResponseEntity.ok(fundService.getFundsByRiskRating(2));
    }

    @GetMapping("/neutral")
    public ResponseEntity<List<FundDto>> getNeutralFunds() throws IOException {
        return ResponseEntity.ok(fundService.getFundsByRiskRating(3));
    }

    @GetMapping("/funds/{url}")
    public String getFundByUrl(@PathVariable String url) throws IOException {
        log.info("get fund by url execute~~~");

        String completeUrl = "https://www.investing.com/funds/" + url;
        Document doc = Jsoup.connect(completeUrl).get();

        // Remove header and footer
        doc.select("header, footer").remove();
        doc.select("#Billboard_Default").remove();
        doc.select(".midHeader").remove();

        // Remove specific words
        removeElementsWithWords(doc, "Watchlist", "Create Alert");

        // Remove the sentiment box
        doc.select(".instrumentsSentiments").remove();

        // Remove the navigation tabs and sub-tabs
        doc.select("ul.newBigTabs, ul.newBigSubTabs").remove();

        // Remove the 'i' icon next to the title
        doc.select(".instrumentHeader .infoBox").remove();

        // Remove the chart and its controls
        doc.select("#js_instrument_chart_wrapper").remove();

        // Remove unnecessary elements
        doc.select(".chartWrap, #rightColumn").remove();
        doc.select("div[data-slot-id]").remove();

        // Remove CSS classes associated with the removed elements
        doc.select("header").removeClass("topBar");
        doc.select("footer").removeClass("footer-class");

        // Improve the layout of the overview table
        Element overviewTable = doc.select(".overviewDataTable").first();
        if (overviewTable != null) {
            overviewTable.addClass("improved-layout");
        }

        // Add custom CSS to disable interactions and improve layout
        Element head = doc.head();
        head.appendElement("style").text(
                "body, html { margin: 0; padding: 0; }" +  // 여백 및 패딩 제거
                        "a, button, input[type=submit], input[type=button] { cursor: default !important; }" +
                        ".improved-layout { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; }" +
                        ".improved-layout > div { margin-bottom: 10px; }" +
                        ".float_lang_base_1 { font-weight: bold; display: block; margin-bottom: 5px; }" +
                        ".float_lang_base_2 { display: block; }" +
                        ".instrumentHeader h1 { font-size: 24px; margin-bottom: 20px; }" +
                        "body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                        ".wrapper { " + // .wrapper 스타일 추가
                        "width: 1000px; " +
                        "position: relative; " +
                        "padding: 0px 100px 0; " +
                        "margin: 0 auto; " +
                        "}"
        );

        // Remove all script tags to prevent any JavaScript execution
        doc.select("script").remove();

//        System.out.println(doc.outerHtml());

        return doc.outerHtml();
    }



    private void removeElementsWithWords(Document doc, String... words) {
        for (String word : words) {
            doc.select(":containsOwn(" + word + ")").remove();
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

    // 나중에 플젝 끝날 때쯤 지우면 됨.
//    @GetMapping("/delete")
//    public void delete() {
//        redisService.invalidateCache("depositList");
//    }

}
