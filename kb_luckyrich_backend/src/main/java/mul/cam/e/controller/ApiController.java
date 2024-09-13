package mul.cam.e.controller;

import mul.cam.e.dto.GoogleResponseDto;
import mul.cam.e.dto.GoogleUserInfDto;
import mul.cam.e.dto.UserDto;
import mul.cam.e.jwt.JwtTokenProvider;
import mul.cam.e.security.UserDetail;
import mul.cam.e.security.UserDetailsServices;
import mul.cam.e.service.ApiService;
import mul.cam.e.service.UserService;
import mul.cam.e.util.TokenDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ApiService apiService;
    private final UserService userService;

    @Autowired
    private UserDetailsServices userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${google.oauth.client-id}") String clientId;
    @Value("${google.oauth.password}") String password;
    @Value("${google.oauth.url}") String redirectUrl;

    @Value("${kakao.oauth.client-id}") String KakaoClientId;
    @Value("${kako.oauth.url}") String KakaoUrl;

    public ApiController(ApiService service, TokenDecoder decoder, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.apiService = service;
        this.userService = userService;
    }

    @GetMapping("google")
    public String getLoginUrl(){
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code" +
                "&scope=openid%20email%20profile";
        return url;
    }

    @GetMapping("login/google")
    public void getUserCode(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
        GoogleResponseDto res_body = apiService.getAccessToken(code);

        // Decode Id_Token
        GoogleUserInfDto userInf = TokenDecoder.decodeIdToken(res_body.getId_token());

        // 유저 조회
        UserDetail userDetail = userDetailsService.loadUserByUsername(userInf.getEmail());

        if(userDetail == null) {
            UserDto user = new UserDto(userInf.getFamily_name()+userInf.getGiven_name(),
                    userInf.getEmail(), null, 0);
            userService.register(user);

            userDetail = userDetailsService.loadUserByUsername(userInf.getEmail());
        }

        // 인증 처리 왜 안됨????????
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDetail, null));
//        System.out.println("authenticated");

        // JWT 토큰 생성
        String jwtToken = JwtTokenProvider.createToken(userDetail.getEmail());

        // Vue Login창으로 Redirect
        String redirectUrl = "http://localhost:5173/login?access_token=";
        response.sendRedirect(redirectUrl + jwtToken);

        // Access_Token을 이용한 방법
        // id, email, verified_email, name, given_name, family_name, picture, locale 반환
        // apiService.getGoogleUserInf(res_body.getAccess_token());
    }

    @PostMapping("kakao")
    public String getKakaoLoginUrl(){
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + KakaoClientId + "&redirect_uri=" + KakaoUrl;
        return url;
    }

    @GetMapping("login/kakao")
    public String getKakaoUserCode(@RequestParam("code") String code){
        String res_body = apiService.getKakaoToken(code);
        System.out.println("asddddddddd"+res_body);

        return "";
    }
}
