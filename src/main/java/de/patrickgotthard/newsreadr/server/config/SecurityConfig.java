package de.patrickgotthard.newsreadr.server.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.security.NewsreadrAuthenticationEntryPoint;
import de.patrickgotthard.newsreadr.server.security.NewsreadrUserDetailsService;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Configuration
@ComponentScan("de.patrickgotthard.newsreadr.server.security")
@EnableGlobalMethodSecurity(mode = AdviceMode.ASPECTJ, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
        .authorizeRequests()
        .antMatchers("/css/login.css").permitAll()
        .antMatchers("/favicon.ico").permitAll()
        .antMatchers("/lib/bootstrap/css/bootstrap.min.css").permitAll()
        .antMatchers("/lib/bootstrap/css/bootstrap-theme.min.css").permitAll()
        .antMatchers("/lib/bootstrap/js/bootstrap.min.js").permitAll()
        .antMatchers("/lib/html5shiv/html5shiv.js").permitAll()
        .antMatchers("/lib/jquery/jquery-1.11.0.min.js").permitAll()
        .antMatchers("/lib/respond/respond.min.js").permitAll()
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
