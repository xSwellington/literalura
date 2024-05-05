package dev.swellington.literalura.services;

import dev.swellington.literalura.model.Language;
import dev.swellington.literalura.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;


    public Language getLanguageWithMoreBooks(){
        Optional<Language> language = languageRepository.findTop1ByOrderByBooksAsc();
        return language.orElse(null);
    }
}
