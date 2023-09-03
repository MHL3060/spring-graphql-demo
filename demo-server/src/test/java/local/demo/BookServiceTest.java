package local.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookServiceTest {
    private BookService bookService;
    @BeforeEach
    public void setup() throws IOException {
        var objectMapper = new ObjectMapper();
        bookService = new BookService(objectMapper);
    }

    @Test
    public void testFind() {
        var authorOpt = bookService.findById("1234");
        assertTrue(authorOpt.isPresent());
        assertEquals("first cup of JAVA", authorOpt.get().getTitle());
    }
}