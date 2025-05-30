package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;
import com.oo2.grupo17.entities.UserEntity;
import com.oo2.grupo17.repositories.IClienteRepository;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IRoleRepository;
import com.oo2.grupo17.repositories.IUserRepository;
import com.oo2.grupo17.services.IClienteService;

import jakarta.transaction.Transactional;

@Service
public class ClienteService implements IClienteService {
	
	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final IContactoRepository contactoRepository;
	private final IClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

	public ClienteService(IUserRepository userRepository, IRoleRepository roleRepository,
			IContactoRepository contactoRepository, IClienteRepository clienteRepository,
			ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.contactoRepository = contactoRepository;
		this.clienteRepository = clienteRepository;
		this.modelMapper = modelMapper;
	}

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
				.orElseThrow();
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
				.orElseThrow();
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
	    // 1. Busco rol
	    RoleEntity clienteRole = roleRepository.findByType(RoleType.CLIENTE)
	        .orElseThrow(() -> new RuntimeException("Error: Rol CLIENTE no encontrado"));

	    // 2. Creo Cliente (sin contacto)
	    Cliente cliente = new Cliente();
	    cliente.setNombre(registroDto.getNombre());
	    cliente.setDni(registroDto.getDni());

	    // 3. Creo UserEntity y asocio Cliente
	    UserEntity user = new UserEntity();
	    user.setUsername(registroDto.getEmail());
	    user.setPassword(encryptPassword(registroDto.getPassword()));
	    user.setActive(true);
	    user.setRoleEntities(Set.of(clienteRole));
	    user.setCliente(cliente);
	    cliente.setUser(user);

	    // 4. Guardo User (esto persiste Cliente)
	    userRepository.save(user);

	    // 5. Creo Contacto y asocio Cliente
	    Contacto contacto = new Contacto();
	    contacto.setEmail(registroDto.getEmail());
	    contacto.setMovil(registroDto.getMovil());
	    contacto.setTelefono(registroDto.getTelefono());
	    contacto.setPersona(cliente);

	    // 6. Guardo Contacto
	    contactoRepository.save(contacto);

	    // 7. Asocio el contacto al cliente y updateo
	    cliente.setContacto(contacto);
	    clienteRepository.save(cliente);
	}
	
	// --- MÉTODO AUXILIAR PARA ENCRIPTAR CONTRASEÑAS --- //
    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
        return passwordEncoder.encode(password);
    }
	
}
