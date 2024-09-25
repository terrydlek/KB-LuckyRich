package mul.cam.e.service;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.StockDetailDto;
import mul.cam.e.dto.StockDto;
import mul.cam.e.dto.StockTimeDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class StockService {
    private final RedisService redisService;

    public StockService(RedisService redisService) {
        this.redisService = redisService;
    }

    public List<StockDto> getStock() throws IOException {
        System.out.println("StockService getStock execute~~~~");

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
            redisService.setData(redisKey, stockList, 5);
        } else {
            System.out.println("Retrieving data from Redis.");
        }

        return stockList;
    }

    public StockTimeDto getStockDetailTime(String stockCode) throws IOException {
        System.out.println("StockService getStockDetailTime execute~~~~");

        String url = "https://finance.naver.com/item/sise.naver?code=" + stockCode + "&asktype=10";

        Document doc = Jsoup.connect(url).get();

        StockTimeDto stockTimeDto = new StockTimeDto();

        stockTimeDto.setNowVal(doc.select("#_nowVal").text());
        stockTimeDto.setDiff(doc.select("#_diff span").text());
        stockTimeDto.setRate(doc.select("#_rate span").text());
        stockTimeDto.setQuant(doc.select("#_quant").text());
        stockTimeDto.setAmount(doc.select("#_amount").text());
        stockTimeDto.setFaceVal(doc.select(".section.inner_sub th:contains(액면가)").next().text());
        stockTimeDto.setUpperLimit(doc.select(".section.inner_sub th:contains(상한가)").next().text());
        stockTimeDto.setLowerLimit(doc.select(".section.inner_sub th:contains(하한가)").next().text());
        stockTimeDto.setPer(doc.select("#_sise_per").text());
        stockTimeDto.setEps(doc.select("#_sise_eps").text());
        stockTimeDto.setHigh52Week(doc.select(".section.inner_sub th:contains(52주 최고)").next().text());
        stockTimeDto.setLow52Week(doc.select(".section.inner_sub th:contains(52주 최저)").next().text());
        stockTimeDto.setMarketCap(doc.select("#_sise_market_sum").text());
        stockTimeDto.setForeignShares(doc.select(".section.inner_sub th:contains(외국인현재)").next().text());
        stockTimeDto.setSellingPrice(doc.select(".type2 tbody tr:nth-child(3) td:nth-child(2)").text());
        stockTimeDto.setBuyingPrice(doc.select(".type2 tbody tr:nth-child(3) td:nth-child(4)").text());
        stockTimeDto.setPreviousPrice(doc.select(".section.inner_sub th:contains(전일가)").next().text());
        stockTimeDto.setOpeningPrice(doc.select(".section.inner_sub th:contains(시가)").next().text().split(" ")[0]);
        stockTimeDto.setHighPrice(doc.select("td:contains(고가)").next().text().split(" ")[1]);
        stockTimeDto.setLowPrice(doc.select("td:contains(저가)").next().text().split(" ")[1]);
        // 이까지 잘 됌
        stockTimeDto.setPreviousUpperLimit(doc.select(".section.inner_sub th:contains(전일상한)").next().text());
        stockTimeDto.setPreviousLowerLimit(doc.select(".section.inner_sub th:contains(전일하한)").next().text());
        stockTimeDto.setListedShares(doc.select(".section.inner_sub th:contains(상장주식수)").next().text());
        stockTimeDto.setCapital(doc.select(".section.inner_sub th:contains(자본금)").next().text());

        return stockTimeDto;
    }


    public StockDetailDto getStockDetail(String stockCode) throws IOException {
        System.out.println("StockService getStockDetail execute~~~~");

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
        stockDetail.setChartImageUrl(chartImageUrl);

        return stockDetail;
    }
}
