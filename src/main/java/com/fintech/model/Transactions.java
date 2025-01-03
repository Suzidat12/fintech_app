package com.fintech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
//@Indexed
@Table(name = "transactions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(
        name = "transactions_sequence_gen",
        sequenceName = "transactions_seq",
        allocationSize = 1)
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_sequence_gen")
    private Long id;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    @Column(name = "app_status")
    @Enumerated(EnumType.STRING)
    private AppStatus status = AppStatus.PENDING;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersAccount user;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "verified_by", referencedColumnName = "id")
    private Admin verifiedBy;

}
