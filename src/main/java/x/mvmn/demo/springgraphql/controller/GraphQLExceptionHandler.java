package x.mvmn.demo.springgraphql.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof DataIntegrityViolationException dataIntegrityExc) {
            if (dataIntegrityExc.getCause() instanceof ConstraintViolationException cvex
                    && cvex.getConstraintName().equals("user_data_username_idx")) {
                return GraphqlErrorBuilder.newError()
                        .message("Duplicate user")
                        .errorType(ErrorType.BAD_REQUEST)
                        .build();
            }
        }
        return null;
    }
}