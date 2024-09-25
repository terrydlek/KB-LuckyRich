package mul.cam.e.controller;

import mul.cam.e.dto.*;
import mul.cam.e.jwt.JwtTokenProvider;
import mul.cam.e.security.SecurityUser;
import mul.cam.e.service.ApiService;
import mul.cam.e.service.UserService;
import mul.cam.e.util.TokenDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ApiService apiService;
    private final UserService userService;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kakao.oauth.url}") String KakaoUrl;

    public ApiController(ApiService service, TokenDecoder decoder, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.apiService = service;
        this.userService = userService;
    }

    @GetMapping("/google")
    public String getLoginUrl(){
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code" +
                "&scope=openid%20email%20profile";
        return url;
    }

    @GetMapping("/login/google")
    public void getUserCode(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
        GoogleResponseDto res_body = apiService.getAccessToken(code);

        // Decode Id_Token
        GoogleUserInfDto userInf = TokenDecoder.decodeIdToken(res_body.getId_token());

        int provider_id = 2;
        // 유저 조회
        SecurityUser customUserDetail = userService.loadUserByUsername(userInf.getEmail());

        if(customUserDetail == null) {
            SecurityUser user = new SecurityUser(userInf.getFamily_name()+userInf.getGiven_name(),
                    userInf.getEmail(), null, 0, provider_id);
            userService.register(user);

            customUserDetail = userService.loadUserByUsername(userInf.getEmail());
        }

        System.out.println(customUserDetail.getEmail());

        // 인증 처리 왜 안됨????????
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDetail, null));
//        System.out.println("authenticated");

        // JWT 토큰 생성
        String jwtToken = JwtTokenProvider.createToken(customUserDetail.getEmail());

        // 계좌 갯수 확인
        int account_num = userService.getAccountNum(customUserDetail.getEmail());

        // Vue Login창으로 Redirect
        String redirectUrl = "http://localhost:5173/login?access_token=";
        response.sendRedirect(redirectUrl + jwtToken + "&" +
                account_num + "=" + account_num);

        // Access_Token을 이용한 방법
        // id, email, verified_email, name, given_name, family_name, picture, locale 반환
        // apiService.getGoogleUserInf(res_body.getAccess_token());
    }

    @PostMapping("/kakao")
    public String getKakaoLoginUrl(){
        System.out.println("kakao");
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + KakaoClientId + "&redirect_uri=" + KakaoUrl;
        System.out.println(url);
        return url;
    }

    @Value("${naver.oauth.client-id}") String NaverClientId;
    @Value("${naver.oauth.url}") String NaverUrl;
    @Value("${naver.oauth.client-secret}") String NaverClientSecret;
    @PostMapping("/naver")
    public String getNaverLoginUrl(){
        System.out.println("getNaverLoginUrl");
        String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
                + NaverClientId +"&state=STATE_STRING&redirect_uri=" + NaverUrl;
        return url;
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<Map<String, Object>> getKakaoUserCode(@RequestParam("code") String code){
        System.out.println("kakao login execute~~~~~~~~");
        KakaoResponseDto res_body = apiService.getKakaoToken(code);
        String accessToken = res_body.getAccess_token();

        KakaoUserInfDto userInfo = apiService.getKakaoUserInfo(accessToken);
        int provider_id= 1;

        SecurityUser customUserDetail = userService.getUserDtoByEmail(userInfo.getEmail());

        if (customUserDetail == null) {
            SecurityUser dto = new SecurityUser(userInfo.getName(), userInfo.getEmail(), null, 0 ,provider_id);
            userService.register(dto);
            customUserDetail = userService.getUserDtoByEmail(userInfo.getEmail());
        }
        String jwtToken = JwtTokenProvider.createToken(customUserDetail.getEmail());
        // 계좌 갯수 확인
        int account_num = userService.getAccountNum(customUserDetail.getEmail());

        Map<String, Object> map = new HashMap<>();
        map.put("name", userInfo.getName());
        map.put("email", userInfo.getEmail());
        map.put("access_token", jwtToken);
        map.put("account_num", account_num);

        return new ResponseEntity<>(map, HttpStatus.OK);  // JSON 응답을 반환합니다.

    }

    @GetMapping("/login/naver")
    public ResponseEntity<Map<String, Object>> naverUserCode(@RequestParam("code") String code, @RequestParam(name = "state") String state, HttpServletResponse response) throws Exception {
        System.out.println("naver login execute");
        NaverResponseDto res_body = apiService.getNaverToken(code, state);
        // 실제 API 서비스 호출로 교체
        String accessToken = res_body.getAccess_token();
        //System.out.println("access" + accessToken);
        NaverUserInfDto userInfo = apiService.getNaverUserInfo(accessToken);

        SecurityUser customUserDetail = userService.getUserDtoByEmail(userInfo.getEmail());
        System.out.println(customUserDetail);

        if (customUserDetail == null) {
            SecurityUser dto = new SecurityUser(userInfo.getName(), userInfo.getEmail(), null, 0 ,3);
            userService.register(dto);
            customUserDetail = userService.getUserDtoByEmail(userInfo.getEmail());
        }
        String jwtToken = JwtTokenProvider.createToken(customUserDetail.getEmail());

        // 계좌 갯수 확인
        int account_num = userService.getAccountNum(customUserDetail.getEmail());

        Map<String, Object> map = new HashMap<>();
        map.put("id", userInfo.getId());
        map.put("name", userInfo.getName());
        map.put("email", userInfo.getEmail());
        map.put("gender", userInfo.getGender());
        map.put("accessToken", jwtToken);
        map.put("account_num", account_num);

        return new ResponseEntity<>(map, HttpStatus.OK);
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
