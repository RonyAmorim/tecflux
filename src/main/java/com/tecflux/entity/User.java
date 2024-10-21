package com.tecflux.entity;

import com.tecflux.util.CryptoUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_user")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"password","email","phone","company","department","roles"})
public class User implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = true, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    private String email;  // Armazena o email original para retorno ao frontend

    @Column(name = "email_hash", nullable = false, unique = true)
    private String emailHash;  // Armazena o hash do email para buscas

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name="phone")
    private String phone;

    @Column(name = "position")
    private String position;

    @Column(name="created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name="last_login")
    private Instant lastLogin;

    @Column(name="password_reset_token")
    private String passwordResetToken;

    @Column(name="password_rest_expiry")
    private LocalDateTime passwordRestExpiry;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Transient
    private String rawEmail;

    @Transient
    private String rawPhone;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.rawEmail == null) {
            throw new IllegalStateException("rawEmail não pode ser nulo");
        }
        this.emailHash = CryptoUtil.hash(this.rawEmail);
        this.email = CryptoUtil.encrypt(this.rawEmail);
        if (this.rawPhone != null) {
            this.phone = CryptoUtil.encrypt(this.rawPhone);
        }
    }

    @PostLoad
    public void postLoad() {
        try{
            this.rawEmail = CryptoUtil.decrypt(this.email);
            this.rawPhone = CryptoUtil.decrypt(this.phone);
        } catch (Exception e) {
            logger.error("Erro ao descriptografar email ou telefone", e);
            throw new RuntimeException("Erro ao descriptografar email ou telefone", e);
        }
    }

    // Implementações da interface UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return CryptoUtil.decrypt(this.email);
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
