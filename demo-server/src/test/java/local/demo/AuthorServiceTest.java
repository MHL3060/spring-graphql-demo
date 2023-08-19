package local.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest {


    private AuthorService authorService;
    @BeforeEach
    public void setup() throws IOException {
        var objectMapper = new ObjectMapper();
        authorService = new AuthorService(objectMapper);
    }

    @Test
    public void testFind() {
        var authorOpt = authorService.findById(1l);
        assertTrue(authorOpt.isPresent());
        assertEquals("Alex", authorOpt.get().getFirstName());
    }

}