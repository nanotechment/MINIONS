package com.example.minions.webSecurityConfig;

import com.example.minions.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AccountRepository accountRepository;

    //private final PasswordEncoder passwordEncoder;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) accountRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("")
        );
    }
    @Bean
    public LogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public HttpStatusReturningLogoutSuccessHandler httpStatusReturningLogoutSuccessHandler() {
        return new HttpStatusReturningLogoutSuccessHandler();
    }
}
