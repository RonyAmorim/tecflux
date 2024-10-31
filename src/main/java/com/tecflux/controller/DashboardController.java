package com.tecflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tecflux.service.DashboardService;
import com.tecflux.entity.Dashboard;


@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/{empresaId}")
    public Dashboard getDashboardPorEmpresa(@PathVariable Long empresaId) {
        return dashboardService.obterResumoChamadosPorEmpresa(empresaId);
    }

    @GetMapping("/dashboard/chamados-por-usuario")
    public List<Object[]> getChamadosPorUsuario() {
    return dashboardService.obterChamadosPorUsuario();
    }
}
