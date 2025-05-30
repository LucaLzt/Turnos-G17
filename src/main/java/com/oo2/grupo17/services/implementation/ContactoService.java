package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.services.IContactoService;

@Service
public class ContactoService implements IContactoService {
	
	private final IContactoRepository contactoRepository;
	private final ModelMapper modelMapper;
	
	public ContactoService(IContactoRepository contactoRepository, ModelMapper modelMapper) {
		this.contactoRepository = contactoRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ContactoDto save(ContactoDto contactoDto) {
		Contacto contacto = modelMapper.map(contactoDto, Contacto.class);
		Contacto saved = contactoRepository.save(contacto);
		return modelMapper.map(saved, ContactoDto.class);
	}

	@Override
	public ContactoDto findById(Long id) {
		Contacto contacto = contactoRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(contacto, ContactoDto.class);
	}

	@Override
	public List<ContactoDto> findAll() {
		return contactoRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ContactoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ContactoDto update(Long id, ContactoDto contactoDto) {
		Contacto contacto = contactoRepository.findById(id)
				.orElseThrow();
		contacto.setEmail(contactoDto.getEmail());
		contacto.setMovil(contactoDto.getMovil());
		contacto.setTelefono(contacto.getTelefono());
		Contacto updated = contactoRepository.save(contacto);
		return modelMapper.map(updated, ContactoDto.class);
	}

	@Override
	public void deleteById(Long id) {
		contactoRepository.deleteById(id);
	}

}
