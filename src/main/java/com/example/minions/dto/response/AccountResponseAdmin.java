package com.example.minions.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseAdmin {

    String accountId;

    String email;

    String fullName;

    String password;

    String roleName;

    String username;

}
