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

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Transient
    private String rawCnpj;

    @Transient
    private String rawAddress;

    @Transient
    private String rawPhone;

    @PrePersist
    @PreUpdate
    public void prePersist(){
        this.cnpj = CryptoUtil.encrypt(this.rawCnpj);
        this.address = CryptoUtil.encrypt(this.rawAddress);
        this.phone = CryptoUtil.encrypt(this.rawPhone);
    }

    @PostLoad
    public void postLoad(){
        this.rawCnpj = CryptoUtil.decrypt(this.cnpj);
        this.rawAddress = CryptoUtil.decrypt(this.address);
        this.rawPhone = CryptoUtil.decrypt(this.phone);
    }
}
