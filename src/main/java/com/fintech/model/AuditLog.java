package com.fintech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fintech.model.enums.LoanStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
//@Indexed
@Table(name = "audit_log")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(
        name = "audit_log_sequence_gen",
        sequenceName = "audit_log_seq",
        allocationSize = 1)
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_sequence_gen")

    private String actionType;  // Action type (e.g., "CREATE", "UPDATE", "STATUS_CHANGE")
    private String details;     // Details of the action (e.g., loan amount, user info)
    private LocalDateTime timestamp;  // Timestamp of the action
    private Long userId;        // The ID of the user who triggered the action
    private String adminId;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
