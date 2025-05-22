package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IClienteRepository;
import com.oo2.grupo17.services.IClienteService;

@Service("clienteService")
public class ClienteService 
	extends PersonaService<Cliente, ClienteDto>
	implements IClienteService {
	
	private final IClienteRepository clienteRepository;
	
	public ClienteService(IClienteRepository clienteRepository,
							ModelMapper modelMapper) {
		super(clienteRepository, modelMapper, ClienteDto.class);
		this.clienteRepository = clienteRepository;
	}
	
	@Override
	protected Class<Cliente> getEntityClass() {
		return Cliente.class;
	}
	
	@Override
	public List<ClienteDto> findByNroCliente(Integer nroCliente) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	public List<ClienteDto> findByNroCliente(Integer nroCliente) {
		return clienteRepository.findByNroCliente(nroCliente).stream()
				.map(cliente -> modelMapper.map(cliente, ClienteDto.class))
				.collect(Collectors.toList());
	}
	*/
	
}
