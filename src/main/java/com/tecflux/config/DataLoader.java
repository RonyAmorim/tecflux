package com.tecflux.config;

import com.tecflux.entity.Priority;
import com.tecflux.entity.Role;
import com.tecflux.repository.PriorityRepository;
import com.tecflux.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PriorityRepository priorityRepository;

    public DataLoader(RoleRepository roleRepository, PriorityRepository priorityRepository) {
        this.roleRepository = roleRepository;
        this.priorityRepository = priorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(Role.Values.values())
                .map(Role.Values::toRole)
                .forEach(roleRepository::save);

        Arrays.stream(Priority.Levels.values())
                .map(Priority.Levels::toPriority)
                .forEach(priorityRepository::save);

    }
}
