package com.oo2.grupo17.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.IUserRepository;

import java.util.Set;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public AdminSeeder(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Primero crear el rol ADMIN si no existe
        RoleEntity adminRole = roleRepository.findByType(RoleType.ADMIN)
            .orElseGet(() -> roleRepository.save(
                RoleEntity.builder().type(RoleType.ADMIN).build()
            ));

        // Solo crear el usuario admin si no existe
        if (userRepository.findByUsername("admin@admin.com").isEmpty()) {
            UserEntity admin = UserEntity.builder()
                .username("admin@admin.com")
                .active(true)
                .password(encryptPassword("admin1234"))
                .roleEntities(Set.of(adminRole))
                .build();
            userRepository.save(admin);
        }
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
        return passwordEncoder.encode(password);
    }
}
