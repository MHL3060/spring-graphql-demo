package local.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.client.GraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuthorApi {

    private final GraphQLClient graphQLClient;
    private final MonoGraphQLClient monoGraphQLClient;

    private final String graphQLQueries;

    private final ObjectMapper objectMapper;

    public AuthorApi(@Value("classpath:graphql/author.query.graphql") Resource queryFile,
                     GraphQLClient graphQLClient,
                     MonoGraphQLClient monoGraphQLClient,
                     ObjectMapper objectMapper) throws IOException {

        this.graphQLClient = graphQLClient;
        this.monoGraphQLClient = monoGraphQLClient;
        this.objectMapper = objectMapper;

        try (var reader = new InputStreamReader(queryFile.getInputStream(), StandardCharsets.UTF_8)) {
            graphQLQueries = FileCopyUtils.copyToString(reader);
        }
    }

    public Flux<Book> getBooksByAuthor(Long authorId) {
        var result = monoGraphQLClient.reactiveExecuteQuery(graphQLQueries, Map.of("authorId", authorId), "findBooksByAuthorId")
                .map(response -> convert(response, Book.class));

        return result.flatMapIterable(Function.identity());
    }

    public Flux<Author> getAuthors() {
        var result = monoGraphQLClient.reactiveExecuteQuery(graphQLQueries, Map.of(), "getAllAuthors")
                .map(response -> convert(response, Author.class));

        return result.flatMapIterable(Function.identity());
    }

    private <T> List<T> convert(GraphQLResponse response, Class<T> ref) {
        var result = (List<T>) Optional.ofNullable(response.extractValue("data.rows[*]"))
                .map(List.class::cast)
                .orElseGet(List::of)
                .stream()
                .map(jsonNode -> objectMapper.convertValue(jsonNode, ref))
                .collect(Collectors.toList());

        return result;
    }
}
