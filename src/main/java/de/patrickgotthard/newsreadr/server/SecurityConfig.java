package de.patrickgotthard.newsreadr.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.security.NewsreadrAuthenticationEntryPoint;
import de.patrickgotthard.newsreadr.server.security.NewsreadrUserDetailsService;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Configuration
@EnableGlobalMethodSecurity(mode = AdviceMode.ASPECTJ, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String INITIAL_ADMIN_USERNAME = "admin";
    private static final String INITIAL_ADMIN_PASSWORD = "password";

    @Autowired
    private NewsreadrUserDetailsService userDetailsService;

    @Autowired
    private NewsreadrAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void createAdminUser() {
        if (userRepository.count() == 0) {
            final PasswordEncoder passwordEncoder = passwordEncoder();
            final User user = new User();
            user.setUsername(INITIAL_ADMIN_USERNAME);
            user.setPassword(passwordEncoder.encode(INITIAL_ADMIN_PASSWORD));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
        .authorizeRequests()
        .antMatchers("/css/**").permitAll()
        .antMatchers("/js/**").permitAll()
        .antMatchers("/lib/**").permitAll()
        .antMatchers("**/favicon.ico").permitAll()
        .antMatchers("/login*").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .httpBasic()
        .and()
        .logout()
        .logoutUrl("/logout")
        .permitAll()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .csrf()
        .disable()
        .headers()
        .xssProtection().disable();
        // @formatter:on
    }

}
