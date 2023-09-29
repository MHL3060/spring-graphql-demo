package local.demo;

import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(
        configuration = {
                ReactiveFeignInterceptorConfiguration.class
        },
        url = "http://localhost:8080",
        name = "restClient"

)
public interface FeignClient {

    @GetMapping("/longTasks")
    Mono<String> longTask();
}
