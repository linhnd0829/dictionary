package com.dictionary.api;

import com.dictionary.dal.TranslationHistoryDAO;
import com.dictionary.dal.TranslationHistoryRepo;
import com.dictionary.entity.Translation;
import com.dictionary.entity.TranslationHistory;
import com.dictionary.entity.TranslationResponse;
import com.dictionary.services.ReactiveWebClientTranslationService;
import com.dictionary.services.TranslationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class Dispatcher {

    private final TranslationService translationService;
    private final ReactiveWebClientTranslationService reactiveWebClientTranslationService;

    private final TranslationHistoryDAO translationHistoryDAO;

    public Dispatcher(TranslationService translationService,
                      ReactiveWebClientTranslationService reactiveWebClientTranslationService,
                      TranslationHistoryDAO translationHistoryDAO) {
        this.translationService = translationService;
        this.reactiveWebClientTranslationService = reactiveWebClientTranslationService;
        this.translationHistoryDAO = translationHistoryDAO;
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
                                         @RequestParam(required = false) String registers,
                                         HttpServletRequest request) {
        String queryParameter;
        try {
            queryParameter = translationService.buildQueryParameter(strictMatch, fields, grammaticalFeatures,
                    lexicalCategory, domains, registers);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error encoding query parameter");
            System.out.println(e.getMessage());
            return null;
        }

        TranslationHistory translationHistory = new TranslationHistory(request.getRemoteAddr(), source_lang_translate, target_lang_translate, word_id);
        translationHistoryDAO.saveTranslationHistory(translationHistory);

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
                                                             @RequestParam(required = false) String registers,
                                                               HttpServletRequest request) throws ExecutionException, InterruptedException {
        String queryParameter;
        try {
            queryParameter = translationService.buildQueryParameter(strictMatch, fields, grammaticalFeatures,
                    lexicalCategory, domains, registers);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error encoding query parameter");
            System.out.println(e.getMessage());
            return null;
        }

        TranslationHistory translationHistory = new TranslationHistory(request.getRemoteAddr(), source_lang_translate, target_lang_translate, word_id);
        translationHistoryDAO.saveTranslationHistory(translationHistory);

        Future<TranslationResponse> translationResponse = reactiveWebClientTranslationService.getTranslateResult(source_lang_translate, target_lang_translate, word_id, queryParameter);
        return translationResponse.get();
    }

}
