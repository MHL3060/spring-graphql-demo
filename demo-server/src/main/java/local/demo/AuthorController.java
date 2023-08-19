package local.demo;


import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    private final BookService bookService;


    @QueryMapping
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








}
