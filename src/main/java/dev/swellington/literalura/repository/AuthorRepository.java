package dev.swellington.literalura.repository;

import dev.swellington.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCase(String name);
    @Query("SELECT a FROM Author a WHERE :year BETWEEN a.birthYear AND a.deathYear")
    Optional<List<Author>> findAuthorsByYear(int year);


    @Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books b WHERE b.downloadCount = (SELECT MAX(b2.downloadCount) FROM Book b2)")
    Optional<Author> findAuthorWithMostDownloads();
}
