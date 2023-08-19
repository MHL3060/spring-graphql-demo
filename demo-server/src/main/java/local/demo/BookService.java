package local.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final List<Book> books;

    private final TypeReference<List<Book>> bookType = new TypeReference<>() {};
    public BookService(ObjectMapper objectMapper) throws IOException {

        try (var input = this.getClass().getClassLoader().getResourceAsStream("books.json")) {
            books = objectMapper.readValue(input, bookType);
        }
    }

    public Optional<Book> findById(String id) {
        return books.stream()
                .filter(book -> book.getIsdn().equals(id))
                .findAny();
    }

    public List<Book> getAll() {
        return books;
    }

    public Flux<Book> findBooksByAuthorId(Long authorId) {
        return Flux.fromIterable(books.stream()
                .filter(book -> book.getAuthorIds().contains(authorId))
                .collect(Collectors.toList())
        );
    }
}
