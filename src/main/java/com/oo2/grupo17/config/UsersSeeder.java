package com.oo2.grupo17.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.IUserRepository;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IProfesionalService;

import lombok.Builder;

import java.util.Set;

@Component @Builder
public class UsersSeeder implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IClienteService clienteService;
    private final IProfesionalService profesionalService;

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadUsers();
    }

    private void loadUsers() {
        if (userRepository.count() == 0){
            loadUserAdmin();
            loadUserCliente();
            loadUserProfesional();
        }
    }
    
    private void loadUserAdmin() {
        userRepository.save(buildUserAdmin("admin1234@admin.com", "admin1234"));
    }
    
    private void loadUserCliente() {
        ClienteRegistroDto clienteRegistro = new ClienteRegistroDto();
        clienteRegistro.setEmail("cliente1234@cliente.com");
        clienteRegistro.setDni(12345678);
        clienteRegistro.setNombre("Cliente");
        clienteRegistro.setMovil(1234567890);
        clienteRegistro.setTelefono(12345678);
        clienteRegistro.setPassword("cliente1234");
        clienteService.registrarCliente(clienteRegistro);
    }
    
    private void loadUserProfesional() {
    	ProfesionalRegistradoDto profesionalRegistro = new ProfesionalRegistradoDto();
    	profesionalRegistro.setEmail("profesional1234@profesional.com");
    	profesionalRegistro.setDni(87654321);
    	profesionalRegistro.setNombre("Profesional");
    	profesionalRegistro.setMatricula(000001);
    	profesionalRegistro.setMovil(1234567890);
    	profesionalRegistro.setTelefono(87654321);
    	profesionalService.registrarProfesional(profesionalRegistro, "profesional1234");
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