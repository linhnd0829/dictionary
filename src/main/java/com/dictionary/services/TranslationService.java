package com.dictionary.services;

import com.dictionary.Config;
import com.dictionary.api.RequestPath;
import com.dictionary.entity.Translation;
import com.dictionary.entity.TranslationResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class TranslationService {

    private final RestTemplate restTemplate;

    private static final String APP_ID = "app_id";
    private static final String APP_KEY = "app_key";

    private static final String STRICT_MATCH = "strictMatch";
    private static final String FIELDS = "fields";
    private static final String GRAMMATICAL_FEATURES = "grammaticalFeatures";
    private static final String LEXICAL_CATEGORY = "lexicalCategory";
    private static final String DOMAINS = "domains";
    private static final String REGISTERS = "registers";

    public TranslationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public TranslationResponse getTranslateResult(String sourceLangTranslate, String targetLangTranslate, String wordId, String queryParameter) {
        String url = RequestPath.BASE_URL + RequestPath.TRANSLATION + queryParameter;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.set(APP_ID, Config.APP_ID_VAL);
        headers.set(APP_KEY, Config.APP_KEY_VAL);

        HttpEntity<JsonObject> request = new HttpEntity<>(headers);

        TranslationResponse translationResponse = new TranslationResponse();

        try {
            ResponseEntity<JsonObject> response = this.restTemplate.exchange(url, HttpMethod.GET, request,
                    JsonObject.class, sourceLangTranslate, targetLangTranslate, wordId);

            translationResponse.setStatusCode(response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                Translation translation = new Gson().fromJson(response.getBody(), Translation.class);
                translationResponse.setTranslation(translation);
                return translationResponse;
            }
        } catch (HttpClientErrorException e) {
            translationResponse.setStatusCode(e.getStatusCode());
            translationResponse.setError(e.getMessage());
            return translationResponse;
        }

        return null;
    }

    public String buildQueryParameter(Boolean strictMatch, String fields, String grammaticalFeatures,
                                       String lexicalCategory, String domains, String registers) throws UnsupportedEncodingException {

        StringBuilder queryParameter = new StringBuilder();

        Map<String, String> params = new HashMap<>();
        if (!Objects.isNull(strictMatch)) {
            params.put(STRICT_MATCH, String.valueOf(strictMatch));
        }

        if (!Objects.isNull(fields) && !fields.isEmpty()) {
            params.put(FIELDS, fields);
        }

        if (!Objects.isNull(grammaticalFeatures)) {
            params.put(GRAMMATICAL_FEATURES, grammaticalFeatures);
        }

        if (!Objects.isNull(lexicalCategory)) {
            params.put(LEXICAL_CATEGORY, lexicalCategory);
        }

        if (!Objects.isNull(domains)) {
            params.put(DOMAINS, domains);
        }

        if (!Objects.isNull(registers)) {
            params.put(REGISTERS, registers);
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            queryParameter.append(entry.getKey());
            queryParameter.append("=");
            queryParameter.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            queryParameter.append("&");
        }

        String result = queryParameter.toString();

        if (result.isEmpty()) {
            return result;
        }

        return "?" + result.substring(0, result.length() - 1) ;
    }
}
