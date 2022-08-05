package com.dictionary.api;

public class RequestPath {
    public static final String HELLO = "api/v2/hello";
    public static final String TRANSLATION = "/api/v2/translations/{source_lang_translate}/{target_lang_translate}/{word_id}";
    public static final String REACTIVE_TRANSLATION = "/api/v2/reactive_translations/{source_lang_translate}/{target_lang_translate}/{word_id}";

    public static final String BASE_URL = "https://od-api.oxforddictionaries.com";
}
