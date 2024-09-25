package mul.cam.e.jwt;

import mul.cam.e.security.UserDetail;
import mul.cam.e.security.UserDetailsServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServices userDetailServices;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServices userDetailServices) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailServices = userDetailServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        String email = null;

        // /api 엔드포인트를 제외
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if (requestURI.startsWith("/api") || requestURI.startsWith("/myasset/") || requestURI.startsWith("/recommend/") || requestURI.startsWith("/test-redis")|| requestURI.startsWith("/realestate/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 존재시
        if (token != null) {
            try {
                email = jwtTokenProvider.getEmail(token);
            } catch (Exception e) {
                // 토큰이 유효하지 않음
                System.out.println("***** 유효하지 않은 토큰 " + new Date());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        // 사용자 이메일이 존재하고 인증되지 않은 경우
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 사용자 정보를 로드
            UserDetail userDetail = userDetailServices.loadUserByUsername(email);
            // 토큰이 유효한 경우
            if (jwtTokenProvider.validateToken(token)) {
//                System.out.println("UserDetail: " + userDetail);
                // 인증 토큰을 생성하고 SecurityContext에 저장
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, userDetail.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // 토큰이 유효하지 않은 경우 401 에러 반환
                System.out.println("**** 유효하지 않은 토큰 " + new Date());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        } else if (email == null) {
            // 토큰이 없는 경우 401 에러 반환
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is missing");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // request에서 토큰이 제대로 들어왔는지 확인
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /*
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtTokenProvider provider;

    public JwtTokenFilter(JwtTokenProvider provider) {
        System.out.println("* JwtTokenFilter 생성자 " + new Date());
        this.provider = provider;
    }

    // JWT토큰필터 doFilter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("** JwtTokenFilter doFilterInternal " + new Date());

        System.out.println("*** JwtTokenProvider.httpHeaderKey : " + JwtTokenProvider.httpHeaderKey);

        String token = request.getHeader(JwtTokenProvider.httpHeaderKey);

        String path = request.getRequestURI();

        // 경로가 /asset/** 또는 /api/**로 시작하는 경우, 필터를 통과시킴
        if (path.startsWith("/asset/") || path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(token != null) {
            System.out.println("**** token:" + BearerRemove(token));
            token = BearerRemove(token);
        }

        // 토큰이 만료되면 EXPIRED_TOKEN 메시지를 보내주고 빠져 나간다
        if(token != null && !provider.validateToken(token)){
            System.out.println("***** doFilterInternal 토큰 만료 " + new Date());

            // filter에서 다음처리를 하지 않고 바로 데이터를 리턴
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print("EXPIRED_TOKEN");
            return; // 여기서 리턴하지 않으면 403에러!
        }
        //유효한 토큰인지 확인합니다.
        else if (token != null && provider.validateToken(token)) {
            System.out.println("***** 유효한 토큰 " + new Date());

            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = provider.getAuthentication(token);

            //SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("authentication:" + authentication);
        }
        else { // 처음 들어 온 경우에는 토큰이 없다   token == null
            System.out.println("***** 유효하지 않은 토큰 " + new Date());
        }

        filterChain.doFilter(request, response);
    }
    */

    // 토큰을 만들면 앞에 문자열이 추가된다. "Bearer" 를 제거해 주는 함수
//    private String BearerRemove(String token) {
//        return token.substring("Bearer ".length());
//    }

}
