package local.demo;

/**
 * this only for the graphql version > 1.2
@ControllerAdvice
public class RestExceptionHandler {

    @GraphQlException
    public GraphqlError invalidCountry(InvalidCountryException e) {
        return ResponseEntity.badRequest()
                .build();
    }
}
 **/
