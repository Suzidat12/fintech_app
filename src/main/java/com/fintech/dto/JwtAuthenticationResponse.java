package com.fintech.dto;

import com.fintech.model.Admin;
import com.fintech.model.UsersAccount;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;
    private UsersAccount account;
    private Admin admin;
}
