package mul.cam.e.rabbit;

import mul.cam.e.dto.StockDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockCrawlListener {

    @RabbitListener(queues = "stock-queue")
    public void receiveMessage(String message) throws IOException {
        if ("Start stock crawling".equals(message)) {
            crawlStockData();
        }
    }

    public void crawlStockData() throws IOException {
        System.out.println("Crawling stock data...");

        List<StockDto> stockList = new ArrayList<>();
        String url = "https://finance.naver.com/sise/sise_quant.naver";
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select(".box_type_l table.type_2 tr");

        for (Element row : rows) {
            Elements columns = row.select("td");
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
        // 데이터 저장 로직 추가
    }
}
