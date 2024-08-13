package com.tecflux.config;

import com.tecflux.entity.Role;
import com.tecflux.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(Role.Values.values())
                .map(Role.Values::toRole)
                .forEach(roleRepository::save);
    }
}
