package com.tecflux.entity;

import com.tecflux.util.CryptoUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_company")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "hashed_cnpj", nullable = false, unique = true)
    private String hashedCnpj;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = true)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Department> departments;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users;

    @Transient
    private String rawCnpj;

    @Transient
    private String rawAddress;

    @Transient
    private String rawPhone;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.rawCnpj == null) {
            throw new IllegalArgumentException("O CNPJ não pode estar vazio");
        }
        this.hashedCnpj = CryptoUtil.hash(this.rawCnpj);
        this.cnpj = CryptoUtil.encrypt(this.rawCnpj);
        this.address = this.rawAddress != null ? CryptoUtil.encrypt(this.rawAddress) : null;
        this.phone = this.rawPhone != null ? CryptoUtil.encrypt(this.rawPhone) : null;
    }

    @PostLoad
    public void postLoad() {
        this.rawCnpj = CryptoUtil.decrypt(this.cnpj);
        this.rawAddress = this.address != null ? CryptoUtil.decrypt(this.address) : null;
        this.rawPhone = this.phone != null ? CryptoUtil.decrypt(this.phone) : null;
    }

}
