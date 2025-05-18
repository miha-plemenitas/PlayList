package com.playlist.rating_service.audit;

import com.playlist.rating_service.model.Rating;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InterceptorBinding;
import jakarta.interceptor.InvocationContext;
import org.bson.types.ObjectId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.Arrays;

@AuditLog
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class AuditInterceptor {

    @AroundInvoke
    public Object logMethod(InvocationContext ctx) throws Exception {
        String method = ctx.getMethod().getName();
        Object[] args = ctx.getParameters();

        String formattedArgs = Arrays.stream(args)
            .map(arg -> {
                if (arg instanceof Rating rating) {
                    return String.format("Rating{userId=%s, gameId=%s, rating=%d}",
                            rating.userId != null ? rating.userId.toHexString() : "null",
                            rating.gameId != null ? rating.gameId.toHexString() : "null",
                            rating.rating);
                } else {
                    return String.valueOf(arg);
                }
            })
            .reduce((a, b) -> a + ", " + b)
            .orElse("[]");

        System.out.println("\n=== [AUDIT LOG - RatingService] ===");
        System.out.println("Time       : " + LocalDateTime.now());
        System.out.println("Class      : " + ctx.getTarget().getClass().getSimpleName());
        System.out.println("Method     : " + method);
        System.out.println("Args       : [" + formattedArgs + "]");
        System.out.println("====================================\n");

        return ctx.proceed();
    }
}
