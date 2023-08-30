package local.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public class AuthorService {

    private final List<Author> authors;

    private final TypeReference<List<Author>> authorType = new TypeReference<>() {};
    public AuthorService(ObjectMapper objectMapper) throws IOException {
        try (var input = getClass().getClassLoader().getResourceAsStream("authors.json")) {
            authors = objectMapper.readValue(input, authorType);
        }
    }

    public Mono<Author> findById(Long id) {
        var authorOpt = authors.stream()
                .filter(author -> author.getId() == id)
                .findAny();

        return Mono.justOrEmpty(authorOpt);
    }

    public Author save(Author author) {

        if (author.getId() == null) {
            author.setId(System.currentTimeMillis());
            authors.add(author);
        } else {
            for (var i = authors.iterator(); i.hasNext();) {
                if (i.next().getId() == author.getId()) {
                    i.remove();
                }
            }
            authors.add(author);
        }
        return author;
    }

    public Flux<Author> getAll() {
        return Flux.fromIterable(authors);
    }
}
