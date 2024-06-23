package com.example.minions.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    String currentPassword;

    @Size(min = 6, message = "At least 6 character!")
    @NotEmpty(message = "Input password")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"
            , message = "Must have special characters!")
    @Pattern(regexp = ".*[A-Z].*", message = "At least 1 uppercase characters!")
    String newPassword;

    @NotEmpty(message = "please confirm your password!")
    String confirmPassword;
}
