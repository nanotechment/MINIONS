package com.example.minions.webSecurityConfig;

import com.example.minions.autogen.AccountGenerator;
import com.example.minions.entity.Account;
import com.example.minions.exception.MinionsException;
import com.example.minions.repository.AccountRepository;
import com.example.minions.repository.RoleRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountGenerator accountGenerator;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");
        String[] parts = email.split("@");
        String username = parts[0];
        boolean emailVerified = Boolean.TRUE.equals(user.getAttribute("email_verified"));
        String accountImage = user.getAttribute("picture");
        //check if account already exist!
        if (!accountRepository.existsByEmail(email) && !accountRepository.existsByUsername(username)) {
            Account account = Account.builder()
                    .email(email)
                    .username(username)
                    .roles(roleRepository.findByRoleName("customer"))
                    .build();
            account.setAccountId(accountGenerator.generate(account));

            accountRepository.save(account);
            //save to context holder
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            account,
                            account.getPassword(),
                            account.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        } else {
            Account account = accountRepository.findAccountByUsernameAndEmail(username, email).orElseThrow(
                    () -> MinionsException.notFound("Account not found!")
            );
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            account,
                            account.getPassword(),
                            account.getAuthorities()
                    );
            usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
