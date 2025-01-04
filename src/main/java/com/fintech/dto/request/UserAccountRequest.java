package com.fintech.dto.request;

import com.fintech.validation.ExtendedEmailValidator;

import lombok.Data;
import javax.validation.constraints.Pattern;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class UserAccountRequest {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Date of Birth is required")
    private String dateOfBirth;
    @ExtendedEmailValidator
    private String email;
  //  @PhoneNumber
    private String phoneNumber;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character.")
    private String password;
    @NotNull(message = "Address is required")
    private String address;
    private String gender;
    @NotNull(message = "BVN is required")
    private String bvn;
}
