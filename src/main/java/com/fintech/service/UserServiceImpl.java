package com.fintech.service;

import com.fintech.repository.AdminRepository;
import com.fintech.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // Try to find student first
                UserDetails user = studentRepository.findByEmail(username)
                        .orElse(null);
                if (user == null) {
                    // If student not found, try to find lecturer
                    user = lecturerRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
                }
                if (user == null) {
                    // If lecturer not found, try to find admin
                    user = adminRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
                }
                return user;
            }
        };
    }
}

