package com.example.minions.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@Table(name = "ACCOUNT")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Account  implements UserDetails {
    @Id
    @Column(name = "ACCOUNT_ID", nullable = false)
    String accountId;
    @Column(name = "EMAIL", nullable = false)
    String email;
    @Column(name = "FULL_NAME")
    String fullName;
    @Column(name = "USERNAME", nullable = false, updatable = false)
    String username;
    @Column(name = "PASSWORD")
    String password;
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", nullable = false)
    Roles roles;

    String otpCode;

    Date OtpTimeToLive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + roles.getRoleName().toUpperCase()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
