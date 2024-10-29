package com.tecflux.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "tb_ticket")  // Representando a tabela de dashboard
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dashboard_id")
    private Long id;

    @Column(name = "total_abertos", nullable = false)
    private Long totalAbertos;

    @Column(name = "total_em_progresso", nullable = false)
    private Long totalEmProgresso;

    @Column(name = "total_concluidos", nullable = false)
    private Long totalConcluidos;

    // Construtor personalizado para definir as contagens
    public Dashboard(Long totalAbertos, Long totalEmProgresso, Long totalConcluidos) {
        this.totalAbertos = totalAbertos;
        this.totalEmProgresso = totalEmProgresso;
        this.totalConcluidos = totalConcluidos;
    }
}
