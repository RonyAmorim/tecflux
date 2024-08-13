package com.tecflux.entity;

import com.tecflux.util.CryptoUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "tb_user")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name="phone" , nullable = false)
    private String phone;

    @Column(name="created_at" , nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name="last_login")
    private Instant lastLogin;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Transient
    private String rawEmail;

    @Transient
    private String rawPassword;

    @Transient
    private String rawPhone;

    @PrePersist
    @PreUpdate
    public void prePersist(){
        this.email = CryptoUtil.encrypt(this.rawEmail);
        this.password = CryptoUtil.encrypt(this.rawPassword);
        this.phone = CryptoUtil.encrypt(this.rawPhone);
    }

    @PostLoad
    public void postLoad() {
        this.rawEmail = CryptoUtil.decrypt(this.email);
        this.rawPassword = CryptoUtil.decrypt(this.password);
        this.rawPhone =  CryptoUtil.decrypt(this.phone);
    }
}
