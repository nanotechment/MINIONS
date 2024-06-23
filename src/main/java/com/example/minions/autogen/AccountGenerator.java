package com.example.minions.autogen;

import com.example.minions.entity.Account;
import com.example.minions.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountGenerator {
    private String customerPrefix = "CUS";
    private String employeePrefix = "EMP";
    private String adminPrefix = "ADM";

    @Autowired
    AccountRepository accountRepository;

    /*
    Function-name: auto generate String id
    Author: Dang Khoa
    Date: 12/10/2023
    */
    public String generate(Account account) {
        String prefix = customerPrefix;
        if (account.getRoles().getRoleId() == 1L) {
            prefix = adminPrefix;
        } else if (account.getRoles().getRoleId() == 2L) {
            prefix = employeePrefix;
        }

        String finalPrefix = prefix;
        List<Account> accounts;
        try {
            accounts = accountRepository.findByAccountIdStartsWith(finalPrefix);
            if(accounts.isEmpty()){
                throw new NullPointerException();
            }
            Long max = 0L;
            for (Account acc : accounts) {
                if (Long.valueOf(acc.getAccountId().substring(3).trim()) > max) {
                    max = Long.valueOf(acc.getAccountId().substring(3).trim());
                }
            }
            return prefix + (String.format("%04d", max + 1));
        } catch (NullPointerException ex) {
            return prefix + (String.format("%04d", 1L));
        }


    }
}
