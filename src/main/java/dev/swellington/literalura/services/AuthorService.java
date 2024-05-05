package dev.swellington.literalura.services;

import dev.swellington.literalura.exception.RegisterNotFoundException;
import dev.swellington.literalura.model.Author;
import dev.swellington.literalura.model.Book;
import dev.swellington.literalura.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;


    public Author findAuthorByName(String autor) throws RegisterNotFoundException {
        var author = authorRepository.findByNameIgnoreCase(autor);
        if (author.isPresent()) return author.get();
        throw new RegisterNotFoundException("Nenhum Autor com o nome " + autor + " encontrado no bando de dados.");
    }

    public List<Author> listAll() {
        Optional<List<Author>> authors = Optional.of(authorRepository.findAll());
        return authors.orElseGet(ArrayList::new);
    }

    public List<Author> listbyLivingInYear(int ano) {
        Optional<List<Author>> authors = authorRepository.findAuthorsByYear(ano);
        return authors.orElseGet(ArrayList::new);
    }

    public Author findAuthorWithMostDownloads() throws RegisterNotFoundException {
        Optional<Author> author = authorRepository.findAuthorWithMostDownloads();
       if (author.isEmpty()) throw new RegisterNotFoundException("Nenhuma informação encontrada no banco de dados para esse filtro.");
       return author.get();
    }
}
