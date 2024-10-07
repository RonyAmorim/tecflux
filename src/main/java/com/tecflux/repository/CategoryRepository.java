package com.tecflux.repository;

import com.tecflux.entity.Category;
import com.tecflux.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Encontra uma lista de categorias associadas a um departamento específico.
     *
     * @param departmentId ID do departamento.
     * @param pageable     Informações de paginação.
     * @return Página de categorias.
     */
    Page<Category> findByDepartmentId(Long departmentId, Pageable pageable);
}
