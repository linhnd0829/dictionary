package com.example.demo.api;

import com.example.demo.entity.Translation;
import com.example.demo.services.TranslationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Dispatcher {

    private final TranslationService translationService;

    public Dispatcher(TranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping(RequestPath.HELLO)
    public String hello() {
        return "Hello";
    }

    @GetMapping(RequestPath.TRANSLATION)
    public Translation entries(@PathVariable String source_lang_translate, @PathVariable String target_lang_translate, @PathVariable String word_id){
        return translationService.getTranslateResult(source_lang_translate, target_lang_translate, word_id);
    }

}
