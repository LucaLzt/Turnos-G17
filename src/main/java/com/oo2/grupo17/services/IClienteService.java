package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ClienteDto;

public interface IClienteService extends IPersonaService<ClienteDto>{
	
	List<ClienteDto> findByNroCliente(Integer nroCliente);
	
}
