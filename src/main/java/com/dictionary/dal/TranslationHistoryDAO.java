package com.dictionary.dal;

import com.dictionary.entity.Translation;
import com.dictionary.entity.TranslationHistory;
import org.springframework.stereotype.Component;

@Component
public class TranslationHistoryDAO {

    private final TranslationHistoryRepo repo;

    public TranslationHistoryDAO(TranslationHistoryRepo repo) {
        this.repo = repo;
    }

    public void saveTranslationHistory(TranslationHistory translationHistory) {
        System.out.println(translationHistory.toString());
        repo.save(translationHistory);
    }
}
