package ru.levelup.battleship.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginUrl = "/login";

        http.csrf();

        http.authorizeRequests()
                .antMatchers(loginUrl, "/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/**/*.js", "/**/*.css", "/**/*.svg", "/**/*.png").permitAll()
                .antMatchers("/", "/app/**", "/api/**", "/ws/**").hasRole("user")
                .anyRequest().denyAll();

        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"));

        http.formLogin()
                .loginPage(loginUrl)
                .loginProcessingUrl(loginUrl)
                .defaultSuccessUrl("/app/rooms", true)
                .usernameParameter("login")
                .passwordParameter("password")
                .permitAll();

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl(loginUrl)
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}