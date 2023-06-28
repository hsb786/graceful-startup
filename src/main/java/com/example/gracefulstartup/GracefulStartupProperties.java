package com.example.gracefulstartup;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties("graceful")
public class GracefulStartupProperties {

    private boolean enabled = true;

    private double increaseWeight = 0.2;

    private long eachStepSleepSecond = 10;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getIncreaseWeight() {
        return increaseWeight;
    }

    public void setIncreaseWeight(double increaseWeight) {
        this.increaseWeight = increaseWeight;
    }

    public long getEachStepSleepSecond() {
        return eachStepSleepSecond;
    }

    public void setEachStepSleepSecond(long eachStepSleepSecond) {
        this.eachStepSleepSecond = eachStepSleepSecond;
    }
}
