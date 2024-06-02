package x.mvmn.demo.springgraphql.controller;

import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import x.mvmn.demo.springgraphql.model.Group;
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
}
