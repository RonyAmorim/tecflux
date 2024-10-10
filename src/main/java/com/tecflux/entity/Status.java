package com.tecflux.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tb_status")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "tickets")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets;

    // Construtores personalizados, se necessários
    public Status(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Enum interno para definir os status pré-definidos.
     */
    public enum Values {
        OPEN(1L, "OPEN", "Aberto"),
        IN_PROGRESS(2L, "IN_PROGRESS", "Em Progresso"),
        RESOLVED(3L, "RESOLVED", "Resolvido"),
        CLOSED(4L, "CLOSED", "Fechado"),
        ON_HOLD(5L, "ON_HOLD", "Em Espera"),
        ESCALATED(6L, "ESCALATED", "Escalonado"),
        REOPENED(7L, "REOPENED", "Reaberto"),
        CANCELLED(8L, "CANCELLED", "Cancelado");

        private final Long id;
        private final String name;
        private final String description;

        Values(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        /**
         * Converte o enum para uma instância da entidade Status.
         *
         * @return Instância de Status.
         */
        public Status toStatus() {
            return new Status(this.name, this.description);
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

}
