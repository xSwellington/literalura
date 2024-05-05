package dev.swellington.literalura.services;

import dev.swellington.literalura.dto.BookDTO;
import dev.swellington.literalura.dto.SearchDTO;
import dev.swellington.literalura.exception.TransformDataException;
import dev.swellington.literalura.model.Author;
import dev.swellington.literalura.model.Book;
import dev.swellington.literalura.model.Language;
import dev.swellington.literalura.repository.AuthorRepository;
import dev.swellington.literalura.repository.BookRepository;
import dev.swellington.literalura.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Optional<BookDTO> findBookInAPIbyTitle(String title) throws IOException, InterruptedException, TransformDataException {
        var responseJson = ConsumeAPI.makeRequest("/books", new String[][]{{"search", title}});
        SearchDTO searchDTO = new TransformData().transform(responseJson, SearchDTO.class);
        return searchDTO.results().stream().filter(e -> e.title().toLowerCase().contains(title.toLowerCase())).findFirst();
    }


    public void createBook(BookDTO e) {
        if (bookRepository.existsById((long) e.id())) return;
        Book book = new Book();
        book.setId((long) e.id());
        book.setTitle(e.title());
        book.setDownloadCount(e.download_count());
        bookRepository.save(book);

        e.authors().forEach(a -> {
            Optional<Author> author = authorRepository.findByNameIgnoreCase(a.name());
            Author author1;
            if (author.isPresent()) {
                author1 = author.get();
            } else {
               author1 = new Author();
               author1.setBirthYear(a.birth_year());
               author1.setName(a.name());
               author1.setDeathYear(a.death_year());
               authorRepository.save(author1);
            }
            author1.getBooks().add(book);
            book.getAuthors().add(author1);
        });

        e.languages().forEach(a -> {
            Optional<Language> lang = languageRepository.findByName(a.trim());
            Language language1;
            if (lang.isPresent()) {
                language1 = lang.get();
            } else {
                language1 = new Language(a);
                languageRepository.save(language1);
            }
            language1.getBooks().add(book);
            book.getLanguages().add(language1);
        });

        bookRepository.save(book);
    }

    public List<Book> findAllByAuthor(Author author) {
       Optional<List<Book>> books = bookRepository.findAllByAuthorsContains( author );
       return books.orElseGet(ArrayList::new);
    }

    public Integer countBookByAuthor(Author author) {
        return bookRepository.countDistinctByAuthorsContainingIgnoreCase(author).orElse(0);

    }

    public List<Book> listAll() {
        Optional<List<Book>> books = bookRepository.findAllWithAuthors();
        return books.orElseGet(ArrayList::new);
    }

    public List<Book> findByLanguageName(String idioma) {
        Optional<List<Book>> books = bookRepository.findByLanguageName(idioma);
        return books.orElseGet(ArrayList::new);
    }

    public List<Book> findTop10ByDownload(){
        Optional<List<Book>> books= bookRepository.findTop10ByDownloadCount();
        return books.orElseGet(ArrayList::new);
    }
}
