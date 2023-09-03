package local.demo;


import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ReactiveSecurityDataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this is for the spring graphQL version < 1.2
 * in graphQL 1.2 you can use the ControllerAdvice class
 * and annotate @GraphQlException at the method level.
 */
@Component
public class GraphqlExceptionResolver extends ReactiveSecurityDataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {

        if (exception instanceof ValidationException) {
            var validationException = (ConstraintViolationException) exception;
            var errors = validationException.getConstraintViolations().stream()
                    .map(error -> GraphqlErrorBuilder.newError()
                            .message(error.getMessageTemplate().replace("{", "").replace("}", ""))
                            .extensions(Map.of(error.getPropertyPath().toString(), error.getMessage()))
                            .build()
                    ).collect(Collectors.toList());

            return Mono.just(errors);
        } else {
            return super.resolveException(exception, environment);
        }
    }
}
