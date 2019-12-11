package com.cefalo.assignment.observer;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DemoHealthIndicator implements HealthIndicator {
    int numberOfRequestInHealth;

    @Override
    public Health getHealth(boolean includeDetails) {
        System.out.println(numberOfRequestInHealth);
        numberOfRequestInHealth++;
        return (numberOfRequestInHealth > 5) ?
                Health.up().withDetail("count", numberOfRequestInHealth).build() :
                Health.down().withDetail("count", "count is less than 5").build();
    }

    @Override
    public Health health() {
        return null;
    }

    @PostConstruct
    void init() {
        numberOfRequestInHealth = 0;
    }
}
