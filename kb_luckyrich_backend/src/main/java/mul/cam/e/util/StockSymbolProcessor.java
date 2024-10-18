package mul.cam.e.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockSymbolProcessor {

    public static String recentStock(String stockSymbol) throws IOException {
        String url = "https://finance.naver.com/item/sise_day.naver?code=" + stockSymbol;
        Document doc = Jsoup.connect(url).get();

        List<String> recentPrice = new ArrayList<>();

        Elements rows = doc.select("table.type2 tr");

        for (Element row : rows) {
            Elements priceElement = row.select("tr td.num span.tah.p11");

            if (!priceElement.isEmpty()) {
                String closingPrice = priceElement.get(0).text();
                recentPrice.add(closingPrice);

                if (recentPrice.size() > 0) break;
            }
        }
        return recentPrice.get(0);

    }

    public static Map<String, List<String>> processStockSymbol(String stockSymbol, int quantity) throws IOException {
        String url = "https://finance.naver.com/item/sise_day.naver?code=" + stockSymbol;
        Document doc = Jsoup.connect(url).get();

        List<String> dates = new ArrayList<>();
        List<String> closingPrices = new ArrayList<>();

        Elements rows = doc.select("table.type2 tr");

        for (Element row : rows) {
            Elements dateElement = row.select("tr td span.tah.p10.gray03");
            Elements priceElement = row.select("tr td.num span.tah.p11");

            if (!dateElement.isEmpty() && !priceElement.isEmpty()) {
                String date = dateElement.text();
                String closingPrice = priceElement.get(0).text();

                dates.add(date);
                closingPrices.add(closingPrice);

                if (dates.size() == 10 && closingPrices.size() == 10) break;
            }
        }

        List<String> q = new ArrayList<>();
        q.add(String.valueOf(quantity));

        Map<String, List<String>> stockData = new HashMap<>();
        stockData.put("Dates", dates);
        stockData.put("ClosingPrices", closingPrices);
        stockData.put("quantity", q);

        return stockData;
    }

    public static Map<String, BigInteger> calculateAssetTrend(List<Map<String, Object>> transaction, Map<String, Map<String, List<String>>> symbol) {
        Map<String, BigInteger> answer = new HashMap<>();

        for (Map<String, Object> trans : transaction) {
            String transDate = (String) trans.get("transaction_date");
            BigDecimal amount = (BigDecimal) trans.get("amount");

            BigInteger stockTotal = BigInteger.ZERO;
            for (String stockSymbol : symbol.keySet()) {
                List<String> dates = symbol.get(stockSymbol).get("Dates");
                List<String> closingPrices = symbol.get(stockSymbol).get("ClosingPrices");
                List<String> quantities = symbol.get(stockSymbol).get("quantity");

                int quantity = Integer.parseInt(quantities.get(0));
                int closingPrice = getLatestClosingPrice(transDate, dates, closingPrices);

                stockTotal = stockTotal.add(BigInteger.valueOf(quantity).multiply(BigInteger.valueOf(closingPrice)));
            }

            answer.put(transDate, stockTotal.add(amount.toBigInteger()));
        }

        return answer;
    }

    private static int getLatestClosingPrice(String transDate, List<String> dates, List<String> closingPrices) {
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).compareTo(transDate) <= 0) return Integer.parseInt(closingPrices.get(i).replace(",", ""));
        }
        return Integer.parseInt(closingPrices.get(0).replace(",", ""));
    }

}
