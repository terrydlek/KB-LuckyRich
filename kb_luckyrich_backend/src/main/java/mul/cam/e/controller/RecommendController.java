package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.service.FundService;
import mul.cam.e.service.RabbitService;
import mul.cam.e.service.RecommendService;
import mul.cam.e.service.RedisService;
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

    @Autowired
    public RecommendController(RedisService redisService, RecommendService recommendService, RabbitService rabbitService, FundService fundService) {
        this.redisService = redisService;
        this.recommendService = recommendService;
        this.rabbitService = rabbitService;
        this.fundService = fundService;
    }

    @GetMapping("/conservative")
    public ResponseEntity<List<FundDto>> getConservativeFunds() throws IOException {
        return ResponseEntity.ok(fundService.getFundsByRiskRating(2));
    }

    @GetMapping("/neutral")
    public ResponseEntity<List<FundDto>> getNeutralFunds() throws IOException {
        return ResponseEntity.ok(fundService.getFundsByRiskRating(3));
    }
}

//    @GetMapping("test")
//    public String test() throws IOException {
//        return "done";
//    }

    /*

    @GetMapping("/deleteStock")
    public void deleteStock() {
        redisService.invalidateCache("stockList");
        System.out.println("stock list 데이터 삭제");
    }

    @GetMapping("/getStock")
    public ResponseEntity<List<StockDto>> getStock() throws IOException {
        System.out.println("asset getStock execute~~~~");

        String redisKey = "stockList";
        List<StockDto> stockList = redisService.getStockData(redisKey);

        if (stockList == null || stockList.isEmpty()) {
            System.out.println("No data in Redis. API call.");
            stockList = new ArrayList<>();
            String url = "https://finance.naver.com/sise/sise_quant.naver";
            Document doc = Jsoup.connect(url).get();

            Elements rows = doc.select(".box_type_l table.type_2 tr");

            // 각 행에서 데이터 추출
            for (Element row : rows) {
                Elements columns = row.select("td");

                // 데이터가 있는 행만 처리
                if (columns.size() > 1) {
                    // 종목명과 링크 추출
                    Element stockElement = columns.get(1).selectFirst("a");
                    String stockName = stockElement.text();  // 종목명
                    String href = stockElement.attr("href");  // 링크에서 코드 추출
                    String code = href.split("code=")[1];  // 'code=' 이후의 부분 추출

                    String currentPrice = columns.get(2).text();  // 현재가
                    String change = columns.get(3).text();  // 전일비
                    String changeRate = columns.get(4).text();  // 등락률
                    String volume = columns.get(5).text();  // 거래량
                    String marketCap = columns.get(6).text();  // 시가총액
                    String sales = columns.get(7).text();  // 매출액
                    String operatingProfit = columns.get(8).text();  // 영업이익
                    String eps = columns.get(9).text();  // 주당순이익 (EPS)
                    String per = columns.get(10).text();  // PER
                    String roe = columns.get(11).text();  // ROE

                    // StockDto에 데이터 저장 (종목 코드 추가)
                    StockDto stock = new StockDto(stockName, code, currentPrice, change, changeRate, volume, marketCap, sales, operatingProfit, eps, per, roe);
                    stockList.add(stock);
                }
            }
            redisService.setStockData(redisKey, stockList);
        } else {
            System.out.println("Retrieving data from Redis.");
        }

        return ResponseEntity.ok(stockList);
    }

//    @GetMapping("/stock")
//    public String stock() {
//        System.out.println("stock execute~~~");
//        rabbitService.sendStockCrawlRequest();
//        return "Stock crawl request send to RabbitMQ";
//    }

//    @Autowired
//    private StockProducer stockProducer;

    @GetMapping("/getDeposit")
    public ResponseEntity<List<DepositDto>> getDeposits() throws IOException {
        System.out.println("Recommendation getDeposit execute~~~~~~~~~~");

        // Redis에서 먼저 확인하고 없으면 DB에서 가져오기
        String redisKey = "depositList";
        List<DepositDto> depositList = redisService.getDepositData(redisKey);

        if (depositList == null || depositList.isEmpty()) {
            System.out.println("There is no data in Redis. Retrieved from DB.");
            depositList = recommendService.depositList();
            redisService.setData(redisKey, depositList);
        } else System.out.println("Retrieving data from Redis.");

        return ResponseEntity.ok(depositList);
    }

    @GetMapping("/getDeposit/{prodname}")
    public ResponseEntity<DepositDto> getDepositByProdname(@PathVariable("prodname") String prodname) throws IOException {
        return ResponseEntity.ok(recommendService.getDepositByProdname(prodname));
    }

    @GetMapping("/stock/{stockCode}")
    public ResponseEntity<StockDetailDto> getStockByStockCode(@PathVariable("stockCode") String stockCode) throws IOException {
        System.out.println("Fetching stock details for stockCode: " + stockCode);

        // 주식 정보가 있는 URL
        String url = "https://finance.naver.com/item/main.nhn?code=" + stockCode;

        // Jsoup으로 HTML 가져오기
        Document doc = Jsoup.connect(url).get();

        // 주식 데이터 추출
        String stockName = doc.select("div.wrap_company h2 a").first().text();
        String currentPrice = doc.select("p.no_today span.blind").first().text();
        String prevClosingPrice = doc.select("td.first span.blind").get(0).text();
        String openingPrice = doc.select("td.first span.blind").get(1).text();
        String highPrice = doc.select("td span.blind").get(1).text();
        String upperLimitPrice = doc.select("td span.blind").get(2).text();
        String lowPrice = doc.select("td span.blind").get(3).text();
        String lowerLimitPrice = doc.select("td span.blind").get(4).text();
        String tradeVolume = doc.select("td span.blind").get(5).text();
        String tradeValue = doc.select("td span.blind").get(6).text();

        // 차트 이미지 URL (주식 차트 이미지가 표시되는 부분에서 추출)
        String chartImageUrl = doc.select("div.chart img").first().attr("src");

        // StockDetailDto 객체 생성 및 데이터 저장
        StockDetailDto stockDetail = new StockDetailDto();
        stockDetail.setStockName(stockName);
        stockDetail.setCurrentPrice(currentPrice);
        stockDetail.setPrevClosingPrice(prevClosingPrice);
        stockDetail.setOpeningPrice(openingPrice);
        stockDetail.setHighPrice(highPrice);
        stockDetail.setUpperLimitPrice(upperLimitPrice);
        stockDetail.setLowPrice(lowPrice);
        stockDetail.setLowerLimitPrice(lowerLimitPrice);
        stockDetail.setTradeVolume(tradeVolume);
        stockDetail.setTradeValue(tradeValue);
        stockDetail.setChartImageUrl("https://finance.naver.com" + chartImageUrl);

        // ResponseEntity로 반환
        return ResponseEntity.ok(stockDetail);
    }



    @GetMapping("/stock/{stockCode}/compare")
    public ResponseEntity<StockCompareDto> getStockComparison(@PathVariable("stockCode") String stockCode) throws IOException {
        String url = "https://finance.naver.com/item/main.naver?code=" + stockCode;
        Document doc = Jsoup.connect(url).get();

        StockCompareDto compareData = new StockCompareDto();

        // 종목명 추출
        Elements stockNamesElements = doc.select(".section.trade_compare table thead th[scope=col] a");
        List<String> stockNames = new ArrayList<>();
        for (Element element : stockNamesElements) {
            stockNames.add(element.text());
        }
        compareData.setStockNames(stockNames);

        // 각 데이터 항목 추출 (현재가, 전일대비, 등락률, 시가총액 등)
        Elements rows = doc.select(".section.trade_compare table tbody tr");

        // 현재가 추출
        compareData.setCurrentPrices(rows.get(0).select("td").eachText());

        // 전일대비 추출
        compareData.setPriceChanges(rows.get(1).select("td").eachText());

        // 등락률 추출
        compareData.setFluctuationRates(rows.get(2).select("td").eachText());

        // 시가총액 추출
        compareData.setMarketCaps(rows.get(3).select("td").eachText());

        // 외국인 비율 추출
        compareData.setForeignOwnerships(rows.get(4).select("td").eachText());

        // 매출액 추출
        compareData.setSales(rows.get(5).select("td").eachText());

        // 영업이익 추출
        compareData.setOperatingProfits(rows.get(6).select("td").eachText());

        // 당기순이익 추출
        compareData.setNetProfits(rows.get(9).select("td").eachText());

        // 주당순이익(EPS) 추출
        compareData.setEps(rows.get(10).select("td").eachText());

        // ROE 추출
        compareData.setRoes(rows.get(11).select("td").eachText());

        // PER 추출
        compareData.setPers(rows.get(12).select("td").eachText());

        // PBR 추출
        compareData.setPbrs(rows.get(13).select("td").eachText());

        // ResponseEntity로 반환
        return ResponseEntity.ok(compareData);
    }

}
     */
