package com.fawary.fawarypayment.filter;


import com.fawary.fawarypayment.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtTokenValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt=request.getHeader(ApplicationConstants.JWT_HEADER);
        if(jwt!=null) {
            try {
                Environment env = getEnvironment();
                if (null != env) {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                            ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    if (null != secretKey) {
                        String newJwt = jwt.replace(ApplicationConstants.TOKEN_PREFIX, "");
                        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(newJwt).getPayload();
                        String username = String.valueOf(claims.get("username"));
                        String roles = String.valueOf(claims.get("Roles"));
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                                AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                }
            } catch (Exception e) {
                throw new BadRequestException("Invalid token");
            }
        }

        filterChain.doFilter(request, response);


    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getServletPath().equals("/user");
//    }
}
