package com.tecflux.repository;

import com.tecflux.entity.Company;
import com.tecflux.entity.Department;
import com.tecflux.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailHash(String emailHash);
    Optional<User> findByUsername(String username);
}
