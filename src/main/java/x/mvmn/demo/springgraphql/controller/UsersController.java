package x.mvmn.demo.springgraphql.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import x.mvmn.demo.springgraphql.model.Group;
import x.mvmn.demo.springgraphql.model.Role;
import x.mvmn.demo.springgraphql.model.Tuple2;
import x.mvmn.demo.springgraphql.model.User;
import x.mvmn.demo.springgraphql.repo.GroupRepository;
import x.mvmn.demo.springgraphql.repo.UserRepository;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    @MutationMapping
    public User createUser(@Argument String username, @Argument String password, @Argument String firstName,
            @Argument String lastName, @Argument LocalDate dateOfBirth, @Argument Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setRole(role);
        return userRepository.save(user);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument String firstName, @Argument String lastName,
            @Argument String dateOfBirth) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (lastName != null) {
                user.setLastName(lastName);
            }
            if (dateOfBirth != null) {
                user.setDateOfBirth(LocalDate.parse(dateOfBirth));
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @BatchMapping
    public Map<User, List<Group>> groups(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyMap();
        }
        List<Group> groups = groupRepository.findGroupsByUserId(users.stream().map(User::getId).collect(Collectors.toList()));

        return groups.stream().flatMap(group -> group.getUsers().stream().map(user -> Tuple2.of(group, user))).collect(Collectors.groupingBy(Tuple2::getB, Collectors.mapping(Tuple2::getA, Collectors.toList())));
    }
}
