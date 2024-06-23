package com.example.minions.repository;

import com.example.minions.entity.Account;
import com.example.minions.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByRoles(Roles roles);

    boolean existsByUsername(String username);
    List<Account> findByAccountIdStartsWith(String finalPrefix);
    Optional<Account> findByAccountId(String accountId);

    List<Account> findAllByRoles(Roles roles);
    Optional<Account> findAccountByUsernameAndEmail(String username, String email);


    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
