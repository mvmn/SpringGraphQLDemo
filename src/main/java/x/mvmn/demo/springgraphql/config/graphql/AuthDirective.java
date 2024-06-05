package x.mvmn.demo.springgraphql.config.graphql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLAppliedDirective;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

@Component
public class AuthDirective implements SchemaDirectiveWiring {

    private static final String SPRING_ROLE_PREFIX = "ROLE_";

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        GraphQLFieldDefinition field = environment.getElement();
        GraphQLAppliedDirective directive = environment.getAppliedDirectives().get("auth");
        if (directive != null) {
            List<String> requiredRoles = directive.getArgument("requires").getValue();

            DataFetcher<?> originalDataFetcher = environment.getFieldDataFetcher();

            DataFetcher<?> authDataFetcher = (DataFetchingEnvironment dataFetchingEnvironment) -> {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    throw new AccessDeniedException("User not authenticated");
                }

                String userRole = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
                if (userRole.startsWith(SPRING_ROLE_PREFIX)) {
                    userRole = userRole.substring(SPRING_ROLE_PREFIX.length());
                }

                if (!requiredRoles.contains(userRole)) {
                    throw new AccessDeniedException("Access denied: missing required role, one of "
                            + requiredRoles.stream().collect(Collectors.joining(", ")));
                }

                return originalDataFetcher.get(dataFetchingEnvironment);
            };

            environment.setFieldDataFetcher(authDataFetcher);
        }
        return field;
    }
}