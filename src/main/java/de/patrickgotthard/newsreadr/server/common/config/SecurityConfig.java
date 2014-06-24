package de.patrickgotthard.newsreadr.server.common.config;

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

import de.patrickgotthard.newsreadr.server.security.NewsreadrAuthenticationEntryPoint;
import de.patrickgotthard.newsreadr.server.security.NewsreadrUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(mode = AdviceMode.ASPECTJ, securedEnabled = true, prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private NewsreadrUserDetailsService userDetailsService;

    @Autowired
    private NewsreadrAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        // url protection
        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/lib/**", "/favicon.ico", "/login").permitAll().anyRequest().authenticated();

        // enable basic auth
        http.httpBasic();

        // form login
        http.formLogin().loginPage("/login").permitAll();

        // logout
        http.logout().logoutUrl("/logout").permitAll();

        // enable api-aware exception handling
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        // disable csrf
        http.csrf().disable();

        // disable xss protection
        http.headers().xssProtection().disable();

    }
}
