package com.example.minions.service.impl;

import com.example.minions.autogen.AccountGenerator;
import com.example.minions.dto.request.*;
import com.example.minions.dto.response.AccountDTOResponse;
import com.example.minions.dto.response.AccountResponseAdmin;
import com.example.minions.dto.response.LoginDTOResponse;
import com.example.minions.entity.Account;
import com.example.minions.entity.Token;
import com.example.minions.entity.TokenType;
import com.example.minions.exception.MinionsException;
import com.example.minions.mapper.AccountMapper;
import com.example.minions.model.TokenPayload;
import com.example.minions.repository.AccountRepository;
import com.example.minions.repository.TokenRepository;
import com.example.minions.service.AccountService;
import com.example.minions.service.EmailSenderService;
import com.example.minions.service.OTPCodeUtil;
import com.example.minions.service.RoleService;
import com.example.minions.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.minions.util.Constant.OTP_CODE_LINK;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService, UserDetailsService {

    @Autowired
    AccountGenerator accountGenerator;

    PasswordEncoder passwordEncoder;
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    TokenRepository tokenRepository;

    @Override
    public AccountDTOResponse createCustomerAccount(AccountDTOCreate accountDTORegister) {
        //map entity
        Account account = accountMapper.toAccount(accountDTORegister);
        //set encode password
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        //set role
        account.setRoles(roleService.findByRoleName("customer"));
        //generate new id
        String id = accountGenerator.generate(account);
        account.setAccountId(id);

        //sent verify email
        sentVerifyEmail(account);
        return accountMapper.toAccountDTOResponse(account);
    }

    @Override
    public LoginDTOResponse login(LoginDTORequest loginDTORequest) {
        Account account = findByUsername(loginDTORequest.getUsername());
        boolean isAuthentication = passwordEncoder
                .matches(loginDTORequest.getPassword(), account.getPassword());
        if (!isAuthentication) {
            throw MinionsException.badRequest("Username or Password is incorrect");
        }
        final int ONE_DAY_SECOND = 24 * 60 * 60;
        final int ONE_WEEK = 7 *24 * 60 *60;
        var account1 =  LoginDTORequest.builder().build();

        String accessToken = jwtTokenUtil.generateToken(accountMapper.toTokenPayload(account), ONE_DAY_SECOND);
        var refreshToken = jwtTokenUtil.generateToken(accountMapper.toTokenPayload(account), ONE_WEEK);
        saveUserToken(account,accessToken);


        //tra ve cho nguoi dungz

        return LoginDTOResponse.builder()
                .account(accountMapper.toAccountResponseAdmin(account))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AccountResponseAdmin getUserInformation(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(
                () -> MinionsException.notFound("Account not found")
        );


        return accountMapper.toAccountResponseAdmin(account);
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public List<AccountDTOResponse> getAllUser() {
        List<Account> accountList = accountRepository.findAll();
        List<AccountDTOResponse> accountDTOResponseList = new ArrayList<>();
        for (Account account : accountList) {
            accountDTOResponseList.add(accountMapper.toAccountDTOResponse(account));
        }
        return accountDTOResponseList;
    }

    @Override
    public AccountDTOResponse updateUser(AccountDTOUpdate accountDTOUpdate, String id) {
        Account account = accountMapper.toAccount(accountDTOUpdate);
        account.setAccountId(id);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accountRepository.save(account);
        return accountMapper.toAccountDTOResponse(account);
    }

    @Override
    public List<Account> getAccountList() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getCustomerAccountList() {
        return accountRepository.findAllByRoles(roleService.findByRoleName("customer"));
    }

    @Override
    public List<Account> getEmployeeAccountList() {
        return accountRepository.findAllByRoles(roleService.findByRoleName("employee"));
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findById(String id) {
        return accountRepository.findById(id).orElseThrow(
                () -> MinionsException.notFound("Account not found!")
        );
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(
                () -> MinionsException.notFound("Account not found!!")
        );
    }

    @Override
    public AccountDTOResponse updateAccount(AccountDTOUpdate accountDTOUpdate, String id) {
        Account existAccount = findById(id);

        //set fields
        Account account = accountMapper.toAccount(accountDTOUpdate);
        account.setAccountId(existAccount.getAccountId());
        account.setPassword(existAccount.getPassword());
        account.setRoles(existAccount.getRoles());
        account.setOtpCode(existAccount.getOtpCode());
        account.setOtpTimeToLive(existAccount.getOtpTimeToLive());


        //verify updated email
        if (!existAccount.getEmail().equals(accountDTOUpdate.getEmail())) {
            account.setEmail(accountDTOUpdate.getEmail());
            sentVerifyEmail(account);
            return accountMapper.toAccountDTOResponse(account);

        }

        account.setEmail(existAccount.getEmail());
        accountRepository.save(account);

        return accountMapper.toAccountDTOResponse(account);
    }

    @Override
    public AccountDTOResponse createEmployeeAccount(AccountDTOCreate accountDTOCreate) {
        Account account = accountMapper.toAccount(accountDTOCreate);
        account.setRoles(roleService.findByRoleName("employee"));
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountId(accountGenerator.generate(account));
        accountRepository.save(account);
        return accountMapper.toAccountDTOResponse(account);
    }

    public void sentVerifyEmail(Account account) {
        //set otp code and otp time to live
        account.setOtpCode(OTPCodeUtil.generateRandomCode(6));
        account.setOtpTimeToLive(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
        //save otp and otp time to live in database
        account = accountRepository.save(account);
        //sent verify email
        Context context = new Context();
        context.setVariable("code", OTP_CODE_LINK + account.getOtpCode() + "&username=" + account.getUsername());

        emailSenderService.sendEmailWithHtmlTemplate(account.getEmail(), "Movie Theater Verify Email", "confirmEmail", context);
    }

    @Override
    public AccountDTOResponse forgetPassword(OTPRequest otpRequest) {
        Account existAccount = findByUsername(otpRequest.getUsername());

        existAccount.setPassword(passwordEncoder.encode(otpRequest.getPassword()));
        accountRepository.save(existAccount);

        return accountMapper.toAccountDTOResponse(existAccount);
    }

    @Override
    public AccountDTOResponse changeAccountPassword(ChangePasswordRequest changePasswordRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = findByUsername(username);

        account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        accountRepository.save(account);
        return accountMapper.toAccountDTOResponse(account);
    }

    @Override
    public boolean checkMatchedPassword(String password, Account account) {
        return passwordEncoder
                .matches(password, account.getPassword());
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        String refreshToken = null;
        TokenPayload tokenPayload = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.split(" ")[1];
            try {
                tokenPayload = jwtTokenUtil.getTokenPayLoad(refreshToken);
            } catch (ExpiredJwtException ex) {
                System.out.println("Token is expired");
            }

        } else {
            System.out.println("JWT not start with Bearer");
        }
        if (tokenPayload != null) {
            Account account = accountRepository.findById(tokenPayload.getAccountId()).orElseThrow(

            );
            //       if(jwtTokenUtil.isTokenValid(refreshToken, account)){
            var accessToken = jwtTokenUtil.generateToken(accountMapper.toTokenPayload(account), 860000);
            revokeAllUserTokens(account);
            saveUserToken(account, accessToken);
            var authResponse = LoginDTOResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            //      }
        }
    }

    private void revokeAllUserTokens(Account account) {
        var validUserTokens = tokenRepository.findAllValidTokenByAccount(account.getAccountId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(1);
            token.setRevoked(1);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(
                () -> MinionsException.notFound("Account not found!")
        );
    }

    @Override
    public void saveUserToken(Account user, String jwtToken) {
        var token = Token.builder()
                .account(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(0)
                .revoked(0)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }
}
