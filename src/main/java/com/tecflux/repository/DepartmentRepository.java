package com.tecflux.repository;

import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findById(Long id);

    Page<Department> findByCompany(Company company, Pageable pageable);
}
