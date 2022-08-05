package com.dictionary.api;

import com.dictionary.entity.Translation;
import com.dictionary.entity.TranslationResponse;
import com.dictionary.services.ReactiveWebClientTranslationService;
import com.dictionary.services.TranslationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class Dispatcher {

    private final TranslationService translationService;
    private final ReactiveWebClientTranslationService reactiveWebClientTranslationService;

    public Dispatcher(TranslationService translationService,
                      ReactiveWebClientTranslationService reactiveWebClientTranslationService) {
        this.translationService = translationService;
        this.reactiveWebClientTranslationService = reactiveWebClientTranslationService;
    }

    @GetMapping(RequestPath.HELLO)
    public String hello() {
        return "Hello";
    }

    @GetMapping(RequestPath.TRANSLATION)
    public TranslationResponse translate(@PathVariable String source_lang_translate,
                                         @PathVariable String target_lang_translate,
                                         @PathVariable String word_id,
                                         @RequestParam(required = false) Boolean strictMatch,
                                         @RequestParam(required = false) String fields,
                                         @RequestParam(required = false) String grammaticalFeatures,
                                         @RequestParam(required = false) String lexicalCategory,
                                         @RequestParam(required = false) String domains,
                                         @RequestParam(required = false) String registers) {
        String queryParameter;
        try {
            queryParameter = translationService.buildQueryParameter(strictMatch, fields, grammaticalFeatures,
                    lexicalCategory, domains, registers);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error encoding query parameter");
            System.out.println(e.getMessage());
            return null;
        }

        return translationService.getTranslateResult(source_lang_translate, target_lang_translate, word_id, queryParameter);
    }

    @GetMapping(RequestPath.REACTIVE_TRANSLATION)
    public TranslationResponse translateUsingReactiveWebClient(@PathVariable String source_lang_translate,
                                                             @PathVariable String target_lang_translate,
                                                             @PathVariable String word_id,
                                                             @RequestParam(required = false) Boolean strictMatch,
                                                             @RequestParam(required = false) String fields,
                                                             @RequestParam(required = false) String grammaticalFeatures,
                                                             @RequestParam(required = false) String lexicalCategory,
                                                             @RequestParam(required = false) String domains,
                                                             @RequestParam(required = false) String registers) throws ExecutionException, InterruptedException {
        String queryParameter;
        try {
            queryParameter = translationService.buildQueryParameter(strictMatch, fields, grammaticalFeatures,
                    lexicalCategory, domains, registers);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error encoding query parameter");
            System.out.println(e.getMessage());
            return null;
        }

        Future<TranslationResponse> translationResponse = reactiveWebClientTranslationService.getTranslateResult(source_lang_translate, target_lang_translate, word_id, queryParameter);
        return translationResponse.get();
    }

}
