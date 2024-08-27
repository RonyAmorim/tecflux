package com.tecflux.repository;

import com.tecflux.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByHashedCnpj(String hashedCnpj);
    Optional<Company> findById(Long id);

    boolean existsByHashedCnpj(String hashedCnpj);
}
