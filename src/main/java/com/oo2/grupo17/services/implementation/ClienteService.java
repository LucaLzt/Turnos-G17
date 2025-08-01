package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.Turno;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.exceptions.ContraseñaIncorrectaException;
import com.oo2.grupo17.exceptions.DniIncorrectoException;
import com.oo2.grupo17.exceptions.EmailIncorrectoException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.exceptions.RolNoEncontradoException;
import com.oo2.grupo17.repositories.IClienteRepository;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.ITurnoRepository;
import com.oo2.grupo17.repositories.IUserRepository;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;

import jakarta.transaction.Transactional;
import lombok.Builder;

@Service @Builder
public class ClienteService implements IClienteService {
	
	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final ITurnoRepository turnoRepository;
	private final IContactoRepository contactoRepository;
	private final IClienteRepository clienteRepository;
	private final IProfesionalRepository profesionalRepository;
	private final IContactoService contactoService;
	private final DisponibilidadService disponibilidadService;
	private final BCryptPasswordEncoder encoder;
    private final ModelMapper modelMapper;

	@Override
	public ClienteDto save(ClienteDto clienteDto) {
		Cliente cliente = modelMapper.map(clienteDto, Cliente.class);
		if(cliente.getContacto() == null) {
			cliente.setContacto(new Contacto());
		}
		Cliente saved = clienteRepository.save(cliente);
		return modelMapper.map(saved, ClienteDto.class);
	}

	@Override
	public ClienteDto findById(Long id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con ID: " + id));
		return modelMapper.map(cliente, ClienteDto.class);
	}

	@Override
	public List<ClienteDto> findAll() {
		return clienteRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ClienteDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClienteDto update(Long id, ClienteDto clienteDto) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con ID: " + id));
		cliente.setNombre(clienteDto.getNombre());
		cliente.setDni(clienteDto.getDni());
		Cliente updated = clienteRepository.save(cliente);
		return modelMapper.map(updated, ClienteDto.class);
	}

	@Override
	public void deleteById(Long id) {
		clienteRepository.deleteById(id);
	}
	
	@Override @Transactional
	public void registrarCliente(ClienteRegistroDto registroDto) {
		
		// 1. Verifico que no exista ni el email ni el dni
		if (clienteRepository.existsByContacto_Email(registroDto.getEmail())) {
	        throw new RuntimeException("Ya existe un cliente con ese email.");
	    }
	    if (clienteRepository.existsByDni(registroDto.getDni())) {
	        throw new RuntimeException("Ya existe un cliente con ese DNI.");
	    }
		
	    // 2. Busco rol
	    RoleEntity clienteRole = roleRepository.findByType(RoleType.CLIENTE)
	        .orElseThrow(() -> new RolNoEncontradoException("No se encontró el rol: CLIENTE"));

	    // 3. Creo Cliente (sin contacto)
	    Cliente cliente = new Cliente();
	    cliente.setNombre(registroDto.getNombre());
	    cliente.setDni(registroDto.getDni());

	    // 4. Creo UserEntity y asocio Cliente
	    UserEntity user = new UserEntity();
	    user.setUsername(registroDto.getEmail());
	    user.setPassword(encryptPassword(registroDto.getPassword()));
	    user.setActive(true);
	    user.setRoleEntities(Set.of(clienteRole));
	    user.setCliente(cliente);
	    cliente.setUser(user);

	    // 5. Guardo User (esto persiste Cliente)
	    userRepository.save(user);
	    
	    // 6. Genero el numero de cliente en base al id del cliente
	    cliente.setNroCliente(String.format("%06d", cliente.getId()));

	    // 7. Creo Contacto y asocio Cliente
	    Contacto contacto = new Contacto();
	    contacto.setEmail(registroDto.getEmail());
	    contacto.setMovil(registroDto.getMovil());
	    contacto.setTelefono(registroDto.getTelefono());
	    contacto.setPersona(cliente);

	    // 8. Guardo Contacto
	    contactoRepository.save(contacto);

	    // 9. Asocio el contacto al cliente y updateo
	    cliente.setContacto(contacto);
	    clienteRepository.save(cliente);
	}
	
