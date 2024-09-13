package mul.cam.e.controller;

import mul.cam.e.dto.StockDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/asset")
public class StockController {

//    <!--종목 -->
//			<div class="box_type_l">
//				<div class="tab_style_1">
//
//
//
//
//					<div class="tab_smeun choice"><div class="choice_lt"></div>코스피</div>
//					<div class="tab_smeun"><a href="?sosok=1">코스닥</a></div>
//
//
//				</div>
//				<table cellpadding="0"cellspacing="0"class="type_2">
//				<col width="4%">
//				<col width="*">
//				<col width="7%">
//				<col width="7%">
//				<col width="7%">
//
//					<col width="9%">
//
//					<col width="9%">
//
//					<col width="9%">
//
//					<col width="9%">
//
//					<col width="9%">
//
//					<col width="9%">
//
//					<col width="9%">
//
//				<tr>
//					<th>N</th>
//					<th>종목명</th>
//					<th>현재가</th>
//					<th class="tr"style="padding-right:8px">전일비</th>
//					<th>등락률</th>
//					<th>거래량</th>
//					<th>시가총액</th>
//					<th>매출액</th>
//					<th>영업이익</th>
//					<th>주당순이익</th>
//					<th>PER</th>
//					<th>ROE</th>
//				</tr>
//				<tr><td colspan="10"class="blank_08"></td></tr>
//				<tr>
//					<td class="no">1</td>
//					<td><a href="/item/main.naver?code=252670"class="tltle">KODEX 200선물인버스2X</a></td>
//					<td class="number">2,270</td>
//					<td class="number">
//				<em class="bu_p bu_pdn"><span class="blind">하락</span></em><span class="tah p11 nv01">
//            110
//				</span>
//			</td>
//					<td class="number">
//				<span class="tah p11 nv01">
//            -4.62%
//				</span>
//			</td>
//									<td class="number">197,393,080</td>
//									<td class="number">13,556</td>
//					<td class="number">N/A</td>
//					<td class="number">N/A</td>
//					<td class="number">N/A</td>
//					<td class="number">N/A</td>
//					<td class="number">N/A</td>
//				</tr>

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

}
