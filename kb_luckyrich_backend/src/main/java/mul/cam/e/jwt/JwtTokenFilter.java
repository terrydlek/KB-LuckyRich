package mul.cam.e.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 토큰을 만들면 앞에 문자열이 추가된다. "Bearer" 를 제거해 주는 함수
    private String BearerRemove(String token) {
        return token.substring("Bearer ".length());
    }
}
