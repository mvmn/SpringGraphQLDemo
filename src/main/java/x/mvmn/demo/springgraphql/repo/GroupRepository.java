package x.mvmn.demo.springgraphql.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.graphql.data.GraphQlRepository;

import x.mvmn.demo.springgraphql.model.Group;
import x.mvmn.demo.springgraphql.model.User;

@GraphQlRepository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String group);

    @Query("SELECT DISTINCT g FROM Group g LEFT JOIN FETCH g.users u WHERE u.id IN (:userIds)")
    List<Group> findGroupsByUserId(@Param("userIds") List<Long> userIds);
}