package mul.cam.e.controller;

import mul.cam.e.dto.*;
import mul.cam.e.jwt.JwtTokenProvider;
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

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final JwtTokenProvider jwtTokenProvider;

    private final ApiService apiService;
    private final UserService userService;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kako.oauth.url}") String KakaoUrl;

    public ApiController(ApiService service, TokenDecoder decoder, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.apiService = service;
        this.jwtTokenProvider = jwtTokenProvider;
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

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(userInf.getEmail());

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
        System.out.println("getNaverLoginUrl");
        String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
                + NaverClientId +"&state=STATE_STRING&redirect_uri=" + NaverUrl;
        return url;
    }

    @GetMapping("login/naver")
    public ResponseEntity<Map<String, Object>> naverUserCode(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state) {
    //public Map<String, Object> naverUserCode(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state) {

        NaverResponseDto res_body = apiService.getNaverToken(code, state);
        // 실제 API 서비스 호출로 교체
        String accessToken = res_body.getAccess_token();
        //System.out.println("access" + accessToken);

        NaverUserInfDto userInfo = apiService.getNaverUserInfo(accessToken);

        Map<String, Object> map = new HashMap<>();
        map.put("id", userInfo.getId());
        map.put("name", userInfo.getName());
        map.put("email", userInfo.getEmail());
        map.put("gender", userInfo.getGender());
        map.put("accessToken", accessToken);

        System.out.println("naver info" + map);

//        String redirectUrl = "http://localhost:5173/";
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(map, HttpStatus.OK);  // JSON 응답을 반환합니다.

        //return map;
    }

    // 로그아웃
    @CrossOrigin(origins = "http://localhost:5173")  // 프론트엔드 주소
    @PostMapping("/logout")
    public ResponseEntity<String> naverUserCode(@RequestParam("token") String accessToken, HttpSession session) throws IOException {
        System.out.println("네이버 로그아웃 중...");

        // 네이버 토큰 삭제 API 호출
        String deleteTokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=" + NaverClientId
                + "&client_secret=" + NaverClientSecret + "&access_token=" + accessToken + "&service_provider=NAVER";

        System.out.println("토큰 삭제 URL: " + deleteTokenUrl);

        try {
            String response = requestToServer(deleteTokenUrl);
            System.out.println("토큰 삭제 응답: " + response);

            // 세션 무효화 (사용자 로그아웃 처리)
            session.invalidate();

            return ResponseEntity.ok("로그아웃 성공");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 실패");
        }
    }

    // 서버에 API 요청을 보내는 메소드
    private String requestToServer(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        BufferedReader br;

        if (responseCode == 200) { // 정상 응답
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {  // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        return responseCode == 200 ? response.toString() : null;
    }
}


