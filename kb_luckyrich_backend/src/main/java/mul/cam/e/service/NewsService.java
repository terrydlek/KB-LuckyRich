package mul.cam.e.service;

import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dto.NewsDetailDto;
import mul.cam.e.dto.NewsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class NewsService {

    private final RedisService redisService;

    public NewsService(RedisService redisService) {
        this.redisService = redisService;
    }

    public List<NewsDto> getNews() throws IOException {
        log.info("NewsService getNews executed");

        String redisKey = "news";
        List<NewsDto> newsList = redisService.getNewsData(redisKey);

        if (newsList == null || newsList.isEmpty()) {
            log.info("No data in Redis. API call.");
            newsList = new ArrayList<>();
            String url = "https://news.naver.com/breakingnews/section/101/259";
            Document doc = Jsoup.connect(url).get();

            // 뉴스 리스트를 가져오기 위한 selector 설정 (각 뉴스 항목 li를 선택)
            Elements newsElements = doc.select(".section_article._TEMPLATE .sa_list .sa_item._LAZY_LOADING_WRAP .sa_item_inner .sa_item_flex");

            for (Element newsElement : newsElements) {
                String imageUrl = newsElement.select("img").attr("data-src");
                String title = newsElement.select("strong.sa_text_strong").text();
                String newsUrl = newsElement.select("a.sa_text_title").attr("href");
                String code = "";
                if (newsUrl.contains("article/")) {
                    code = newsUrl.split("article/")[1]; // 'article/' 뒤의 부분을 추출
                }

                String description = newsElement.select("div.sa_text_lede").text();

                NewsDto newsDto = new NewsDto();
                newsDto.setImageUrl(imageUrl);
                newsDto.setTitle(title);
                newsDto.setDescription(description);
                newsDto.setUrl(newsUrl);
                newsDto.setCode(code);

                newsList.add(newsDto);

            }
            redisService.setData(redisKey, newsList, 5);
        }
        return newsList;
    }

    public NewsDetailDto getNewsDetails(String code) throws IOException {
        log.info("NewsService getNewsDetail executed");

        String redisKey = code + "newsDetail";

        NewsDetailDto newsDetail = redisService.getNewsDetailData(redisKey);

        if (newsDetail == null) {
            log.info("No data in Redis. API call.");
            newsDetail = new NewsDetailDto();

            String url = "https://n.news.naver.com/mnews/article/" + code;
            Document doc = Jsoup.connect(url).get();

            String newsTitle =doc.select(".media_end_head_title .media_end_head_headline span").text();
            String dateTime = doc.select(".media_end_head_info_datestamp_time._ARTICLE_DATE_TIME").attr("data-date-time");
            String imageUrl = doc.select("#img1").attr("data-src");
            String articleText = doc.select("article#dic_area").text();
            String imageDescription = doc.select("em.img_desc").text();

            log.info("News Title: " + newsTitle);
            log.info("News Title: " + dateTime);
            log.info("Image URL: " + imageUrl);
            log.info("Article Text: " + articleText);

            newsDetail.setTitle(newsTitle);
            newsDetail.setImageUrl(imageUrl);
            newsDetail.setArticleText(articleText);
            newsDetail.setDateTime(dateTime);
            newsDetail.setImageDescription(imageDescription);

            redisService.setData(redisKey, newsDetail, 10);

        }
        return newsDetail;

    }

}
