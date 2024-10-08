package com.tecflux.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tb_role")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "users")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getRoles().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }


    public enum Values {
        MASTER(1L, "ROLE_MASTER"),
        ADMINISTRADOR(2L, "ROLE_ADMINISTRADOR"),
        TECNICO(3L, "ROLE_TECNICO"),
        USUARIO(4L, "ROLE_USUARIO");

        private final Long id;
        private final String name;

        Values(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Role toRole() {
            return new Role(id, name);
        }

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }
    }
}
