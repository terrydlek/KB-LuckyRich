package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.NewsDto;
import mul.cam.e.service.NewsService;
import mul.cam.e.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@Log4j
@RequestMapping("/news")
public class NewsController {
    private final RedisService redisService;
    private final NewsService newsService;

    public NewsController(RedisService redisService, NewsService newsService) {
        this.redisService = redisService;
        this.newsService = newsService;
    }

    @GetMapping("/getnews")
    public ResponseEntity<List<NewsDto>> getAllNews() throws IOException {
        return ResponseEntity.ok(newsService.getNews());
    }

}
