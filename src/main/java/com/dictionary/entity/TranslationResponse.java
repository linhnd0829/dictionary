package com.dictionary.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TranslationResponse {
    private Translation translation;
    private HttpStatus statusCode;
    private String error;
}
