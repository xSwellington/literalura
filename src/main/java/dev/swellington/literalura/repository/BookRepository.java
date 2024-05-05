package dev.swellington.literalura.repository;

import dev.swellington.literalura.model.Author;
import dev.swellington.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<List<Book>> findAllByAuthorsContains(Author author);

    Optional<Integer> countDistinctByAuthorsContainingIgnoreCase(Author author);

    @Query("SELECT b FROM Book b JOIN FETCH b.authors")
    Optional<List<Book>> findAllWithAuthors();

    @Query("SELECT b FROM Book b JOIN FETCH b.authors ORDER BY b.downloadCount DESC limit 10")
    Optional<List<Book>> findTop10ByDownloadCount();

    @Query("SELECT b FROM Book b JOIN b.languages l JOIN FETCH b.authors WHERE l.name = :languageName")
    Optional<List<Book>> findByLanguageName(@Param("languageName") String languageName);
}
