package com.fintech.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserAccountRequest {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Date of Birth is required")
    private String dateOfBirth;
    @Email(message = "The Email is not valid ! INVALID", regexp = ".+[@].+[\\.].+")
    @NotNull(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits and can optionally start with a '+' sign.")
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character.")
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Address is required")
    private String address;
    private String gender;
    @NotNull(message = "BVN is required")
    @Pattern(regexp = "^\\d{11}$", message = "BVN must be exactly 11 digits")
    private String bvn;

}
