package com.dictionary.services;

import com.dictionary.Config;
import com.dictionary.api.RequestPath;
import com.dictionary.entity.Translation;
import com.dictionary.entity.TranslationResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@Service
public class ReactiveWebClientTranslationService {

    private final WebClient webClient;

    private static final String APP_ID = "app_id";
    private static final String APP_KEY = "app_key";

    private static final String ERROR = "error";

    public ReactiveWebClientTranslationService() {
        this.webClient = WebClient.create();
    }

    @Async
    public Future<TranslationResponse> getTranslateResult(String sourceLangTranslate, String targetLangTranslate, String wordId, String queryParameter) {

        CompletableFuture<TranslationResponse> promise = new CompletableFuture<>();

        String url = RequestPath.BASE_URL + RequestPath.TRANSLATION + queryParameter;

        TranslationResponse translationResponse = new TranslationResponse();

        Mono<ClientResponse> translationMono = webClient.get()
                .uri(url, sourceLangTranslate, targetLangTranslate, wordId)
                .header(APP_ID, Config.APP_ID_VAL)
                .header(APP_KEY, Config.APP_KEY_VAL)
                .exchange();

        translationMono.subscribe(response -> {
            translationResponse.setStatusCode(response.statusCode());
            Mono<String> bodyToMono = response.bodyToMono(String.class);

            bodyToMono.subscribe(res -> {
                JsonObject jsonObject = new Gson().fromJson(res, JsonObject.class);
                if (!Objects.isNull(jsonObject.get(ERROR))) {
                    translationResponse.setError(res);
                    promise.complete(translationResponse);
                    return;
                }
                Translation translation = new Gson().fromJson(jsonObject, Translation.class);
                translationResponse.setTranslation(translation);
                promise.complete(translationResponse);
            }, ex -> {
                translationResponse.setError(ex.getMessage());
                promise.complete(translationResponse);
            });
        }, ex -> {
            translationResponse.setError(ex.getMessage());
            promise.complete(translationResponse);
        });

        return promise;

    }
}
