package mul.cam.e.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

// 토큰을 발행하고 받은 토큰을 분석하는 클래스
@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "pomqi1baytc1t2xsdtw3df44ffe5gs76hsr7je08kn9";
    private static final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000; // 1시간
    //private static final long ACCESS_TOKEN_VALIDITY = 60 * 1000;

  private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 1주일

    // 액세스 토큰 생성
    public static String createToken(String id) {
        return createToken(id, ACCESS_TOKEN_VALIDITY);
    }

    // 리프레시 토큰 생성
    public static String createRefreshToken(String id) {
        return createToken(id, REFRESH_TOKEN_VALIDITY);
    }

    // 공통 토큰 생성 메소드
    private static String createToken(String id, long validityPeriod) {
        Claims claims = Jwts.claims().setSubject(id);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityPeriod);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 토큰 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 리프레시 토큰 검사
    public boolean validateRefreshToken(String token) {
        return validateToken(token);  // 동일한 검증 로직 사용
    }

    // 토큰으로 사용자 ID 확인
    public String getId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 리프레시 토큰으로 사용자 ID 확인
    public String getIdFromRefreshToken(String token) {
        return getId(token);  // 동일한 로직 사용
    }
}


    /*
    public static String httpHeaderKey = "Authorization"; // 허가
    private String securityKey = "myJWTkeymyJWTkeymyJWTkeymyJWTkeymyJWTkey";
    private long delayTime = 60 * 60 * 1000L;    // 토큰의 유효시간

    private final UserDetailsService userService;

    public JwtTokenProvider(UserDetailsService userService) {
        this.userService = userService;
    }

    // 비밀키를 인코딩. 의존성 주입이 이루어진 후 초기화를 수행하는 메소드
    @PostConstruct
    protected void securityKeyEncoding(){
        securityKey = Base64.getEncoder().encodeToString(securityKey.getBytes());
    }

    // 토큰 발행(JWT 토큰 생성)
    public String createToken(String userPk) {
        System.out.println("createToken - 토큰 생성");

        Claims claims = Jwts.claims().setSubject(userPk); // user를 식별하는 값을 넣는다
//        claims.put("roles", roles); // ROLE_USER
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)      // 정보저장
                .setIssuedAt(now)       // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + delayTime)) // 만료시간
                .signWith(SignatureAlgorithm.HS256, securityKey)  // 암호화 알고리즘와 Signature 들어갈 securityKey 값을 세팅
                .compact();

        System.out.println("생성된 토큰 ===> " + token);
        return token;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(getUserInfo(token));
        System.out.println("username: " + userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰의 유효성 + 만료일자 확인 (토큰 검사)
    public boolean validateToken(String jwtToken) {
        System.out.println("validateToken(JwtTokenProvider) - 토큰 검사 " + new Date());
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(securityKey).parseClaimsJws(jwtToken);
            System.out.println(claims );
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //토큰에서 회원 정보 추출(사용자 정보 확인)
    private String getUserInfo(String token) {
        System.out.println("getUserInfo(JwtTokenProvider) - 사용자 정보 확인 " + new Date());
        return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody().getSubject();
    }
    */


