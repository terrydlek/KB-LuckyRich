package mul.cam.e.controller;

import mul.cam.e.dto.GoogleResponseDto;
import mul.cam.e.dto.GoogleUserInfDto;
import mul.cam.e.dto.NaverUserInfDto;
import mul.cam.e.dto.UserDto;
import mul.cam.e.service.ApiService;
import mul.cam.e.service.UserService;
import mul.cam.e.util.TokenDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ApiService apiService;
    private final UserService userService;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kako.oauth.url}") String KakaoUrl;

    public ApiController(ApiService service, TokenDecoder decoder, UserService userService) {
        this.apiService = service;
        this.userService = userService;
    }

    @PostMapping("google")
    public String getLoginUrl(){
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code" +
                "&scope=openid%20email%20profile";
        return url;
    }

    @GetMapping("login/google")
    public ResponseEntity<Void> getUserCode(@RequestParam("code") String code){
        GoogleResponseDto res_body = apiService.getAccessToken(code);

        // Decode Id_Token
        GoogleUserInfDto userInf = apiService.decodeIdToken(res_body.getId_token());

        UserDto user = new UserDto(userInf.getFamily_name()+userInf.getGiven_name(),
                userInf.getEmail(), null, 0);

        // Access_Token을 이용한 방법
        // id, email, verified_email, name, given_name, family_name, picture, locale 반환
        // apiService.getGoogleUserInf(res_body.getAccess_token());

        // Vue Home URL
        String redirectUrl = "http://localhost:5173/";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        if(userInf == null){
            // google에서 받은 유저정보가 없을시
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        if(userService.userCheck(userInf.getEmail()) == 0){
            userService.register(user);
            System.out.println("register success");
        }
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("kakao")
    public String getKakaoLoginUrl(){
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + KakaoClientId + "&redirect_uri=" + KakaoUrl;
        return url;
    }

    @GetMapping("login/kakao")
    public ResponseEntity<Void> getKakaoUserCode(@RequestParam("code") String code){
        String res_body = apiService.getKakaoToken(code);
        System.out.println("asddddddddd"+res_body);

        String redirectUrl = "http://localhost:5173/";

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @Value("${naver.oauth.client-id}") String NaverClientId;
    @Value("${naver.oauth.url}") String NaverUrl;
    @Value("${naver.oauth.client-secret}") String NaverClientSecret;
    @PostMapping("naver")

    public String getNaverLoginUrl(){
        String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
                + NaverClientId +"&state=STATE_STRING&redirect_uri=" + NaverUrl;
        return url;
    }

    @GetMapping("login/naver")
    public ResponseEntity<Map<String, Object>> naverUserCode(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state) {
        Map<String, Object> map = new HashMap<>();

        // 실제 API 서비스 호출로 교체
        String accessToken = apiService.getNaverToken(code, state);
        //System.out.println("accessToken = " + accessToken);

        NaverUserInfDto userInfo = apiService.getNaverUserInfo(accessToken);

        map.put("id", userInfo.getId());
        map.put("name", userInfo.getName());
        map.put("email", userInfo.getEmail());
        map.put("gender", userInfo.getGender());
        map.put("accessToken", accessToken);

        System.out.println("naver info" + map);

        String redirectUrl = "http://localhost:5173/";


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}


