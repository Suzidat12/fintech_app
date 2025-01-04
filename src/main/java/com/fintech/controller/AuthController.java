package com.fintech.controller;

import com.fintech.dto.JwtAuthenticationResponse;
import com.fintech.dto.LoginRequest;
import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.AdminAccountRequest;
import com.fintech.dto.request.UserAccountRequest;
import com.fintech.model.Admin;
import com.fintech.model.UsersAccount;
import com.fintech.service.AdminService;
import com.fintech.service.UsersAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AdminService adminService;
    private final UsersAccountService usersAccountService;

    @PostMapping("/create-admin")
    public ResponseEntity<ResponseDto<Admin>> create(@RequestBody AdminAccountRequest request){
        return adminService.create(request);
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseDto<UsersAccount>> create(@RequestBody UserAccountRequest request){
        return usersAccountService.create(request);
    }
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody LoginRequest request) {
        return adminService.loginAdmin(request);
    }

    @PostMapping("/login-user")
    public JwtAuthenticationResponse loginUser(@RequestBody LoginRequest request) {
        return usersAccountService.loginUser(request);
    }
}
