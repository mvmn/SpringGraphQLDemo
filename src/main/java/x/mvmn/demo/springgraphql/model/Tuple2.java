package x.mvmn.demo.springgraphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Tuple2<A, B> {
    private final A a;
    private final B b;

    public static <X, Y> Tuple2<X, Y> of(X x, Y y) {
        return Tuple2.<X, Y>builder().a(x).b(y).build();
    }
}
