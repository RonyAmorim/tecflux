package com.tecflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tecflux.service.DashboardService;
import com.tecflux.entity.Dashboard;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/{empresaId}")
    public ResponseEntity<Dashboard> getDashboardPorEmpresa(@PathVariable Long empresaId) {
        Dashboard dashboard = dashboardService.obterResumoChamadosPorEmpresa(empresaId);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/dashboard/exibir-resumo/{empresaId}")
    public ResponseEntity<Void> exibirResumoChamados(@PathVariable Long empresaId) {
        dashboardService.exibirResumoChamados(empresaId);
        return ResponseEntity.ok().build(); 
    }
}
