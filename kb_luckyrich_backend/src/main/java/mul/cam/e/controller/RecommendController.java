package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.DepositDto;
import mul.cam.e.dto.FundDto;
import mul.cam.e.dto.StockDto;
import mul.cam.e.service.RecommendService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j
@RequestMapping("/asset")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
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

    @GetMapping("/getStock")
    public List<StockDto> getStock() throws IOException {

        String url = "https://finance.naver.com/sise/sise_quant.naver";

        Document doc = Jsoup.connect(url).get();

        List<StockDto> stockList = new ArrayList<>();

        Elements rows = doc.select(".box_type_l table.type_2 tr");

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
        System.out.println(stockList);

        // 추출한 주식 데이터 리스트 반환
        return stockList;
    }


    @GetMapping("/getDeposit")
    public ResponseEntity<List<DepositDto>> getDeposits() throws IOException {
        System.out.println("Recommendation getDeposit execute~~~~~~~~~~");

        System.out.println(recommendService.depositList());
        return ResponseEntity.ok(recommendService.depositList());
    }


}
