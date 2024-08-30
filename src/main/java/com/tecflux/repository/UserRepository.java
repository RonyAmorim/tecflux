package com.tecflux.repository;

import com.tecflux.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailHash(String emailHash);
    Optional<User> findByName(String name);

    Page<User> findByCompanyId(Long companyId, Pageable pageable);

    Page<User> findByDepartmentId(Long departmentId, Pageable pageable);
}
