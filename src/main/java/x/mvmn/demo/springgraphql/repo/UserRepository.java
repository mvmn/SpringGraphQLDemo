package x.mvmn.demo.springgraphql.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.stereotype.Repository;

import x.mvmn.demo.springgraphql.model.User;

@Repository
@GraphQlRepository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = { "groups" })
    List<User> findAll();

    @EntityGraph(attributePaths = { "groups" })
    Optional<User> findById(Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.groups g WHERE g.id IN (:groupIds)")
    List<User> findUsersByGroupId(@Param("groupIds") List<Long> groupIds);
}
