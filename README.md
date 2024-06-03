A project that demonstrates several GraphQL concepts in integration with the Spring Framework:
- Using a GraphQL directive in conjunction with Spring Security for access control that is defined declaratively in GraphQL schema (no need to browse the Java code for `@PreAuthorize` annotations or anything like that);
- Using an automatic handling of GraphQL queries and pagination with Spring's `@GraphQlRepository` and `ConnectionTypeDefinitionConfigurer` respectively - the first one automatically handles typical queries without a need to write extra code, and the second one auto-generates Edge, Connection and PageInfo types for GraphQL schema.
 
