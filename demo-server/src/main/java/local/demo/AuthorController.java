package local.demo;


import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    private final BookService bookService;

    @QueryMapping
    @PreAuthorize("hasRole('USER')")
    public Flux<Author> getAuthors() {
        return authorService.getAll();
    }

    @SchemaMapping(typeName = "Author", field = "books")
    public Flux<Book> getBooks(Author author) {
        return bookService.findBooksByAuthorId(author.getId());
    }

    @QueryMapping
    public Mono<Author> findAuthorById(@Argument long id) {
        return authorService.findById(id);
    }

    @QueryMapping
    public Flux<Book> findBooksByAuthorId(@Argument long authorId) {
        return bookService.findBooksByAuthorId(authorId);
    }

    @SchemaMapping(typeName = "Book", field = "authors")
    public Flux<Author> getBooks(Book book, DataFetchingEnvironment environment) {
        log.info("dept={}, fields='{}'", environment.getSelectionSet().getFields().size(), environment.getSelectionSet().getFields());
        return Flux.fromIterable(book.getAuthorIds())
                .flatMap(authorService::findById);

    }

    @SchemaMapping(field = "price")
    public Mono<Float> getBookPrice(Book book, @Argument String country) {

        if (country == null || country.equals("") || country.equalsIgnoreCase("us")) {
            return Mono.just(1.0f);
        } else if (country.equalsIgnoreCase("ca")) {
            return Mono.just(1.3f);
        } else {
            return Mono.error(InvalidCountryException::new);
        }
    }

    @MutationMapping
    public Author save(@Argument @Valid Author author) {
        return authorService.save(author);
    }





}
