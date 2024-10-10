package com.tecflux.repository;

import com.tecflux.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    boolean existsByLevel(String level);
}
