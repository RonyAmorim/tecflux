package com.tecflux.config;

import com.tecflux.entity.Priority;
import com.tecflux.entity.Role;
import com.tecflux.entity.Status;
import com.tecflux.repository.PriorityRepository;
import com.tecflux.repository.RoleRepository;
import com.tecflux.repository.StatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;

    public DataLoader(RoleRepository roleRepository, PriorityRepository priorityRepository, StatusRepository statusRepository) {
        this.roleRepository = roleRepository;
        this.priorityRepository = priorityRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(Role.Values.values())
                .map(Role.Values::toRole)
                .forEach(role -> {
                    if (!roleRepository.existsByName(role.getName())) {
                        roleRepository.save(role);
                    }
                });

        Arrays.stream(Priority.Levels.values())
                .map(Priority.Levels::toPriority)
                .forEach(priority -> {
                    if (!priorityRepository.existsByLevel(priority.getLevel())) {
                        priorityRepository.save(priority);
                    }
                });

        Arrays.stream(Status.Values.values())
                .map(Status.Values::toStatus)
                .forEach(status -> {
                    if (!statusRepository.existsByName(status.getName())) {
                        statusRepository.save(status);
                    }
                });
    }
}
