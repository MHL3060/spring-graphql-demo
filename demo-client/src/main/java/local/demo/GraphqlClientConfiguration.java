package local.demo;

import com.netflix.graphql.dgs.client.GraphQLClient;
import com.netflix.graphql.dgs.client.HttpResponse;
import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import com.netflix.graphql.dgs.client.RequestExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Configuration
public class GraphqlClientConfiguration {

    @Bean
    public GraphQLClient graphqlClient() {
        var restTemplate = new RestTemplate();

        restTemplate.setInterceptors(List.of(
                        (request, body, execution) -> {
                            request.getHeaders()
                                    .set(HttpHeaders.AUTHORIZATION, "Basic Zm9vOmJhcg==");
                            return execution.execute(request, body);
                        }
                )
        );
        RequestExecutor requestExecutor = (url, headers, body) -> {
            var httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::addAll);
            var exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, httpHeaders), String.class);
            return new HttpResponse(exchange.getStatusCodeValue(), exchange.getBody());
        };

        return GraphQLClient.createCustom("http://localhost:8080/grpahql", requestExecutor);
    }


    @Bean
    public MonoGraphQLClient webFluxGraphqlClient() {
        var webClient = WebClient.builder().baseUrl("http://localhost:8080/grpahql")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic Zm9vOmJhcg==")
                .build();

        return MonoGraphQLClient.createWithWebClient(webClient);
    }

}
