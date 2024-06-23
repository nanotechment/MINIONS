package com.example.minions.service;

import com.example.minions.dto.request.*;
import com.example.minions.dto.response.AccountDTOResponse;
import com.example.minions.dto.response.AccountResponseAdmin;
import com.example.minions.dto.response.LoginDTOResponse;
import com.example.minions.entity.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface AccountService {
    AccountDTOResponse createCustomerAccount(AccountDTOCreate accountDTORegister);

    LoginDTOResponse login(LoginDTORequest loginDTORequest);

    AccountResponseAdmin getUserInformation(String username);

    boolean existsByUsername(String username);

    boolean existByEmail(String email);

    List<AccountDTOResponse> getAllUser();

    AccountDTOResponse updateUser(AccountDTOUpdate accountDTOUpdate, String id);

    List<Account> getAccountList();

    List<Account> getCustomerAccountList();

    List<Account> getEmployeeAccountList();

    public Account save(Account account);

    public Account findById(String id);

    public Account findByUsername(String username);

    public AccountDTOResponse updateAccount(AccountDTOUpdate accountUpdate, String id);

    public AccountDTOResponse createEmployeeAccount(AccountDTOCreate accountDTOCreate);

    void sentVerifyEmail(Account account);

    AccountDTOResponse forgetPassword(OTPRequest otpRequest);

    AccountDTOResponse changeAccountPassword(ChangePasswordRequest changePasswordRequest);

    boolean checkMatchedPassword(String password, Account account);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Account findByEmail(String email);

    void saveUserToken(Account user, String jwtToken);
}
