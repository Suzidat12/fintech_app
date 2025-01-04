package com.fintech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fintech.model.enums.AppStatus;
import com.fintech.model.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
//@Indexed
@Table(name = "users_account")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(
        name = "users_account_sequence_gen",
        sequenceName = "users_account_seq",
        allocationSize = 1)
public class UsersAccount implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_account_sequence_gen")
    private Long id;

    @Column(name = "full_name")
    private String fullName;
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "gender")
    private String gender;
    @Column(name = "account_balance")
    @Builder.Default
    private BigDecimal accountBalance = BigDecimal.ZERO;
    @Column(name = "bvn")
    private String bvn;
    @Column(name = "is_verified")
    private boolean isVerified = false;
    @Enumerated(EnumType.STRING)
    private AppStatus accountStatus = AppStatus.PENDING;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Loan> loanList;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "verified_by", referencedColumnName = "id")
    private Admin verifiedBy;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.USER.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
