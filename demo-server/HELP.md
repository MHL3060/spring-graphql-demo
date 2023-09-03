# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.14/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.14/gradle-plugin/reference/html/#build-image)
* [Spring for GraphQL](https://docs.spring.io/spring-boot/docs/$2.7.14/reference/htmlsingle/index.html#web.graphql)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/2.7.14/reference/htmlsingle/index.html#web.reactive)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a GraphQL service](https://spring.io/guides/gs/graphql-server/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


main_1, simple configuration.
    * show how to add dependency. 
    * add common Java Type to graphql.

    ```
        query {
            getAuthors {
            id
            firstName
            lastName
        }
    }
    ```        
main_2, graphql field
    * parameterized graphql field.

    ```
          fragment bookFragment on Book {
            isdn
            title
          }
        fragment authorFragment on Author {
            firstName
            lastName
        }
    
        query {
            getAuthors {
                ... authorFragment
                books   {
                    ... bookFragment
                    authors {
                        ... authorFragment
                    }
                }
            }
        }
    ```
    
main_2_mutation, mutation. without validation. should add during the demo

    ```
        mutation {
          save(author: {
            firstName: "ben"
          
          }) {
            id
          }
        }
    ```

main_3 graphql security. 
    how to put pre-authorized. depth query, how many fields it can project..
    GraphqlSecurityIT.

main all

client side graphql
   