package local.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.io.IOException;

class AuthorServiceTest {


    private AuthorService authorService;
    @BeforeEach
    public void setup() throws IOException {
        var objectMapper = new ObjectMapper();
        authorService = new AuthorService(objectMapper);
    }

    @Test
    public void testFind() {
        var authorMono = authorService.findById(1l);
       StepVerifier.create(authorMono)
                       .expectNextMatches(author -> author.getFirstName().equals("Alex"))
                               .verifyComplete();

    }

}