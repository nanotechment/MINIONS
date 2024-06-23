package com.example.minions.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDTOCreate {
    @NotEmpty(message = "Username is required!")
    String username;

    @Size(min = 6, message = "At least 6 character!")
    @NotEmpty(message = "Input password")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"
            , message = "Must have special characters!")
    @Pattern(regexp = ".*[A-Z].*", message = "At least 1 uppercase characters!")
    String password;

    @NotEmpty(message = "please confirm your password")
    String confirmPassword;

    @NotEmpty(message = "Please input full name!")
    String fullName;

    @NotEmpty(message = "Please input email!")
    String email;
}
