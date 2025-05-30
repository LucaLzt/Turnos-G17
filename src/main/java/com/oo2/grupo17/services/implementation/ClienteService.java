package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IClienteRepository;
import com.oo2.grupo17.services.IClienteService;

@Service
public class ClienteService implements IClienteService {
	
	private final IClienteRepository clienteRepository;
    private final ModelMapper modelMapper;
	
	public ClienteService(IClienteRepository clienteRepository, ModelMapper modelMapper) {
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
	
}
