package com.playlist.user_service.metrics;

import com.playlist.user_service.repository.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

@Component
public class UserMetrics implements MeterBinder {

    private final UserRepository userRepository;

    public UserMetrics(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        registry.gauge("users.total.count", userRepository, repo -> repo.count());
    }
}
