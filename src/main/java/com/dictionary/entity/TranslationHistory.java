package com.dictionary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "history")
public class TranslationHistory {

    private String ipAddress;
    private String sourceLang;
    private String targetLang;
    private String word;
}
