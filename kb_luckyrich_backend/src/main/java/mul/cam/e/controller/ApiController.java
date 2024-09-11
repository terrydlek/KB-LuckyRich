package mul.cam.e.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import mul.cam.e.dto.GoogleResponseDto;
import mul.cam.e.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ApiService service;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    public ApiController(ApiService service) {
        this.service = service;
    }

    @PostMapping("google")
    public String getLoginUrl(){
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
                clientId + "&redirect_uri=" + redirectUrl + "&response_type=code&scope=email";
        return url;
    }

    @GetMapping("login/google")
    public ResponseEntity<Void> getUserCode(@RequestParam("code") String code){
        GoogleResponseDto res_body = service.getAccessToken(code);
//        System.out.println(res_body.getId_token());

        //TODO
        //디코딩 후 database 유저 확인후 가입 or 로그인 처리

        // Vue Home URL
        String redirectUrl = "http://localhost:5173/";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
