package mul.cam.e.service;

import mul.cam.e.dto.GoogleResponseDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j;
import mul.cam.e.dto.*;
import mul.cam.e.util.TokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Log4j
@Service
public class ApiService {

    private final TokenDecoder tokenDecoder;

    private final String google_token_url = "https://oauth2.googleapis.com/token";
    private final String google_user_inf_url = "https://www.googleapis.com/userinfo/v2/me";
    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Autowired
    public ApiService(TokenDecoder tokenDecoder) {
        this.tokenDecoder = tokenDecoder;
    }

    public GoogleResponseDto getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", password);
        params.put("redirect_uri", redirectUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<GoogleResponseDto> res = restTemplate
                .postForEntity(google_token_url, params, GoogleResponseDto.class);

        return res.getBody();
    }

    public void getGoogleUserInf(String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate
                .exchange(google_user_inf_url, HttpMethod.GET, entity, Map.class);
        System.out.println(response);
    }

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kako.oauth.url}") String KakoUrl;
    private final String kakao_token_url = "https://kauth.kakao.com/oauth/token";

    public KakaoResponseDto getKakaoToken(String code) {
        // RestTemplate을 사용해 카카오 서버로 요청 보내기
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeaders 설정 (x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KakaoClientId); // 본인의 REST API 키로 교체
        params.add("redirect_uri", KakoUrl); // 리다이렉트 URI
        params.add("code", code); // Authorization Code

        // HttpEntity에 헤더와 파라미터 담기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 카카오 서버로 요청 전송
        ResponseEntity<KakaoResponseDto> response = restTemplate.postForEntity(kakao_token_url, request, KakaoResponseDto.class);

        // 응답 본문 반환
        return response.getBody();
    }
    public KakaoUserInfDto getKakaoUserInfo(String accessToken) {
        KakaoUserInfDto userInfo = new KakaoUserInfDto();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();
//            log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
//            log.info("responseBody = {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String name = properties.get("nickname").getAsString();
            String email = kakaoAccount.get("email").getAsString();

            userInfo.setName(name);
            userInfo.setEmail(email);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    @Value("${naver.oauth.client-id}") private String NaverClientId;
    @Value("${naver.oauth.client-secret}") private String NaverClientSecret;
    @Value("${naver.oauth.url}") private String NaverUrl;

    private final String naver_token_url = "https://nid.naver.com/oauth2.0/token";

    public NaverResponseDto getNaverToken(String code, String state) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NaverClientId);
        params.add("client_secret", NaverClientSecret);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<NaverResponseDto> response = restTemplate.exchange(
                naver_token_url,
                HttpMethod.POST,
                request,
                NaverResponseDto.class
        );

        return response.getBody();
    }

    public NaverUserInfDto getNaverUserInfo(String accessToken) {
        String reqUrl = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> naverProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(reqUrl,
                HttpMethod.GET,
                naverProfileRequest,
                String.class);

        String responseBody = response.getBody();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject responseJson = jsonObject.getAsJsonObject("response");

        NaverUserInfDto naverProfile = new NaverUserInfDto();
        naverProfile.setId(responseJson.get("id").getAsString());
        naverProfile.setName(responseJson.get("name").getAsString());
        naverProfile.setEmail(responseJson.get("email").getAsString());
        naverProfile.setGender(responseJson.get("gender").getAsString());

        return naverProfile;
    }

}
