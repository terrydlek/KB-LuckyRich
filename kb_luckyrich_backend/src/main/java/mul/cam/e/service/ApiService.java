package mul.cam.e.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {

    private final String google_token_url = "https://oauth2.googleapis.com/token";
    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", password);
        params.put("redirect_uri", redirectUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> res = restTemplate.postForEntity(google_token_url, params, String.class);

        return res.getBody();
    }

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kako.oauth.url}") String KakoUrl;
    private final String kakao_token_url = "https://kauth.kakao.com/oauth/token";

    public String getKakaoToken(String code) {
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
        ResponseEntity<String> response = restTemplate.postForEntity(kakao_token_url, request, String.class);

        // 응답 본문 반환
        return response.getBody();
    }


}
