package com.playlist.user_service.audit;

import com.playlist.user_service.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class AuditLogger {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        System.out.println("✅ [AuditLogger] Aspect initialized");
    }

    @Before("execution(* com.playlist.user_service.controller.*.*(..))")
    public void debugBefore(JoinPoint joinPoint) {
        System.out.println("👀 [AuditLogger] Intercepted: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* com.playlist.user_service.controller.*.*(..))", returning = "result")
    public void logAudit(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        String clientIP = request.getRemoteAddr();
        String token = request.getHeader("Authorization");
        String user = "Anonymous";

        if (token != null && token.startsWith("Bearer ")) {
            try {
                user = jwtUtil.extractEmail(token.substring(7));
            } catch (Exception ignored) {
            }
        }

        System.out.println("\n=== [AUDIT LOG] ===");
        System.out.println("Time       : " + LocalDateTime.now());
        System.out.println("User       : " + user);
        System.out.println("Client IP  : " + clientIP);
        System.out.println("Method     : " + method);
        System.out.println("Args       : " + Arrays.toString(joinPoint.getArgs()));
        System.out.println("Result     : " + result);
        System.out.println("====================\n");
    }
}
