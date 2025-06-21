package com.example.craft_beer_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class RestTemplateConfig {
    
    // 既存のObjectMapperを利用するために@Autowiredで注入
    @Autowired
    private ObjectMapper objectMapper;
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // カスタムのJackson2HttpMessageConverterを設定
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(objectMapper); // 注入されたObjectMapperを使用
        
        // すべてのコンバーターのリストを作成・設定
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(jacksonConverter);
        
        restTemplate.setMessageConverters(messageConverters);
        
        return restTemplate;
    }
}