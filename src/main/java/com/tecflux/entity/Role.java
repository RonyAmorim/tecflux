package com.tecflux.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tb_role")
@Setter
@Getter
@NoArgsConstructor
public class Role {

    @Id
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public enum Values {
        ADMINISTRADOR(1L, "Administrador"),
        USUARIO(2L, "Usuario");

        private Long id;
        private String name;

        Values(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Role toRole(){
            return new Role(id, name);
        }
    }
}
