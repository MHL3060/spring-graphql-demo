package local.demo;

import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;
import graphql.schema.GraphQLFieldDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureGraphQlTester
public class GraphQLSecurityIT extends BaseIT {


    WebGraphQlTester webGraphQlTester;
    @Autowired
    GraphQlSource graphQlSource;

    @Autowired
    ApplicationContext applicationContext;

    List<String> ALL_QUERIES = List.of(
            "query { getAuthors {id}}"
    );

    @BeforeEach
    public void setup() {
        var testClient = WebTestClient.bindToApplicationContext(applicationContext)
                .configureClient()
                .baseUrl("/graphql")
                .build();

        webGraphQlTester = HttpGraphQlTester.create(testClient);
    }

    /**
     * make sure we have all the queries int he ALL_QUERIES.
     */
    @Test
    public void testCoverage() {
        var parser = new Parser();

        var queries = graphQlSource.schema().getQueryType().getFieldDefinitions().stream()
                .map(GraphQLFieldDefinition::getName)
                .collect(Collectors.toSet());

        var queryNames = ALL_QUERIES.stream()
                .map(parser::parseDocument)
                .map(Document::getDefinitions)
                .flatMap(List::stream)
                .filter(d -> d instanceof OperationDefinition)
                .flatMap(d -> ((OperationDefinition) d).getSelectionSet().getSelections().stream())
                .filter(s -> s instanceof Field)
                .map(f -> ((Field)f).getName())
                .collect(Collectors.toList());

        assertThat(queryNames).containsExactlyElementsOf(queries);
    }

    private GraphQlTester.Errors queryAndGetError(String s) {
        return webGraphQlTester.document(s)
                .execute()
                .errors();
    }

    /**
     * make sure each query in the ALL_QUERIES get invoke by the correct security
     * @return
     */
    @TestFactory
    @WithMockUser(username = "foo", password = "bar", roles = {"m"})
    Stream<DynamicTest> testNoAnonymousAccess() {
        return ALL_QUERIES.stream()
                .map( s -> DynamicTest.dynamicTest("testNoAnonymousAccess: " + s,
                        () -> queryAndGetError(s)
                                .satisfy(errors -> {
                                    assertThat(errors).hasSize(1);
                                    assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.FORBIDDEN);
                                })
                ));
    }
}
