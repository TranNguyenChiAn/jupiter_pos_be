package com.jupiter.store.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

public class ApplicationProperties {
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
