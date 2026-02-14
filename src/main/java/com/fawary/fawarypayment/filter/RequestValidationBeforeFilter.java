package com.fawary.fawarypayment.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {
    /**
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String header=req.getHeader(HttpHeaders.AUTHORIZATION);
        if(null!=header){
            header=header.trim();
            if(StringUtils.startsWithIgnoreCase(header,"Basic")){
                byte[] baseToken=header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decodedToken;
                try{
                    decodedToken= Base64.getDecoder().decode(baseToken);
                    String token=new String(decodedToken,StandardCharsets.UTF_8);
                    int delim=token.indexOf(":");
                    if(delim==-1){
                        throw new BadCredentialsException("Invalid basic authentication token");

                    }
                    String email=token.substring(0,delim);
                    if(!email.toLowerCase().contains("fawary")){
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                }catch(IllegalArgumentException e){
                    throw new BadCredentialsException("Failed to decode basic authentication token",e);

                }
            }
        }

        chain.doFilter(request, response);
    }
}
