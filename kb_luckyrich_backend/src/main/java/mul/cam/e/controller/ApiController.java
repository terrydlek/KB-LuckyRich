package mul.cam.e.controller;

import mul.cam.e.dto.AccountDto;
import mul.cam.e.dto.TransactionDto;
import mul.cam.e.jwt.JwtTokenProvider;
import mul.cam.e.security.SecurityUser;
import mul.cam.e.security.dto.*;
import mul.cam.e.service.ApiService;
import mul.cam.e.security.SecurityUserService;
import mul.cam.e.enumrate.Role;
import mul.cam.e.service.MyAssetService;
import mul.cam.e.util.TokenDecoder;
import mul.cam.e.util.TransactionGenerator;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ApiService apiService;
    private final SecurityUserService securityUserService;
    private final MyAssetService myAssetService;
    private final TransactionGenerator transactionGenerator;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kakao.oauth.url}") String KakaoUrl;

    @Value("${naver.oauth.client-id}") String NaverClientId;
    @Value("${naver.oauth.url}") String NaverUrl;
    @Value("${naver.oauth.client-secret}") String NaverClientSecret;

    public ApiController(ApiService service, SecurityUserService securityUserService, MyAssetService myAssetService, TransactionGenerator transactionGenerator) {
        this.apiService = service;
        this.securityUserService = securityUserService;
        this.myAssetService = myAssetService;
        this.transactionGenerator = transactionGenerator;
    }

    @GetMapping("/google")
    public String getLoginUrl(){
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code" +
                "&scope=openid%20email%20profile";
        return url;
    }

    @PostMapping("/kakao")
    public String getKakaoLoginUrl(){
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + KakaoClientId + "&redirect_uri=" + KakaoUrl;
        return url;
    }

    @PostMapping("/naver")
    public String getNaverLoginUrl(){
        String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
                + NaverClientId +"&state=STATE_STRING&redirect_uri=" + NaverUrl;
        return url;
    }

    @GetMapping("/login/google")
    public void getUserCode(@RequestParam("code") String code, HttpServletResponse response) throws Exception {    GoogleResponseDto res_body = apiService.getAccessToken(code);

        // Decode Id_Token
        GoogleUserInfDto userInf = TokenDecoder.decodeIdToken(res_body.getId_token());

        // 유저 조회
        SecurityUser customUserDetail = securityUserService.loadUserByUsername(userInf.getSub());

        if(customUserDetail == null) {
            SecurityUser user = SecurityUser.builder()
                    .userName(userInf.getSub())
                    .nickName(userInf.getFamily_name() + userInf.getGiven_name())
                    .email(userInf.getEmail())
                    .gender(null)
                    .age(0)
                    .role(Role.USER.getKey())
                    .build();

            securityUserService.register(user);

            customUserDetail = securityUserService.loadUserByUsername(userInf.getSub());
        }

        // JWT 토큰 생성
        String jwtToken = JwtTokenProvider.createToken(customUserDetail.getUsername());
        String refreshToken = JwtTokenProvider.createRefreshToken(customUserDetail.getUsername());

        // 새로 발급한 refreshToken을 데이터베이스에 저장
        securityUserService.updateRefreshToken(customUserDetail.getUserId(), refreshToken);

        // 계좌 갯수 확인
        int account_num = securityUserService.getAccountNum(customUserDetail.getUsername());

        int userId = customUserDetail.getUserId();
        List<AccountDto> accounts = myAssetService.getAccounts(userId);

        for (AccountDto account : accounts) {
            int account_id = account.getAccountId();
            TransactionDto transactionDto = transactionGenerator.generateRandomTransactionDto(account_id);
            myAssetService.setTransaction(transactionDto);
        }

        // Vue Login창으로 Redirect
        String redirectUrl = "http://localhost:5173/luckyrich/login?access_token=";
        response.sendRedirect(redirectUrl + jwtToken + "&" +
                "account_num=" + account_num + "&" +
                "refresh_token=" + refreshToken);

//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("access_token", jwtToken);
//        responseBody.put("refresh_token", refreshToken);
//        responseBody.put("account_num", account_num);

//        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<Map<String, Object>> getKakaoUserCode(@RequestParam("code") String code) {
        KakaoResponseDto res_body = apiService.getKakaoToken(code);
        String accessToken = res_body.getAccess_token();

        KakaoUserInfDto userInfo = apiService.getKakaoUserInfo(accessToken);

        SecurityUser customUserDetail = securityUserService.loadUserByUsername(userInfo.getId());

        if (customUserDetail == null) {
            SecurityUser dto = SecurityUser.builder()
                    .userName(userInfo.getId())
                    .nickName(userInfo.getName())
                    .email(userInfo.getEmail())
                    .gender(null)
                    .age(0)
                    .role(Role.USER.getKey())
                    .build();

            securityUserService.register(dto);
            customUserDetail = securityUserService.loadUserByUsername(userInfo.getId());
        }

        String jwtToken = JwtTokenProvider.createToken(customUserDetail.getUsername());
        String refreshToken = JwtTokenProvider.createRefreshToken(customUserDetail.getUsername());

        // 새로 발급한 refreshToken을 데이터베이스에 저장
        securityUserService.updateRefreshToken(customUserDetail.getUserId(), refreshToken);

        // 계좌 갯수 확인
        int account_num = securityUserService.getAccountNum(customUserDetail.getUsername());

        int userId = customUserDetail.getUserId();
        List<AccountDto> accounts = myAssetService.getAccounts(userId);

        for (AccountDto account : accounts) {
            int account_id = account.getAccountId();
            TransactionDto transactionDto = transactionGenerator.generateRandomTransactionDto(account_id);
            myAssetService.setTransaction(transactionDto);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("access_token", jwtToken);
        map.put("refresh_token", refreshToken);
        map.put("account_num", account_num);

        return new ResponseEntity<>(map, HttpStatus.OK);  // JSON 응답을 반환
    }


    @GetMapping("/login/naver")
    public ResponseEntity<Map<String, Object>> naverUserCode(@RequestParam("code") String code, @RequestParam(name = "state") String state, HttpServletResponse response) throws Exception {
//        System.out.println("naver login execute");
        NaverResponseDto res_body = apiService.getNaverToken(code, state);
        // 실제 API 서비스 호출로 교체
        String accessToken = res_body.getAccess_token();
        //System.out.println("access" + accessToken);
        NaverUserInfDto userInfo = apiService.getNaverUserInfo(accessToken);

        SecurityUser customUserDetail = securityUserService.loadUserByUsername(userInfo.getId());

        if (customUserDetail == null) {
            SecurityUser dto = SecurityUser.builder()
                    .userName(userInfo.getId())
                    .nickName(userInfo.getName())
                    .email(userInfo.getEmail())
                    .gender(null)
                    .age(0)
//                    .role(Role.USER.getKey())
                    .build();

            if (dto.getUsername().equals("OzasBAmAO1H1Ph-lAJ5A-xmD3ncxo8YRE53C9m90G_M")) {
                dto.setRole(Role.ADMIN.getKey());
            } else {
                dto.setRole(Role.USER.getKey());
            }
            securityUserService.register(dto);
            customUserDetail = securityUserService.loadUserByUsername(userInfo.getId());
        }
        String jwtToken = JwtTokenProvider.createToken(customUserDetail.getUsername());
        String refreshToken = JwtTokenProvider.createRefreshToken(customUserDetail.getUsername());

        // 새로 발급한 refreshToken을 데이터베이스에 저장
        securityUserService.updateRefreshToken(customUserDetail.getUserId(), refreshToken);

        // 계좌 갯수 확인
        int account_num = securityUserService.getAccountNum(customUserDetail.getUsername());

        int userId = customUserDetail.getUserId();
        List<AccountDto> accounts = myAssetService.getAccounts(userId);

        for (AccountDto account : accounts) {
            int account_id = account.getAccountId();
            TransactionDto transactionDto = transactionGenerator.generateRandomTransactionDto(account_id);
            myAssetService.setTransaction(transactionDto);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("access_token", jwtToken);
        map.put("refresh_token", refreshToken);
        map.put("account_num", account_num);

        return new ResponseEntity<>(map, HttpStatus.OK);  // JSON 응답을 반환
    }

    // 로그아웃
    @CrossOrigin(origins = "http://localhost:5173")  // 프론트엔드 주소
    @PostMapping("/logout")
    public ResponseEntity<String> naverUserCode(@RequestParam("token") String accessToken, HttpSession session) throws IOException {
        log.info("네이버 로그아웃 중...");

        // 네이버 토큰 삭제 API 호출
        String deleteTokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=" + NaverClientId
                + "&client_secret=" + NaverClientSecret + "&access_token=" + accessToken + "&service_provider=NAVER";

        log.info("토큰 삭제 URL: " + deleteTokenUrl);

        try {
            String response = requestToServer(deleteTokenUrl);
            log.info("토큰 삭제 응답: " + response);

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
