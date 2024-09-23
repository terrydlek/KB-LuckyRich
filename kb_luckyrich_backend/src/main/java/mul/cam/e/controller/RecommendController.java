package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.DepositDto;
import mul.cam.e.dto.FundDto;
import mul.cam.e.dto.StockDto;
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
import java.util.List;

@RestController
@Log4j
@RequestMapping("/asset")
public class RecommendController {

    private final RedisService redisService;
    private final RecommendService recommendService;
    private final RabbitService rabbitService;

    @Autowired
    public RecommendController(RedisService redisService, RecommendService recommendService, RabbitService rabbitService) {
        this.redisService = redisService;
        this.recommendService = recommendService;
        this.rabbitService = rabbitService;
    }

    @GetMapping("conservativeList")
    public ResponseEntity<List<FundDto>> conservativeList() {
        return ResponseEntity.ok(recommendService.conservativeList());
    }

    @GetMapping("test")
    public String test() throws IOException {
        return "done";
    }

    @GetMapping("/neutrilityList")
    public ResponseEntity<List<FundDto>> neutrilityList() {
        return ResponseEntity.ok(recommendService.neutrilityList());
    }

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
            System.out.println(1);
            // 각 행에서 데이터 추출
            for (Element row : rows) {
                Elements columns = row.select("td");
                // 데이터가 있는 행만 처리
                if (columns.size() > 1) {
                    String stockName = columns.get(1).text();  // 종목명
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

                    // StockDto에 데이터 저장
                    StockDto stock = new StockDto(stockName, currentPrice, change, changeRate, volume, marketCap, sales, operatingProfit, eps, per, roe);
                    stockList.add(stock);
                }
            }
            redisService.setStockData(redisKey, stockList);
        } else System.out.println("Retrieving data from Redis.");

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

//    @GetMapping("/getStock")
//    public ResponseEntity<List<StockDto>> getStock() throws IOException {
//        System.out.println("asset getStock execute~~~~");
//
//        List<StockDto> stockList = new ArrayList<>();
//
//        String url = "https://finance.naver.com/sise/sise_quant.naver";
//        Document doc = Jsoup.connect(url).get();
//
//        Elements rows = doc.select(".box_type_l table.type_2 tr");
//
//        for (Element row : rows) {
//            Elements columns = row.select("td");
//            if (columns.size() > 1) {
//                String stockName = columns.get(1).text();
//                String currentPrice = columns.get(2).text();
//                String change = columns.get(3).text();
//                String changeRate = columns.get(4).text();
//                String volume = columns.get(5).text();
//                String marketCap = columns.get(6).text();
//                String sales = columns.get(7).text();
//                String operatingProfit = columns.get(8).text();
//                String eps = columns.get(9).text();
//                String per = columns.get(10).text();
//                String roe = columns.get(11).text();
//
//                StockDto stock = new StockDto(stockName, currentPrice, change, changeRate, volume, marketCap, sales, operatingProfit, eps, per, roe);
//                stockList.add(stock);
//            }
//        }
//
//        return ResponseEntity.ok(stockList);  // 클라이언트에게 요청이 성공했음을 알려줌
//    }

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

}
