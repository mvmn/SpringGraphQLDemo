package x.mvmn.demo.springgraphql.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import x.mvmn.demo.springgraphql.model.Role;
import x.mvmn.demo.springgraphql.repo.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public WebSecurityCustomizer customizer() {
        return web -> web.ignoring().requestMatchers("/graphiql", "/vendor/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/graphql")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .authorizeHttpRequests(Customizer.withDefaults())
                .formLogin(conf -> conf.permitAll().successForwardUrl("/graphiql").successHandler(successHandler()))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        if (userRepository.count() < 1) {
            userRepository.save(x.mvmn.demo.springgraphql.model.User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("admin"))
                    .role(Role.ADMIN)
                    .build());
        }

        return username -> userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/graphiql"); // Redirect to /graphiql after successful login
        return handler;
    }
}