package com.tecflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tecflux.repository.DashboardRepository;
import com.tecflux.entity.Dashboard;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

   @Query("SELECT new com.tecflux.entity.Dashboard(" +
         "SUM(CASE WHEN t.status = 1 THEN 1 ELSE 0 END), " +
         "SUM(CASE WHEN t.status = 2 THEN 1 ELSE 0 END), " +
         "SUM(CASE WHEN t.status = 3 THEN 1 ELSE 0 END)) " +
         "FROM Ticket t WHERE t.empresa.id = :empresaId")
   Dashboard obterResumoChamadosPorEmpresa(Long empresaId);

   @Query("SELECT u.id, COUNT(t) FROM Ticket t JOIN t.user u GROUP BY u.id")
   List<Object[]> obterChamadosPorUsuario();
}
