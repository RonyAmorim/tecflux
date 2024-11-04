package com.tecflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tecflux.service.DashboardService;
import com.tecflux.repository.DashboardRepository;
import com.tecflux.entity.Dashboard;
import java.util.List;


@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    public Dashboard obterResumoChamadosPorEmpresa(Long empresaId) {
        return dashboardRepository.obterResumoChamadosPorEmpresa(empresaId);
    }

    public void exibirResumoChamados(Long empresaId) {
        Dashboard resumo = obterResumoChamadosPorEmpresa(empresaId);

        System.out.println("Chamados abertos: " + resumo.getTotalAbertos());
        System.out.println("Chamados em progresso: " + resumo.getTotalEmProgresso());
        System.out.println("Chamados concluídos: " + resumo.getTotalConcluidos());
    }

    public List<Object[]> obterChamadosPorUsuario() {
        return dashboardRepository.obterChamadosPorUsuario();
    }
}

