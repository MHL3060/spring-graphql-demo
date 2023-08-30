package local.demo;


import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;

//this comes first.
@Component
public class AppControllerAdvice implements DataFetcherExceptionResolver {


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
        }

        return Mono.empty();
    }


}
