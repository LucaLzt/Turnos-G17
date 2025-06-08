package com.oo2.grupo17.services.implementation;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.GenerarDisponibilidadDto;
import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Disponibilidad;
import com.oo2.grupo17.entities.Especialidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.exceptions.RolNoEncontradoException;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IDisponibilidadRepository;
import com.oo2.grupo17.repositories.IEspecialidadRepository;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.IServicioRepository;
import com.oo2.grupo17.repositories.ITurnoRepository;
import com.oo2.grupo17.repositories.IUserRepository;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IProfesionalService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Builder;


@Service @Builder
public class ProfesionalService implements IProfesionalService {
	
	private final IProfesionalRepository profesionalRepository;
	private final IDisponibilidadRepository disponibilidadRepository;
	private final IContactoRepository contactoRepository;
	private final IServicioRepository servicioRepository;
	private final ILugarRepository lugarRepository;
	private final IEspecialidadRepository especialidadRepository;
	private final IRoleRepository roleRepository;
	private final IUserRepository userRepository;
	private final IContactoService contactoService;
	private final EmailService emailService;
	private final ModelMapper modelMapper;
	private final ITurnoRepository turnoRepository;

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
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el profesional con ID: " + id));
		
		ProfesionalDto profesionalDto = modelMapper.map(profesional, ProfesionalDto.class);
		
		List<Long> serviciosIds = profesional.getServicios().stream().map(Servicio::getId).collect(Collectors.toList());
		
		profesionalDto.setServiciosIds(serviciosIds);
		
