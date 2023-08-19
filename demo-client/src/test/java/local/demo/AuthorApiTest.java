package local.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthorApiTest {

    @Autowired
    private AuthorApi authorApi;


    @Test
    public void testGetBook() {
        StepVerifier.create(authorApi.getBooksByAuthor(1l))
                .expectNextMatches(book ->
                    book.getIsdn().equals("1234")
                )
                .expectNextMatches(book -> book.getIsdn().equals("2345"))
                .verifyComplete();
    }

    @Test
    public void testGetAllAuthors() {
        StepVerifier.create(authorApi.getAuthors())
                .expectNextMatches(author ->
                        author.getId() == 1
                )
                .expectNextMatches(author -> author.getId() == 2)
                .verifyComplete();
    }

}