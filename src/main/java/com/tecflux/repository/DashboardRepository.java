package com.tecflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tecflux.repository.DashboardRepository;
import com.tecflux.entity.Dashboard;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    @Query("SELECT new com.tecflux.entity.Dashboard(COUNT(t) FILTER (WHERE t.status.name = 'aberto'), " +
           "COUNT(t) FILTER (WHERE t.status.name = 'em progresso'), " +
           "COUNT(t) FILTER (WHERE t.status.name = 'concluído')) " +
           "FROM Ticket t")
    Dashboard obterResumoChamados();
}
