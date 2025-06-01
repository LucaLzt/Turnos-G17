package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Direccion;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IDireccionRepository;
import com.oo2.grupo17.repositories.ILocalidadRepository;
import com.oo2.grupo17.repositories.IProvinciaRepository;
import com.oo2.grupo17.services.IDireccionService;

@Service
public class DireccionService implements IDireccionService {
	
	private final IDireccionRepository direccionRepository;
	private final IContactoRepository contactoRepository;
	private final IProvinciaRepository provinciaRepository;
	private final ILocalidadRepository localidadRepository;
	private final ModelMapper modelMapper;

	public DireccionService(IDireccionRepository direccionRepository, IContactoRepository contactoRepository,
			IProvinciaRepository provinciaRepository, ILocalidadRepository localidadRepository,
			ModelMapper modelMapper) {
		this.direccionRepository = direccionRepository;
		this.contactoRepository = contactoRepository;
		this.provinciaRepository = provinciaRepository;
		this.localidadRepository = localidadRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public DireccionDto save(DireccionDto direccionDto) {
		Direccion direccion = modelMapper.map(direccionDto, Direccion.class);
		Direccion saved = direccionRepository.save(direccion);
		return modelMapper.map(saved, DireccionDto.class);
	}

	@Override
	public DireccionDto findById(Long id) {
		Direccion direccion = direccionRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(direccion, DireccionDto.class);
	}

	@Override
	public List<DireccionDto> findAll() {
		return direccionRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, DireccionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public DireccionDto update(Long id, DireccionDto direccionDto) {
		Direccion direccion = direccionRepository.findById(id)
				.orElseThrow();
		direccion.setAltura(direccionDto.getAltura());
		direccion.setCalle(direccionDto.getCalle());
		Direccion updated = direccionRepository.save(direccion);
		return modelMapper.map(updated, DireccionDto.class);
	}

	@Override
	public void deleteById(Long id) {
		direccionRepository.deleteById(id);
	}
	
	@Override
	public DireccionDto findByContactoEmail(String email) {
		Contacto contacto = contactoRepository.findByEmail(email)
				.orElseThrow();
		Direccion direccion = contacto.getDireccion();
		if(direccion == null) {
			return null;
		}
		return modelMapper.map(direccion, DireccionDto.class);
	}
	
	@Override
	public DireccionDto crearDireccion(ContactoDto contactoDto, DireccionDto direccionDto) {
		// 1. Crear y guardar la dirección
	    Direccion direccion = modelMapper.map(direccionDto, Direccion.class);
	    Long provinciaId = direccionDto.getProvinciaId();
	    Long localidadId = direccionDto.getLocalidadId();
	    direccion.setProvincia(provinciaRepository.findById(provinciaId).orElseThrow());
	    direccion.setLocalidad(localidadRepository.findById(localidadId).orElseThrow());
	    Direccion savedDireccion = direccionRepository.save(direccion);

	    // 2. Buscar el contacto real de la base
	    Contacto contactoEntity = contactoRepository.findById(contactoDto.getId())
	        .orElseThrow();

	    // 3. Asociar la dirección y guardo el contacto
	    contactoEntity.setDireccion(savedDireccion);
	    contactoRepository.save(contactoEntity);

	    return modelMapper.map(savedDireccion, DireccionDto.class);
	}
	
	@Override
	public DireccionDto actualizarDireccion(ContactoDto contactoDto, DireccionDto direccionDto) {
		// 1. Traigo la dirección existente de la base de datos
		Direccion direccion = direccionRepository.findById(direccionDto.getId())
				.orElseThrow();
		
		// 2. Actualizo los campos de la dirección
		direccion.setCalle(direccionDto.getCalle());
		direccion.setAltura(direccionDto.getAltura());
		Long provinciaId = direccionDto.getProvinciaId();
		Long localidadId = direccionDto.getLocalidadId();
		direccion.setLocalidad(localidadRepository.findById(localidadId).orElseThrow());
		direccion.setProvincia(provinciaRepository.findById(provinciaId).orElseThrow());
		
		// 3. Guardo la dirección actualizada
		Direccion updated = direccionRepository.save(direccion);
		
		// 4. Traigo el contacto existete de la base de datos
		Contacto contactoEntity = contactoRepository.findById(contactoDto.getId())
				.orElseThrow();
		
		// 5. Actualizo la dirección del contacto y guardo
		contactoEntity.setDireccion(updated);
		contactoRepository.save(contactoEntity);
		
		return modelMapper.map(updated, DireccionDto.class);
	}

}
