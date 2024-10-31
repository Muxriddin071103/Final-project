package uz.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import uz.app.repository.UserRepository;
import uz.app.util.UtilConfig;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class MyConfig {
    private final UserRepository userRepository;
    private final MyFilter myFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeRequests()
                .requestMatchers(UtilConfig.openPath)
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/posts/create")
                .hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/posts/edit/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/posts/delete/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.GET,"/posts/myPosts")
                .hasRole("USER")
                .anyRequest()
                .authenticated();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetailsService userDetailsService =(username)->
             userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return userDetailsService;
    }
}
