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

    private Long totalAbertos;
    private Long totalEmProgresso;
    private Long totalConcluidos;

    // Construtor personalizado para definir as contagens
    public Dashboard(Long totalAbertos, Long totalEmProgresso, Long totalConcluidos) {
        this.totalAbertos = totalAbertos;
        this.totalEmProgresso = totalEmProgresso;
        this.totalConcluidos = totalConcluidos;
    }
}
