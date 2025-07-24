package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.dtos.ContactoDto;

public interface IClienteService {

	ClienteDto save(ClienteDto clienteDto);

    ClienteDto findById(Long id);
   
    List<ClienteDto> findAll();

    ClienteDto update(Long id, ClienteDto clienteDto);

    void deleteById(Long id);
    
    void registrarCliente(ClienteRegistroDto registroDto);
    
    ClienteDto findByEmail(String email);
    
    void updatearContactoUserEntity(ContactoDto contactoDto);

	void eliminarCuenta(String email, String password, int dni);

	void cambiarContrasena(ClienteDto cliente, CambioPasswordDto cambioPasswordDto);

	boolean tieneTurno(Long turnoId, Long clienteId);
	
	boolean cancelarTurno(Long turnoId, Long clienteId);
	
}
