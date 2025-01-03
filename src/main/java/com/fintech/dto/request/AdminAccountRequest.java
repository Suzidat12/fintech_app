package com.fintech.dto.request;

import com.fintech.validation.ExtendedEmailValidator;
import com.fintech.validation.PhoneNumber;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AdminAccountRequest {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @ExtendedEmailValidator
    private String email;
    @PhoneNumber
    private String phoneNumber;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character.")
    private String password;
}
