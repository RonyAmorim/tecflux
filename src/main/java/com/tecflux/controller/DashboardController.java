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

    @GetMapping("/dashboard")
    public Dashboard getDashboard() {
        return dashboardService.obterResumoChamados();
    }
}
