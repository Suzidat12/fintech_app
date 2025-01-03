package com.fintech.util;

import com.fintech.model.Admin;
import com.fintech.model.UsersAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


public class SecurityUtil {
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UsersAccount) {
            UsersAccount userDetails = (UsersAccount) authentication.getPrincipal();
            return userDetails.getId();
        }
        return null;
    }

    public static String getAdminIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Admin) {
            Admin userDetails = (Admin) authentication.getPrincipal();
                return userDetails.getUsername();  // Or return another attribute that identifies the admin

        }
        return null;  // Admin is not authenticated or does not have a valid principal
    }
}
