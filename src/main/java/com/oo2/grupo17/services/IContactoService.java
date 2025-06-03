package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ContactoDto;

public interface IContactoService {
	
	ContactoDto save(ContactoDto contactoDto);
    
	ContactoDto findById(Long id);
    
    List<ContactoDto> findAll();

    ContactoDto update(Long id, ContactoDto contactoDto);

    void deleteById(Long id);
    
    ContactoDto findByEmail(String email);
	
}
