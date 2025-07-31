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

import java.util.List;
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
    
    // Métodos para carga de Roles
    private void loadRoles() {
        createRoleIfNotExists(RoleType.CLIENTE);
        createRoleIfNotExists(RoleType.ADMIN);
        createRoleIfNotExists(RoleType.PROFESIONAL);
    }
    
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

    private void loadUsers() {
        if (userRepository.count() == 0){
            loadUserAdmin();
            loadUserClientes();
            loadUserProfesionales();
        }
    }
    
    private void loadUserAdmin() {
        userRepository.save(buildUserAdmin("admin1234@admin.com", "admin1234"));
    }
    
    // Métodos de carga de usuarios Clientes
    private void loadUserClientes() {
    	List<ClienteRegistroDto> clientes = List.of(
    			buildCliente("cliente1@cliente.com", 11111111, "Cliente Uno", 1111111111L, 11111111, "cliente1"),
    			buildCliente("cliente2@cliente.com", 22222222, "Cliente Dos", 2222222222L, 22222222, "cliente2"),
    			buildCliente("cliente3@cliente.com", 33333333, "Cliente Tres", 3333333333L, 33333333, "cliente3")
    	);
    	for (ClienteRegistroDto cliente : clientes) {
			clienteService.registrarCliente(cliente);
		}
    }
    
    private ClienteRegistroDto buildCliente(String email, int dni, String nombre, long movil,
    		int telefono, String password) {
		ClienteRegistroDto clienteRegistro = new ClienteRegistroDto();
		clienteRegistro.setEmail(email);
		clienteRegistro.setDni(dni);
		clienteRegistro.setNombre(nombre);
		clienteRegistro.setMovil(movil);
		clienteRegistro.setTelefono(telefono);
		clienteRegistro.setPassword(password);
		return clienteRegistro;
	}
    
    // Métodos de carga de usuarios Profesionales
    private void loadUserProfesionales() {
		List<ProfesionalRegistradoDto> profesionales = List.of(
				buildProfesional("profesional1@profesional.com", 44444444, "Profesional Uno", 111111, 4444444444L, 44444444),
				buildProfesional("profesional2@profesional.com", 55555555, "Profesional Dos", 222222, 5555555555L, 55555555),
				buildProfesional("profesional3@profesional.com", 66666666, "Profesional Tres", 333333, 6666666666L, 66666666)
		);
		for (ProfesionalRegistradoDto profesional : profesionales) {
			profesionalService.registrarProfesional(profesional, profesional.getEmail().split("@")[0]);
		}
    }
    
    private ProfesionalRegistradoDto buildProfesional(String email, int dni, String nombre, int matricula,
    		long movil, int telefono) {
		ProfesionalRegistradoDto profesionalRegistro = new ProfesionalRegistradoDto();
		profesionalRegistro.setEmail(email);
		profesionalRegistro.setDni(dni);
		profesionalRegistro.setNombre(nombre);
		profesionalRegistro.setMatricula(matricula);
		profesionalRegistro.setMovil(movil);
		profesionalRegistro.setTelefono(telefono);
		return profesionalRegistro;
	}
    
    // Métodos de carga de usuario Admin
    private UserEntity buildUserAdmin(String username, String password) {
    	return UserEntity.builder()
    			.username(username)
    			.active(true)
    			.password(encryptPassword(password))
                .roleEntities(Set.of(roleRepository.findByType(RoleType.ADMIN).get()))
                .build();
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
        return passwordEncoder.encode(password);
    }
}