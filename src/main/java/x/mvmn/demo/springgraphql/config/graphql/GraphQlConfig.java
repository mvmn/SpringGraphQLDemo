package x.mvmn.demo.springgraphql.config.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;

@Configuration
public class GraphQlConfig {

    @Autowired
    private AuthDirective authDirective;

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.directiveWiring(authDirective)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime);
    }

}