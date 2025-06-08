package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.dtos.ContactoDto;

public interface IClienteService {
	
    ClienteDto save(ClienteDto clienteDto);

    ClienteDto findById(Long id);
   
    List<ClienteDto> findAll();

    ClienteDto update(Long id, ClienteDto clienteDto);

    void deleteById(Long id);
    
    public void registrarCliente(ClienteRegistroDto registroDto);
    
    public ClienteDto findByEmail(String email);
    
    public void updatearContactoUserEntity(ContactoDto contactoDto);

	void eliminarCuenta(String email, String password, int dni);
	
}