	@Override
	public ClienteDto findByEmail(String email) {
		Cliente cliente = clienteRepository.findByEmail(email)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con Email: " + email));
		return modelMapper.map(cliente, ClienteDto.class);
	}
	
	@Override 
	public void updatearContactoUserEntity(ContactoDto contactoDto) {
		contactoService.update(contactoDto.getId(), contactoDto);
		Cliente cliente = clienteRepository.findById(contactoDto.getId())
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con ID: " + contactoDto.getId()));
		UserEntity usuario = cliente.getUser();
    	usuario.setUsername(contactoDto.getEmail());
    	userRepository.save(usuario);
	}
	
	@Override @Transactional
	public void eliminarCuenta(String email, String password, int dni) {
		
		// 1. Busco el cliente, el contacto y el usuario desde la base de datos
		Cliente cliente = clienteRepository.findByEmail(email)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con Email: " + email));
		Contacto contacto = contactoRepository.findByEmail(email)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el contacto con Email: " + email));
		UserEntity user = userRepository.findByUsername(email)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el user con Email: " + email));
		
		// 2. Valido los datos con los encontrados
		if(cliente.getDni() != dni) {
			throw new DniIncorrectoException("ERROR: Dni incorrecto");
		}
		if(!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
			throw new ContraseñaIncorrectaException("ERROR: Contraseña incorrecta");
		}
		if(!contacto.getEmail().equals(email)) {
			throw new EmailIncorrectoException("ERROR: Email incorrecto");
		}
		
		// 3. Desvinculo al contacto de cliente (Persona)
		cliente.setContacto(null);
		clienteRepository.save(cliente);
		
		// 3. Elimino el contacto, el cliente y el usuario
		contactoRepository.delete(contacto);
		clienteRepository.delete(cliente);
		userRepository.delete(user);
		
	};
	
	@Override
	public void cambiarContrasena(ClienteDto cliente, CambioPasswordDto cambioPasswordDto) {
		// Obtener el profesional desde la base de datos (por id o email)
	    Cliente clienteEntity = clienteRepository.findById(cliente.getId())
	            .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el Cliente"));
	    
	    UserEntity userEntity = clienteEntity.getUser();

	    // Validar contraseña actual
	    if (!encoder.matches(cambioPasswordDto.getPasswordActual(), userEntity.getPassword())) {
	        throw new ContraseñaIncorrectaException("La contraseña actual es incorrecta");
	    }

	    // Validar que la nueva y la repetida sean iguales
	    if (!cambioPasswordDto.getPasswordNueva().equals(cambioPasswordDto.getPasswordNuevaRepetida())) {
	        throw new ContraseñaIncorrectaException("Las nuevas contraseñas no coinciden");
	    }

	    // Encriptar y guardar la nueva contraseña
	    userEntity.setPassword(encoder.encode(cambioPasswordDto.getPasswordNueva()));
	    userRepository.save(userEntity);
	};
	
	// --- Método auxiliar para encriptar la contraseña --- //
	private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
        return passwordEncoder.encode(password);
    }

	@Override
	public boolean tieneTurno(Long turnoId, Long clienteId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con ID: " + clienteId));
		return turnoRepository.existsByIdAndClienteId(turnoId, cliente.getId());
	}

	@Override
	public boolean cancelarTurno(Long turnoId, Long clienteId) {
		if (!tieneTurno(turnoId, clienteId)) {
			throw new EntidadNoEncontradaException("El cliente no tiene un turno con ID: " + turnoId);
		}
		disponibilidadService.updateOcupacionByTurnoId(turnoId);
		
		Turno turno = turnoRepository.findById(turnoId)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el turno con ID: " + turnoId));
		
		Profesional profesional = turno.getProfesional();
		profesional.getLstTurnos().remove(turno);
		profesionalRepository.save(profesional);
		
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el cliente con ID: " + clienteId));
		cliente.getLstTurnos().remove(turno);
		clienteRepository.save(cliente);
		
		turnoRepository.deleteById(turnoId);
		return true;
	}
	
}
