package com.example.minions.webSecurityConfig;

import com.example.minions.entity.Account;
import com.example.minions.mapper.AccountMapper;
import com.example.minions.model.TokenPayload;
import com.example.minions.repository.AccountRepository;
import com.example.minions.repository.TokenRepository;
import com.example.minions.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class JwtRequestFilter extends OncePerRequestFilter {
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    TokenRepository tokenRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String requestTokenHeader = request.getHeader("Authorization");
            String token = null;
            TokenPayload tokenPayload = null;
            if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")){
                token = requestTokenHeader.split(" ")[1];
                //check active token
                try{
                    tokenPayload = jwtTokenUtil.getTokenPayLoad(token);
                }catch (ExpiredJwtException exception){
                    System.out.println("Token is expired");
                }
            }else{
                System.out.println("JWT not start with Bearer");
            }

            if(tokenPayload!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                Optional<Account> account = accountRepository.findById(tokenPayload.getAccountId());
                //UserDetails userDetails = this.userDetailsService.loadUserByUsername(account)
                if(account.isPresent()){
                    Account account1 = account.get();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(account1.getUsername());
                    var isTokenValid = tokenRepository.findByToken(token)
                            .map(t ->t.getExpired()==0&&t.getRevoked()==0)
                            .orElse(false);
                    if(jwtTokenUtil.isValid(token, accountMapper.toTokenPayload(account1))&&isTokenValid){



                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails
                                , null
                                , userDetails.getAuthorities());

                        usernamePasswordAuthenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        }catch (AccessDeniedException exception){
            RequestDispatcher requestDispatcher =
                    getServletContext().getRequestDispatcher("http://localhost:8080/login");
            requestDispatcher.forward(request,response);
        }


    }

}

