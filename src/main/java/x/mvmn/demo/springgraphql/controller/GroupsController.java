package x.mvmn.demo.springgraphql.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import x.mvmn.demo.springgraphql.model.Group;
import x.mvmn.demo.springgraphql.model.Tuple2;
import x.mvmn.demo.springgraphql.model.User;
import x.mvmn.demo.springgraphql.repo.GroupRepository;
import x.mvmn.demo.springgraphql.repo.UserRepository;

@Controller
@RequiredArgsConstructor
public class GroupsController {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @MutationMapping
    public Group createGroup(@Argument String name) {
        Group group = new Group();
        group.setName(name);
        return groupRepository.save(group);
    }

    @MutationMapping
    public User assignUserToGroup(@Argument String user, @Argument String group) {
        Optional<User> optionalUser = userRepository.findByUsername(user);
        Optional<Group> optionalGroup = groupRepository.findByName(group);

        if (optionalUser.isPresent() && optionalGroup.isPresent()) {
            User userToModify = optionalUser.get();
            if (userToModify.getGroups()
                    .stream()
                    .map(Group::getId)
                    .filter(id -> optionalGroup.get().getId().equals(id))
                    .findAny()
                    .isEmpty()) {
                userToModify.getGroups().add(optionalGroup.get());
            }
            return userRepository.save(userToModify);
        } else {
            throw new RuntimeException("User or Group not found");
        }
    }

    @BatchMapping
    public Map<Group, List<User>> users(List<Group> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyMap();
        }
        List<User> users = userRepository.findUsersByGroupId(groups.stream().map(Group::getId).collect(Collectors.toList()));

        return users.stream().flatMap(user -> user.getGroups().stream().map(group -> Tuple2.of(user, group))).collect(Collectors.groupingBy(Tuple2::getB, Collectors.mapping(Tuple2::getA, Collectors.toList())));
    }
}
