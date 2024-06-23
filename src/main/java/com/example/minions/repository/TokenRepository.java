package com.example.minions.repository;

import com.example.minions.entity.Account;
import com.example.minions.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
select t from Token t inner join Account a\s
on t.account.accountId=a.accountId\s
where a.accountId = :accountId and (t.expired=0 or t.revoked=0)\s
""")
    List<Token> findAllValidTokenByAccount(String accountId);

    Optional<Token> findByToken(String token);

    List<Token> findAllByExpired(int i);

    Optional<Token> findTokenByAccount(Account account);
    List<Token> findTokenByAccount_AccountId(String account);
}