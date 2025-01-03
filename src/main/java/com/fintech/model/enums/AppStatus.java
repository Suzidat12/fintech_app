package com.fintech.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppStatus {
    COMPLETED("COMPLETED"),
    PENDING("PENDING");

    private final String registrationStatus;
}
