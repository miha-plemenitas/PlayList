package com.playlist.webgateway.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Enumeration;

@Component
public class AuditInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String auth = request.getHeader("Authorization");

        System.out.println("\n=== [AUDIT LOG - WebGateway] ===");
        System.out.println("Time       : " + LocalDateTime.now());
        System.out.println("IP         : " + ip);
        System.out.println("Method     : " + method);
        System.out.println("URI        : " + uri + (query != null ? "?" + query : ""));
        System.out.println("AuthHeader : " + (auth != null ? auth : "none"));

        // Optional: Dump all headers
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String h = headerNames.nextElement();
            System.out.println("Header     : " + h + " = " + request.getHeader(h));
        }

        System.out.println("====================================\n");
        return true;
    }
}
