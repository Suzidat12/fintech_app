package com.fintech.dto.response;

import com.fintech.validation.ExtendedEmailValidator;
//import com.fintech.validation.PhoneNumber;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAccountResponse {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String password;
    private String address;
    private String gender;
    private String bvn;

    public UserAccountResponse(String firstName, String lastName, String dateOfBirth, String email, String phoneNumber, String password, String address, String gender, String bvn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.bvn = bvn;
    }
}
