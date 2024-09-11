package mul.cam.e.controller;

import mul.cam.e.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ApiService service;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kako.oauth.url}") String KakaoUrl;

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
    public String getUserCode(@RequestParam("code") String code){
        String res_body = service.getAccessToken(code);
        System.out.println(res_body);

        //TODO
        //디코딩 후 database 유저 확인후 가입 or 로그인 처리

        return "";
    }

    @PostMapping("kakao")
    public String getKakaoLoginUrl(){
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + KakaoClientId + "&redirect_uri=" + KakaoUrl;
        return url;
    }

    @GetMapping("login/kakao")
    public String getKakaoUserCode(@RequestParam("code") String code){
        String res_body = service.getKakaoToken(code);
        System.out.println("asddddddddd"+res_body);

        return "";
    }
}
