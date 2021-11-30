package com.diden.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.diden.config.vo.TokenVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        boolean result = false;
        String requestAccToken = request.getParameter("token_acc");
        String requestRefToken = request.getParameter("token_ref");

        TokenVo requestToken = new TokenVo();
        requestToken.setAccessJwsToken(requestAccToken);
        requestToken.setRefreshJwsToken(requestRefToken);
        log.debug("Acc : {}", requestAccToken);
        log.debug("Ref : {}", requestRefToken);
        Claims claims;
        try {
            claims = jwtTokenProvider.parseJwtToken(requestToken);
            log.debug("claims : {}", claims);
            result = (boolean) claims.get("resulbt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
