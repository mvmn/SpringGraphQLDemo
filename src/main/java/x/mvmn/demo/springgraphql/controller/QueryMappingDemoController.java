package x.mvmn.demo.springgraphql.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class QueryMappingDemoController {

    @QueryMapping()
    public List<String> greet(@Argument("arg") String someArg) {
        return List.of("Hello ", someArg, "!");
    }
}
