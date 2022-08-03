package com.example.demo.services;

import com.example.demo.Config;
import com.example.demo.api.RequestPath;
import com.example.demo.entity.Translation;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Service
public class TranslationService {

    private final RestTemplate restTemplate;

    private static final String APP_ID = "app_id";
    private static final String APP_KEY = "app_key";


    public TranslationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Translation getTranslateResult(String sourceLangTranslate, String targetLangTranslate, String wordId) {
        String url = RequestPath.BASE_URL + RequestPath.TRANSLATION;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.set(APP_ID, Config.APP_ID_VAL);
        headers.set(APP_KEY, Config.APP_KEY_VAL);

        HttpEntity<Translation> request = new HttpEntity<>(headers);

        ResponseEntity<Translation> response = this.restTemplate.exchange(url, HttpMethod.GET, request,
                Translation.class, sourceLangTranslate, targetLangTranslate, wordId);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }
}
