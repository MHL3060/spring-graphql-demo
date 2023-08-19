package local.demo;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

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

    public Flux<Author> getAll() {
        return Flux.fromIterable(authors);
    }
}
