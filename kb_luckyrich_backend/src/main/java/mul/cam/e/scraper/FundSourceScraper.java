package mul.cam.e.scraper;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.FundDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

// investing.com 웹 사이트에서 펀드 데이터 스크래핑 하는 클래스
@Service
@Log4j
public class FundSourceScraper {

    // Investing.com의 한국 펀드 목록 페이지 기본 URL (위험도 엔드포인트 empty)
    private static final String BASE_URL = "https://www.investing.com/funds/south-korea-funds?&issuer_filter=0&riskRating=";

    /*
    @return 스크래핑된 펀드 데이터 리스트
    @throws IOException 네트워크 오류 발생 시
     */
    public List<FundDto> scrapeFunds() throws IOException {
        List<FundDto> funds = new ArrayList<>();
        // 위험도 2(안정추구형), 3(위험중립형)에 대해 스크래핑
        funds.addAll(scrapeFundsByRiskRating(2));
        funds.addAll(scrapeFundsByRiskRating(3));
        return funds;
    }

    /*
    특정 위험도의 펀드 데이터 스크래핑
    @return 지정된 위험도의 펀드 데이터 리스트
    */
    private List<FundDto> scrapeFundsByRiskRating(int riskRating) throws IOException {
        String url = BASE_URL + riskRating;

        // Jsoup 사용해서 url 연결 -> 브라우저 설정 -> 설정된 html 문서 가져오기
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get();

        List<FundDto> funds = new ArrayList<>();

        Element table = doc.select("table#etfs").first();
        if (table != null) {
            Elements rows = table.select("tr");
            for (int i = 1; i < rows.size(); i++) { // 첫 번째 행은 헤더이므로 건너뜁니다
                Element row = rows.get(i);
                Elements columns = row.select("td");

                if (columns.size() >= 7) {
                    FundDto fund = new FundDto();
                    fund.setName(columns.get(1).text());
                    fund.setSymbol(columns.get(2).text());
                    fund.setLastPrice(columns.get(3).text());
                    fund.setChangePercent(columns.get(4).text());
                    fund.setTotalAssets(columns.get(5).text());
                    fund.setLastUpdate(columns.get(6).text());
                    fund.setRiskRating(riskRating);

                    // 국가, 발행사, 자산 클래스 정보는 이 테이블에 없으므로 별도로 처리해야 할 수 있습니다
                    fund.setCountry("South Korea"); // 예시

                    funds.add(fund);
                }
            }
        } else {
            System.out.println("Table not found");
        }

        return funds;
    }

    // 추출된 JSON 형식의 펀드 데이터를 파싱하여 FundDto 객체로 변환
    // @param json 형식의 펀드 데이터 문자열, 위험 등급
    // @return 파싱된 FundDto 객체들의 리스트
    private List<FundDto> parseFundData(String jsonData, int riskRating) {
        List<FundDto> funds = new ArrayList<>();
        String[] fundDataArray = jsonData.split("\\],\\["); // 정규표현식
        System.out.println(fundDataArray.length + fundDataArray[0]);

        for (String fundData : fundDataArray) {
            String[] fields = fundData.replaceAll("[\\[\\]\"]", "").split(","); // 대괄호 없애고 콤마로만 구분


            if (fields.length >= 10) {
                FundDto fund = new FundDto();
                fund.setSymbol(fields[0]); // 심볼
                fund.setName(fields[1]);   // 펀드 이름
                fund.setLastPrice(fields[2]); // 마지막 가격 (문자열로 유지)
                fund.setChangePercent(fields[3]); // 변동률
                fund.setTotalAssets(fields[4]); // 총 자산
                fund.setLastUpdate(fields[5]); // 마지막 업데이트 시간
                fund.setCountry(fields[6]); // 국가
                fund.setIssuer(fields[7]); // 발행자
                fund.setRiskRating(riskRating); // 위험 등급
                fund.setAssetClass(fields[8]); // 자산 클래스
                funds.add(fund);
            }
        }

        return funds;
    }

//    // 문자열을 double 값으로 파싱
//    // 파싱 실패 시 0.0 반환
//    private double parseDouble(String value) {
//        try {
//            return Double.parseDouble(value.replace(",", ""));
//        } catch (NumberFormatException e) {
//            return 0.0; // 파싱 실패 시 기본값 반환
//        }
//    }
}
