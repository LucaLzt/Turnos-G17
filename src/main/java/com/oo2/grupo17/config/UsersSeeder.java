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
public class UsersSeeder implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public UsersSeeder(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository.count() == 0){
            loadUserAdmin();
        }
    }
    
    private void loadUserAdmin() {
        userRepository.save(buildUserAdmin("admin1234@admin.com", "admin1234"));
    }
    
    private UserEntity buildUserAdmin(String username, String password) {
    	return UserEntity.builder()
    			.username(username)
    			.active(true)
    			.password(encryptPassword(password))
                .roleEntities(Set.of(roleRepository.findByType(RoleType.ADMIN).get()))
                .build();
    }
    
    private void loadRoles() {
        createRoleIfNotExists(RoleType.CLIENTE);
        createRoleIfNotExists(RoleType.ADMIN);
        createRoleIfNotExists(RoleType.PROFESIONAL);
    }
    
    // --- GENERO UN PROCEDIMIENTO PARA CREAR ROLES PASADOS POR PARÁMETROS SI NO EXISTEN --- //
    private void createRoleIfNotExists(RoleType roleType) {
        if (roleRepository.findByType(roleType).isEmpty()) {
            roleRepository.save(buildRole(roleType));
        }
    }
    
    private RoleEntity buildRole(RoleType roleType) {
        return RoleEntity.builder()
                .type(roleType)
                .build();
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
        return passwordEncoder.encode(password);
    }
}