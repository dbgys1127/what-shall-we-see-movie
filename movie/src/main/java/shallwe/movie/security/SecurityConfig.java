package shallwe.movie.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/login-form")
                .defaultSuccessUrl("/")
                .loginProcessingUrl("/process_login")
                .failureUrl("/login-form?error")
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied")
                .and()
                .authorizeHttpRequests(authorize->authorize
                        .antMatchers("/mypage/**").hasRole("USER")
                        .antMatchers("/inquiry/**").hasRole("USER")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/**").permitAll());
        return http.build();
    }
}
