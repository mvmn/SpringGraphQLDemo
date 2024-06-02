package x.mvmn.demo.springgraphql.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

import x.mvmn.demo.springgraphql.model.Group;

@GraphQlRepository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String group);
}