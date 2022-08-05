package com.dictionary.dal;

import com.dictionary.entity.TranslationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TranslationHistoryRepo extends MongoRepository<TranslationHistory, Integer> {
}
