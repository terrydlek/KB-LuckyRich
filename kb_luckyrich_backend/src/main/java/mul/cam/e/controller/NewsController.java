package mul.cam.e.controller;

import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.NewsDetailDto;
import mul.cam.e.dto.NewsDto;
import mul.cam.e.service.NewsService;
import mul.cam.e.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getnews/{code1}/{code2}")
    public ResponseEntity<NewsDetailDto> getNewsDetail(@PathVariable String code1, @PathVariable String code2) throws IOException {
        String fullCode = code1 + "/" + code2;
        return ResponseEntity.ok(newsService.getNewsDetails(fullCode));
    }
    @GetMapping("/getnewsestate")
    public ResponseEntity<List<NewsDto>> getEstateNews() throws IOException {
        return ResponseEntity.ok(newsService.getEstateNews());
    }
    @GetMapping("/getnewssecurities")
    public ResponseEntity<List<NewsDto>> getsecuritiesNews() throws IOException {
        return ResponseEntity.ok(newsService.getsecuritiesNews());
    }
    @GetMapping("/getnewspersonal")
    public ResponseEntity<List<NewsDto>> getpersonalNews() throws IOException {
        return ResponseEntity.ok(newsService.getpersonalNews());
    }
}
