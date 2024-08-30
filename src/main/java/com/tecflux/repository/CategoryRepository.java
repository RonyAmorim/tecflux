package com.tecflux.repository;

import com.tecflux.entity.Category;
import com.tecflux.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByDepartment(Department department);

    Page<Category> findByDepartmentId(Long departmentId, Pageable pageable);
}
