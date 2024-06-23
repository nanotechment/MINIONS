package com.example.minions.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class OTPRequest {
    @NotEmpty(message = "Please input OTP")
    String otp;

    @NotEmpty(message = "Please input username!")
    String username;

    @Size(min = 6, message = "At least 6 character!")
    @NotEmpty(message = "Input password")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"
            , message = "Must have special characters!")
    @Pattern(regexp = ".*[A-Z].*", message = "At least 1 uppercase characters!")
    String password;

    @NotEmpty(message = "Please confirm your password!")
    String confirmPassword;
}
