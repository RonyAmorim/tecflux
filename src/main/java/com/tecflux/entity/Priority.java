package com.tecflux.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_priority")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private Long id;

    @Column(name = "level", nullable = false, length = 50)
    private String level;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();

    public Priority(Long id, String level, String description) {
        this.id = id;
        this.level = level;
        this.description = description;
    }

    public Priority(String level, String description) {
        this.level = level;
        this.description = description;
    }

    public enum Levels {
        CRITICO(1L, "CRITICO", "Situações que exigem atenção imediata e extrema."),
        URGENTE(2L, "URGENTE", "Situações que requerem ação imediata."),
        ALTA(3L, "ALTA", "Situações que precisam ser resolvidas rapidamente."),
        MEDIA_ALTA(4L, "MEDIA_ALTA", "Situações que são importantes, mas não críticas."),
        MEDIA(5L, "MEDIA", "Situações que podem aguardar um pouco mais."),
        MEDIA_BAIXA(6L, "MEDIA_BAIXA", "Situações de importância moderada."),
        BAIXA(7L, "BAIXA", "Situações de menor importância."),
        MUITO_BAIXA(8L, "MUITO_BAIXA", "Situações que podem ser tratadas com baixa prioridade."),
        INFORMACIONAL(9L, "INFORMACIONAL", "Apenas para fins de registro, sem ação necessária.");

        private final Long id;
        private final String level;
        private final String description;

        Levels(Long id, String level, String description) {
            this.id = id;
            this.level = level;
            this.description = description;
        }

        public Priority toPriority() {
            return new Priority(id, level, description);
        }

        public String getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }

        public Long getId() {
            return id;
        }
    }
}
