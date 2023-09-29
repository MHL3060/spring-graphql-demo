package local.demo;

import org.springframework.context.annotation.Bean;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;


public class ReactiveFeignInterceptorConfiguration {

    @Bean
    public ReactiveHttpRequestInterceptor intercept() {
        return reactiveHttpRequest -> {
            return Mono.deferContextual(Mono::just)
                    .handle((ctx, sink) -> {
                        try {
                            var uri = new URI(reactiveHttpRequest.uri().toString() +"?accept=" + reactiveHttpRequest.headers().get("Accept"));
                            var request = new ReactiveHttpRequest(
                                    reactiveHttpRequest, uri
                            );
                            request.headers().put("Authorization", List.of("Basic Zm9vOmJhcg=="));
                            sink.next(request);
                        } catch (Exception e) {
                            sink.error(e);
                        }
                    });
        };
    }
}
