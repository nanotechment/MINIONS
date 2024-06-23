package com.example.minions.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
/*
 * Function-name: DTO class for login
 * Author: Hoàng Đinh
 * Date: 29/09/2023
 */
public class LoginDTORequest {
    String username;
    String password;
}