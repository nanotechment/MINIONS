package com.example.minions.webSecurityConfig;

import com.example.minions.autogen.AccountGenerator;
import com.example.minions.repository.AccountRepository;
import com.example.minions.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.minions.util.Constant.ACCOUNT_API;
import static com.example.minions.util.Constant.ADMIN_API;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    //private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountGenerator accountGenerator;

    @Bean
    //Ma hoa password
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(String.format("%s/register", ADMIN_API)).permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/google/login").permitAll()
                        .requestMatchers("/" + ACCOUNT_API + "/register").permitAll()
                        .requestMatchers("/" + ACCOUNT_API + "/changePassword").permitAll()
                        .requestMatchers("/" + ACCOUNT_API + "/reset-password").permitAll()
                        .requestMatchers("/" + ACCOUNT_API + "/employee_register").permitAll()
                        .requestMatchers("/" + ACCOUNT_API + "/verifyEmail/%s").permitAll()
                        .requestMatchers("/payment-callback/%s").permitAll()
                        .requestMatchers("/login/google/callback/%s").permitAll()
                        //.requestMatchers("/" + ACCOUNT_API + "/resend_verify_link").permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**").permitAll()
                        //.requestMatchers("/api/type/%s").hasAuthority("ROLE_ADMIN")

                        //.requestMatchers("/api/account/user-profile")
                        //.requestMatchers("/").permitAll()

                        .anyRequest().permitAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        httpSecurity
                .logout(
                        logout -> logout.logoutUrl("/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                                .clearAuthentication(true)
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies()
                )
                // .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

        ;
        return httpSecurity.build();
    }
}


