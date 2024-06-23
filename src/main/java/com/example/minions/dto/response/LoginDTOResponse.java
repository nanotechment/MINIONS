package com.example.minions.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
/*
 * Function-name: DTO class for login
 * Author: Hoàng Đinh
 * Date: 29/09/2023
 */
public class LoginDTOResponse {
    String accessToken;
    String refreshToken;
    AccountResponseAdmin account;
}