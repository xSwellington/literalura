package dev.swellington.literalura.model;

import dev.swellington.literalura.dto.PersonDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "birth_year")
    private int birthYear;

    @Column(nullable = false, name = "death_year")
    private int deathYear;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.DETACH)
    private Set<Book> books = new HashSet<>();


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return String.format("Autor (Id: %d, Nome: %s, Ano de nascimento: %d, Ano de falecimento: %d)", id, name, birthYear, deathYear);
    }
}
