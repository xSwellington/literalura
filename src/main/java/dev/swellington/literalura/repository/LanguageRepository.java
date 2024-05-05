package dev.swellington.literalura.repository;

import dev.swellington.literalura.model.Book;
import dev.swellington.literalura.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByName(String name);
    Optional<Language> findByName(String name);


    Optional<Language> findTop1ByOrderByBooksAsc();
}
