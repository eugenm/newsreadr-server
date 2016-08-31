package de.patrickgotthard.newsreadr.server.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private NewsreadrUserDetailsService userDetailsService;

    @Autowired
    private NewsreadrAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/lib/**", "/webjars/**", "/favicon.ico", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
            .httpBasic()
                .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .and()
            .logout()
                .logoutUrl("/logout").permitAll()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
            .csrf()
                .disable()
            .headers()
                .frameOptions()
                    .sameOrigin()
                .xssProtection()
                    .disable();
        // @formatter:on
    }

}
