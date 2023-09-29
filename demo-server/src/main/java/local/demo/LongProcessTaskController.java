package local.demo;


import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RestController
public class LongProcessTaskController {

    @GetMapping(value = "/longTasks")
    public Mono<String> getTask(ServerHttpRequest input) {
        input.getHeaders();
        var future = new CompletableFuture<String>();

        return Mono.fromFuture( () -> {
            try {
                Thread.sleep(Duration.ofMillis(10).toMillis());
                future.complete("OK");
                return future;
            } catch (Exception e) {
                return null;
            }
        });
    }
}
