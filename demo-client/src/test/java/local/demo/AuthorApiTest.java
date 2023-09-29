package local.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Date;


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

    @Test
    public void testReactor() {
        Flux.just(1,2,3)
                .handle((t, s) -> {
                    var context = s.contextView();
                    System.out.println("first:" + context + " " + System.currentTimeMillis()); //3
                    s.next("" + "f:" + t);
                })
                .contextWrite(e -> e.put(System.currentTimeMillis(), "1231")) //2
                .handle((t, si) -> {
                    var context = si.contextView();
                    System.out.println("second: " + context); //4
                    si.next("" + "s:" + t);
                })
                .contextWrite(e -> e.put(System.currentTimeMillis(), "1231s")) //1
                .subscribe(s -> System.out.println(s));
    }


    @Test
    public void testReactor2() {

        Flux.deferContextual(def -> {


        return Flux.just(1,2,3)
                .handle((t, s) -> {
                    System.out.println("def:"+def);
                    var context = s.contextView();
                    System.out.println("first:" + context + " " + System.currentTimeMillis()); //3
                    s.next("" + "f:" + t);
                })
                .contextWrite(e -> e.put(System.currentTimeMillis(), "1231")) //2
                .handle((t, si) -> {
                    var context = si.contextView();
                    System.out.println("second: " + context); //4
                    si.next("" + "s:" + t);
                })
                .contextWrite(e -> e.put(System.currentTimeMillis() + 10000, "1231s")) //1
               ;
        }).subscribe(s -> System.out.println(s));
    }


}