		return profesionalDto;
	}

	@Override
	public List<ProfesionalDto> findAll() {
	    return profesionalRepository.findAll()
	        .stream()
	        .map(object -> {
	            ProfesionalDto dto = modelMapper.map(object, ProfesionalDto.class);
	            // Suponiendo que object.getServicios() devuelve List<Servicio>
	            if (object.getServicios() != null) {
	                List<Long> serviciosIds = object.getServicios()
	                    .stream()
	                    .map(servicio -> servicio.getId())
	                    .collect(Collectors.toList());
	                dto.setServiciosIds(serviciosIds);
	            } else {
	                dto.setServiciosIds(Collections.emptyList());
	            }
	            return dto;
	        })
	        .collect(Collectors.toList());
	}

	@Override
	public ProfesionalDto update(Long id, ProfesionalDto profesionalDto) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el profesional con ID: " + id));
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
	public void eliminarProfesional(Long id) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el profesional con ID: " + id));
		
		// Elimino el profesional dentro de los servicios a los que esta relacionado
		for(Servicio servicio : new HashSet<>(profesional.getServicios())) {
			servicio.getProfesionales().remove(profesional);
			servicioRepository.save(servicio);
		}

		Set<Disponibilidad> d = profesional.getDisponibilidades();
		
		profesional.setDisponibilidades(null);
		for(Disponibilidad dispo : d) {
			disponibilidadRepository.delete(dispo);
		}
		
		 profesional.setContacto(null);
		 profesional.setUser(null);
		 profesionalRepository.save(profesional);

		 profesionalRepository.deleteById(id);
	}
	
	@Override @Transactional
	public void registrarProfesional(ProfesionalRegistradoDto registroDto) {
		
		// 1. Verifico que no exista ni el email ni el dni
		if (profesionalRepository.existsByContacto_Email(registroDto.getEmail())) {
			throw new RuntimeException("Ya existe un cliente con ese email.");
		}
		if (profesionalRepository.existsByDni(registroDto.getDni())) {
			throw new RuntimeException("Ya existe un cliente con ese DNI.");
		}
		
	    // 2. Busco rol
	    RoleEntity profesionalRole = roleRepository.findByType(RoleType.PROFESIONAL)
	        .orElseThrow(() -> new RolNoEncontradoException("No se encontró el rol: PROFESIONAL"));

	    // 3. Creo Cliente (sin contacto)
	    Profesional profesional = new Profesional();
	    profesional.setNombre(registroDto.getNombre());
	    profesional.setDni(registroDto.getDni());
	    profesional.setMatricula(registroDto.getMatricula());

	    // 4. Creo UserEntity y asocio Cliente
	    UserEntity user = new UserEntity();
	    user.setUsername(registroDto.getEmail());
	    String contraseñaGenerada = generarPasswordAleatoria();
	    //user.setPassword(encryptPassword(contraseñaGenerada));
	    user.setPassword(encryptPassword("1234")); // Para pruebas, usar una contraseña fija
	    user.setActive(true);
	    user.setRoleEntities(new HashSet<>(Set.of(profesionalRole)));
	    user.setProfesional(profesional);
	    profesional.setUser(user);

	    // 5. Guardo User (esto persiste Cliente)
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
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el profesional con ID: " + contactoDto.getId()));
		UserEntity usuario = profesional.getUser();
    	usuario.setUsername(contactoDto.getEmail());
    	userRepository.save(usuario);
	}
	
	@Override
	public void generarDisponibilidadesAutomaticas(GenerarDisponibilidadDto dto) {
        // 1. Buscar el profesional por ID
        Profesional profesional = profesionalRepository.findById(dto.getProfesionalId())
            .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el profesional con ID: " + dto.getProfesionalId()));

        // 2. Iterar sobre cada día del rango
        LocalDate fecha = dto.getFechaInicio();
        while (!fecha.isAfter(dto.getFechaFin())) {

            // 3. Para cada día, iterar sobre las horas disponibles
            LocalTime hora = dto.getHoraInicio();
            while (!hora.plusMinutes(dto.getDuracionMinutos()).isAfter(dto.getHoraFin())) {
                // 4. Crear y guardar la disponibilidad
                Disponibilidad disponibilidad = new Disponibilidad();
                disponibilidad.setProfesional(profesional);
                disponibilidad.setInicio(LocalDateTime.of(fecha, hora));
                disponibilidad.setDuracion(Duration.ofMinutes(dto.getDuracionMinutos()));
                disponibilidad.setOcupado(false);

                disponibilidadRepository.save(disponibilidad);

                // 5. Avanzar al siguiente turno
                hora = hora.plusMinutes(dto.getDuracionMinutos());
            }
            // 6. Avanzar al siguiente día
            fecha = fecha.plusDays(1);
        }
    }
	
	@Override
	public List<Profesional> obtenerProfesionalesPorLugar(Long lugarId){
		return profesionalRepository.findByLugar_id(lugarId);
	}
	
	@Override
	public void asignarDatosProfesional(Long id, ProfesionalDto profesionalDto, List<Long> serviciosIds) {
		Profesional profesional = profesionalRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado"));
		System.out.println(profesionalDto.getEspecialidad());
		System.out.println(profesionalDto.getEspecialidad());
		System.out.println(profesionalDto.getEspecialidad());
		// Asignar Especialidad
		if(profesionalDto.getEspecialidad() != null) {
			Especialidad especialidad = especialidadRepository.findById(profesionalDto.getEspecialidad().getId()).orElseThrow(()-> new EntityNotFoundException("Especialidad no encontrada"));
			profesional.setEspecialidad(especialidad);
		} else {
			profesional.setEspecialidad(null);
		}

		// Asignar Servicios
	   Set<Servicio> serviciosActuales = new HashSet<>(profesional.getServicios());

	   Set<Servicio> nuevosServicios;
	   if (serviciosIds != null && !serviciosIds.isEmpty()) {
	       nuevosServicios = new HashSet<>(servicioRepository.findAllById(serviciosIds));
	   } else {
	       nuevosServicios = new HashSet<>();
	   }

	     // Quitar profesional de los servicios que ya no corresponden
	   for (Servicio servicio : serviciosActuales) {
	       if (!nuevosServicios.contains(servicio)) {
	           servicio.getProfesionales().remove(profesional);
	           profesional.getServicios().remove(servicio);
	       }	    
	   }

	    // 2. Agregar profesional a los nuevos servicios seleccionados
	   for (Servicio servicio : nuevosServicios) {
	       servicio.getProfesionales().add(profesional);
	       profesional.getServicios().add(servicio);
	   }

	   profesional.setServicios(nuevosServicios);

		// Asignar Lugar
		if(profesionalDto.getLugar() != null) {
			Lugar lugar = lugarRepository.findById(profesionalDto.getLugar().getId()).orElseThrow(()-> new EntityNotFoundException("Lugar no encontrado"));
			profesional.setLugar(lugar);
		} else {
			profesional.setLugar(null);
		}

		profesionalRepository.save(profesional);
	}
	
}
