package com.oo2.grupo17.services.implementation;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Especialidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IEspecialidadRepository;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.IServicioRepository;
import com.oo2.grupo17.repositories.IUserRepository;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IProfesionalService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Builder;


@Service @Builder
public class ProfesionalService implements IProfesionalService {
	
	private final IProfesionalRepository profesionalRepository;
	private final IContactoRepository contactoRepository;
	private final IServicioRepository servicioRepository;
	private final ILugarRepository lugarRepository;
	private final IEspecialidadRepository especialidadRepository;
	private final IRoleRepository roleRepository;
	private final IUserRepository userRepository;
	private final IContactoService contactoService;
	private final EmailService emailService;
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
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		
		// Elimino el profesional dentro de los servicios a los que esta relacionado
		for(Servicio servicio : new HashSet<>(profesional.getServicios())) {
			servicio.getProfesionales().remove(profesional);
			servicioRepository.save(servicio);
		}
		
		contactoRepository.deleteById(id);
		profesionalRepository.deleteById(id);
	}
	
	@Override @Transactional
	public void registrarProfesional(ProfesionalRegistradoDto registroDto) {
	    // 1. Busco rol
	    RoleEntity profesionalRole = roleRepository.findByType(RoleType.PROFESIONAL)
	        .orElseThrow();

	    // 2. Creo Cliente (sin contacto)
	    Profesional profesional = new Profesional();
	    profesional.setNombre(registroDto.getNombre());
	    profesional.setDni(registroDto.getDni());
	    profesional.setMatricula(registroDto.getMatricula());

	    // 3. Creo UserEntity y asocio Cliente
	    UserEntity user = new UserEntity();
	    user.setUsername(registroDto.getEmail());
	    String contraseñaGenerada = generarPasswordAleatoria();
	    //user.setPassword(encryptPassword(contraseñaGenerada));
	    user.setPassword(encryptPassword("1234")); // Para pruebas, usar una contraseña fija
	    user.setActive(true);
	    user.setRoleEntities(new HashSet<>(Set.of(profesionalRole)));
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
	    
	    // 9. Envío correo al profesional con la contraseña generada
	    // emailService.enviarEmailRegistro(registroDto.getEmail(), registroDto.getNombre(), contraseñaGenerada);
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
	
	public void asignarDatosProfesional(Long id, Long especialidadId, Long lugarId, Set<Long> serviciosId) {
		Profesional profesional = profesionalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado"));
		
		// Asignar Especialidad
		if(especialidadId != null) {
			Especialidad especialidad = especialidadRepository.findById(especialidadId).orElseThrow(()-> new EntityNotFoundException("Especialidad no encontrada"));
			profesional.setEspecialidad(especialidad);
		} else {
			profesional.setEspecialidad(null);
		}
		
		// Asignar Servicios
		if(serviciosId != null && !serviciosId.isEmpty()) {
			Set<Servicio> servicios = new HashSet<>(servicioRepository.findAllById(serviciosId));
			// Como servicio es la clase fuerte en la relacion ManyToMany, le agrego el profesional a cada servicio seleccionado 
			for(Servicio servicio : servicios) {
				servicio.getProfesionales().add(profesional);
				servicioRepository.save(servicio);
			}
			profesional.setServicios(servicios);
		} else {
			profesional.setServicios(Collections.emptySet());
		}
		
		// Asignar Lugar
		if(lugarId != null) {
			Lugar lugar = lugarRepository.findById(lugarId).orElseThrow(()-> new EntityNotFoundException("Lugar no encontrado"));
			profesional.setLugar(lugar);
		} else {
			profesional.setLugar(null);
		}
		
		profesionalRepository.save(profesional);
	}
	
	@Override
	public ProfesionalDto findByEmail(String email) {
		Profesional profesional = profesionalRepository.findByEmail(email)
				.orElseThrow();
		return modelMapper.map(profesional, ProfesionalDto.class);
	}
	
	@Override 
	public void updatearContactoUserEntity(ContactoDto contactoDto) {
		contactoService.update(contactoDto.getId(), contactoDto);
		Profesional profesional = profesionalRepository.findById(contactoDto.getId())
				.orElseThrow();
		UserEntity usuario = profesional.getUser();
    	usuario.setUsername(contactoDto.getEmail());
    	userRepository.save(usuario);
	}
	
}
