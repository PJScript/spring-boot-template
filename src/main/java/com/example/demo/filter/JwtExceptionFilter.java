package com.example.demo.filter;
import com.example.demo.config.SecurityPath;
import com.example.demo.exception.GlobalErroCode;
import com.example.demo.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Getter
@Slf4j
@AllArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final SecurityPath securityPath;



    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        for (String path : securityPath.getPublicPath()) {
            System.out.println(path + ":경로");
            System.out.println(new AntPathRequestMatcher(path).matcher(request) + ":결과");
            if (
                    new AntPathRequestMatcher(path).matches(request)

            ) {
                filterChain.doFilter(request, response);
                return;
            }
        }




        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            System.out.println(authHeader + ":AUTH HEADER");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.info("토큰이 없거나 Bearer 타입이 아님");
                setErrorResponse(GlobalErroCode.MALFORMED_TOKEN, response);
                return;
            }

            String token = authHeader.substring(7); // "Bearer " 다음부터 토큰 추출
            if (token.isEmpty()) {
                setErrorResponse(GlobalErroCode.TOKEN_EMPTY, response);
                return;
            }

            if (!jwtTokenUtils.validate(token)) {
                log.info("토큰이 만료되었습니다.");
                setErrorResponse(GlobalErroCode.TOKEN_EXPIRED, response);
                return;
            }


            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e){
            setErrorResponse(GlobalErroCode.TOKEN_EMPTY, response);
        }
        catch (ExpiredJwtException e) {
            setErrorResponse(GlobalErroCode.TOKEN_EXPIRED, response);
        } catch (UnsupportedJwtException e) {
            setErrorResponse(GlobalErroCode.UNSUPPORTED_TOKEN, response);
        } catch (MalformedJwtException e) {
            setErrorResponse(GlobalErroCode.MALFORMED_TOKEN, response);
        } catch (SignatureException e) {
            setErrorResponse(GlobalErroCode.SIGNATURE_INVALID, response);
        } catch (JwtException e) {
            setErrorResponse(GlobalErroCode.TOKEN_EMPTY, response); // Or a general JWT error enum value
        }
    }

    private void setErrorResponse(GlobalErroCode errorCode, HttpServletResponse response) throws IOException {
        log.info(errorCode.getLogMessge());
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + errorCode.getCode() + "\", \"message\": \"" + errorCode.getMessage() + "\"}");
    }
}
