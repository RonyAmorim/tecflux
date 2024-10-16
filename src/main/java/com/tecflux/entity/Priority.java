package com.tecflux.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    private List<Ticket> tickets = new ArrayList<>();

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
        CRITICO(1L, "URGENTE", "Situações que exigem atenção imediata e extrema."),
        ALTA(2L, "ALTA", "Situações que precisam ser resolvidas rapidamente."),
        MEDIA(3L, "MEDIA", "Situações que podem aguardar um pouco mais."),
        BAIXA(4L, "BAIXA", "Situações de menor importância.");

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
