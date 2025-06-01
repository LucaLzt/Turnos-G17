package com.oo2.grupo17.services.implementation;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.IUserRepository;
import com.oo2.grupo17.services.IProfesionalService;

import jakarta.transaction.Transactional;
import lombok.Builder;

@Service @Builder
public class ProfesionalService implements IProfesionalService {
	
	private final IProfesionalRepository profesionalRepository;
	private final IContactoRepository contactoRepository;
	private final IRoleRepository roleRepository;
	private final IUserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public ProfesionalDto save(ProfesionalDto profesionalDto) {
		Profesional profesional = modelMapper.map(profesionalDto, Profesional.class);
		if(profesional.getContacto() == null) {
			profesional.setContacto(new Contacto());
		}
		Profesional saved = profesionalRepository.save(profesional);
		return modelMapper.map(saved, ProfesionalDto.class);
	}

	@Override
	public ProfesionalDto findById(Long id) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(profesional, ProfesionalDto.class);
	}

	@Override
	public List<ProfesionalDto> findAll() {
		return profesionalRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ProfesionalDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ProfesionalDto update(Long id, ProfesionalDto profesionalDto) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		profesional.setNombre(profesionalDto.getNombre());
		profesional.setDni(profesionalDto.getDni());
		profesional.setMatricula(profesionalDto.getMatricula());
		Profesional updated = profesionalRepository.save(profesional);
		return modelMapper.map(updated, ProfesionalDto.class);
	}

	@Override
	public void deleteById(Long id) {
		profesionalRepository.deleteById(id);
	}
	
	@Override @Transactional
	public void registrarProfesional(ProfesionalRegistradoDto registroDto) {
	    // 1. Busco rol
	    RoleEntity profesionalRole = roleRepository.findByType(RoleType.PROFESIONAL)
	        .orElseThrow(() -> new RuntimeException("Error: Rol CLIENTE no encontrado"));

	    // 2. Creo Cliente (sin contacto)
	    Profesional profesional = new Profesional();
	    profesional.setNombre(registroDto.getNombre());
	    profesional.setDni(registroDto.getDni());
	    profesional.setMatricula(registroDto.getMatricula());

	    // 3. Creo UserEntity y asocio Cliente
	    UserEntity user = new UserEntity();
	    user.setUsername(registroDto.getEmail());
	    user.setPassword(encryptPassword("12345678")); // Por el momento, contraseña fija
	    
	    // Cuando implementemos el envío de mails
	    // user.setPassword(encryptPassword(generarPasswordAleatoria())); <--- Y enviaríamos el mail con la contraseña
	    // Después el profesional podrá cambiarla en su perfil
	    
	    user.setActive(true);
	    user.setRoleEntities(Set.of(profesionalRole));
	    user.setProfesional(profesional);
	    profesional.setUser(user);

	    // 4. Guardo User (esto persiste Cliente)
	    userRepository.save(user);

	    // 6. Creo Contacto y asocio Cliente
	    Contacto contacto = new Contacto();
	    contacto.setEmail(registroDto.getEmail());
	    contacto.setMovil(registroDto.getMovil());
	    contacto.setTelefono(registroDto.getTelefono());
	    contacto.setPersona(profesional);

	    // 7. Guardo Contacto
	    contactoRepository.save(contacto);

	    // 8. Asocio el contacto al cliente y updateo
	    profesional.setContacto(contacto);
	    profesionalRepository.save(profesional);
	}
	
	// --- Método auxiliar para crear contraseña aleatoria --- //
	private String generarPasswordAleatoria() {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			password.append(caracteres.charAt(random.nextInt(caracteres.length())));
		}
		return password.toString();
	}
	
	// --- Método auxiliar para encriptar la contraseña --- //
	private String encryptPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
		return passwordEncoder.encode(password);
	}

}
