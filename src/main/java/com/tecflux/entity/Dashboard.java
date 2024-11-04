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
    private Long id;  

    @Column(name = "total_abertos")
    private Long totalAbertos;

    @Column(name = "total_em_progresso")
    private Long totalEmProgresso;

    @Column(name = "total_concluidos")
    private Long totalConcluidos;

    @Column(name = "empresa_id")
    private Long empresaId;


    // Construtor personalizado para definir as contagens
    public Dashboard(Long totalAbertos, Long totalEmProgresso, Long totalConcluidos) {
        this.totalAbertos = totalAbertos;
        this.totalEmProgresso = totalEmProgresso;
        this.totalConcluidos = totalConcluidos;
    }
}
