package local.demo;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import static graphql.scalars.ExtendedScalars.*;

@Configuration
public class GraphqlConfiguration {

    /**
     * since Graphql built-in type doesn't include some of the Java common types.
     * add them in here.
     * @return
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(GraphQLLong)
                .scalar(DateTime)
                .scalar(UUID);
    }
}
