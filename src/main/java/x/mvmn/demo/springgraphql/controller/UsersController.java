package x.mvmn.demo.springgraphql.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import x.mvmn.demo.springgraphql.model.Role;
import x.mvmn.demo.springgraphql.model.User;
import x.mvmn.demo.springgraphql.repo.UserRepository;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UserRepository userRepository;
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
}
