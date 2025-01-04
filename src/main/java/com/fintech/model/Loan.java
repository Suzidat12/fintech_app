package com.fintech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.LoanStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
//@Indexed
@Table(name = "loan")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(
        name = "loan_sequence_gen",
        sequenceName = "loan_seq",
        allocationSize = 1)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_sequence_gen")
    private Long id;
    @Column(name = "laon_amount")
    private BigDecimal loanAmount;
    @Column(name = "tenure")
    @Builder.Default
    private int tenure = 0;  // Tenure in months
    @Column(name = "interest_rate")
    private BigDecimal interestRate;  // Calculated interest rate
    @Column(name = "narration")
    private String narration;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;  // Loan amount + interest
    @Column(name = "disburstment_date")
    private LocalDateTime disbursementDate;
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersAccount user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admin_id")  // Foreign key to Admin table
    private Admin admin;  // Admin who approved or rejected the loa
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "verified_by", referencedColumnName = "id")
    private Admin verifiedBy;
}
