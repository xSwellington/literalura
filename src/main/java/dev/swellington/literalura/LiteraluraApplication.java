package dev.swellington.literalura;

import dev.swellington.literalura.dto.BookDTO;
import dev.swellington.literalura.dto.PersonDTO;
import dev.swellington.literalura.exception.RegisterNotFoundException;
import dev.swellington.literalura.exception.TransformDataException;
import dev.swellington.literalura.model.Author;
import dev.swellington.literalura.model.Book;
import dev.swellington.literalura.model.Language;
import dev.swellington.literalura.repository.AuthorRepository;
import dev.swellington.literalura.repository.BookRepository;
import dev.swellington.literalura.repository.LanguageRepository;
import dev.swellington.literalura.services.AuthorService;
import dev.swellington.literalura.services.BookService;
import dev.swellington.literalura.services.ConsumeAPI;
import dev.swellington.literalura.services.LanguageService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LanguageService languageService;

    private void printMenu() {
        System.out.println("""
                Escolha o número da sua opção:
                				
                1. Buscar livro pelo título
                2. Buscar livros pelo autor
                3. Buscar autor pelo nome
                4. Listar livros registrados
                5. Listar autores registrados
                6. Listar autores vivos em um determinado ano
                7. Listar livros em um determinado idioma
                8. Top 10 livros mais baixados
                9. Listar autor com mais livros baixados
                10. Lista idioma com mais livros registrados
                11. Sair	
                															
                """);
        System.out.print("Opção: ");
    }

    @Override
    public void run(String... args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        var ativo = true;
        while (ativo) {
            try {
                printMenu();
                var opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1: {
                        try {
                            System.out.print("Coloque o título a ser pesquisado: ");
                            var titulo = scanner.nextLine();
                            System.out.println("Aguarde...");
                            Optional<BookDTO> book = bookService.findBookInAPIbyTitle(titulo);
                            if (book.isEmpty()) {
                                System.out.println("Nenhum livro encontrado contendo o título: " + titulo);
                            } else {
                                System.out.println("Livros encontrado com o título: " + titulo);
                                System.out.println();
                                System.out.println("Id: " + book.get().id());
                                System.out.println("Título: " + book.get().title());
                                System.out.println("Autor(es): " + book.get().authors().stream().map(PersonDTO::name).collect(Collectors.joining(", ")));
                                System.out.println("Número de downloads: " + book.get().download_count());
                                System.out.println("Idioma(s): " + String.join(", ", book.get().languages()));
                                System.out.println();
                                bookService.createBook(book.get());
                            }
                        } catch (TransformDataException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case 2: {
                        System.out.print("Coloque o nome ao autor a ser pesquisado: ");
                        var authorName = scanner.nextLine();
                        System.out.println("Aguarde...");
                        try {
                            var author = authorService.findAuthorByName(authorName);
                            System.out.println("Obras de " + author.getName() + ":");
                            System.out.println();
                            System.out.println("[" + bookService.findAllByAuthor(author).stream().map(Book::getTitle).collect(Collectors.joining(", ")) + "]");
                            System.out.println();
                        } catch (RegisterNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 3: {
                        System.out.print("Coloque o nome ao autor a ser pesquisado: ");
                        var authorName = scanner.nextLine();
                        System.out.println("Aguarde...");
                        try {
                            System.out.println();
                            var author = authorService.findAuthorByName(authorName);
                            System.out.println("Id: " + author.getId());
                            System.out.println("Nome: " + author.getName());
                            System.out.println("Ano de nascimento: " + author.getBirthYear());
                            System.out.println("Ano de falecimento: " + author.getDeathYear());
                            System.out.println("Quantidade de livros: " + bookService.countBookByAuthor(author));
                            System.out.println();
                        } catch (RegisterNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 4: {
                        List<Book> books = bookService.listAll();
                        if (books.isEmpty()) {
                            System.out.println("Não há livros registrados no banco de dados.");
                        } else {
                            books.forEach(System.out::println);
                            System.out.println();
                        }
                        break;
                    }
                    case 5: {
                        List<Author> authors = authorService.listAll();
                        if (authors.isEmpty()) {
                            System.out.println("Não há autores registrados no banco de dados.");
                        } else {
                            authors.forEach(System.out::println);
                            System.out.println();
                        }
                        break;
                    }
                    case 6: {
                        System.out.println();
                        System.out.print("Coloque o ano que deseja pesquisa: ");
                        var ano = Integer.parseInt(scanner.nextLine());
                        List<Author> authors = authorService.listbyLivingInYear(ano);
                        if (authors.isEmpty()) {
                            System.out.println("Não há autores registrados no banco de dados correspondente a esse filtro.");
                        } else {
                            System.out.println("Encontrados: " + authors.size());
                            System.out.println();
                            authors.forEach(System.out::println);
                            System.out.println();
                        }
                        break;
                    }
                    case 7: {
                        System.out.println();
                        System.out.print("Coloque o idioma que deseja pesquisa (exemplo: en, pt, fr): ");
                        var idioma = scanner.nextLine();
                        List<Book> books = bookService.findByLanguageName(idioma);
                        if (books.isEmpty()) {
                            System.out.println("Não há livros registrados no banco de dados para esse filtro.");
                        } else {
                            System.out.println("Registros encontrados: " + books.size());
                            books.forEach(System.out::println);
                            System.out.println();
                        }
                        break;
                    }
                    case 8: {
                        System.out.println();
                        List<Book> books = bookService.findTop10ByDownload();
                        if (books.isEmpty()) {
                            System.out.println("Não há livros registrados no banco de dados para esse filtro.");
                        } else {
                            System.out.println("Registros encontrados: " + books.size());
                            books.forEach(System.out::println);
                            System.out.println();
                        }
                        break;
                    }
                    case 9:{
                        try {
                            System.out.println();
                            var author = authorService.findAuthorWithMostDownloads();
                            System.out.println("O autor com mais livros baixados é: " + author.getName());
                            System.out.println();
                        } catch (RegisterNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }

                    case 10: {
                        System.out.println();
                        var language = languageService.getLanguageWithMoreBooks();
                        if (language != null) {
                            System.out.println("O idioma com mais livros é: " + language.getName());
                        } else {
                            System.out.println("Nenhum resultado encontrado para esse filtro");
                        }
                        break;
                    }
                    case 11: {
                        System.out.println("Saiu");
                        ativo = false;
                        break;
                    }
                }
            }catch (NumberFormatException n) {
                System.out.println("Opção inválida.");
                System.out.println();
            }
        }
    }
}
