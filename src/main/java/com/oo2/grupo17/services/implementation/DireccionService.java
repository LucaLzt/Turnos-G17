package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Direccion;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.repositories.IContactoRepository;
import com.oo2.grupo17.repositories.IDireccionRepository;
import com.oo2.grupo17.repositories.ILocalidadRepository;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.repositories.IProvinciaRepository;
import com.oo2.grupo17.services.IDireccionService;

import jakarta.transaction.Transactional;
import lombok.Builder;

@Service @Builder
public class DireccionService implements IDireccionService {
	
	private final IDireccionRepository direccionRepository;
	private final IContactoRepository contactoRepository;
	private final IProvinciaRepository provinciaRepository;
	private final ILocalidadRepository localidadRepository;
	private final ILugarRepository lugarRepository;
	private final ModelMapper modelMapper;

	@Override
	public DireccionDto save(DireccionDto direccionDto) {
		Direccion direccion = modelMapper.map(direccionDto, Direccion.class);
		Direccion saved = direccionRepository.save(direccion);
		return modelMapper.map(saved, DireccionDto.class);
	}

	@Override
	public DireccionDto findById(Long id) {
		Direccion direccion = direccionRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el direccion con ID: " + id));
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
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el direccion con ID: " + id));
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
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el contacto con Email: " + email));
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
	        .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el contacto con ID: " + contactoDto.getId()));

	    // 3. Asociar la dirección y guardo el contacto
	    contactoEntity.setDireccion(savedDireccion);
	    contactoRepository.save(contactoEntity);

	    return modelMapper.map(savedDireccion, DireccionDto.class);
	}
	
	@Override
	public DireccionDto actualizarDireccion(ContactoDto contactoDto, DireccionDto direccionDto) {
		// 1. Traigo la dirección existente de la base de datos
		Direccion direccion = direccionRepository.findById(direccionDto.getId())
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el direccion con ID: " + direccionDto.getId()));
		
		// 2. Actualizo los campos de la dirección
		direccion.setCalle(direccionDto.getCalle());
		direccion.setAltura(direccionDto.getAltura());
		Long provinciaId = direccionDto.getProvinciaId();
		Long localidadId = direccionDto.getLocalidadId();
		direccion.setLocalidad(localidadRepository.findById(localidadId).orElseThrow());
		direccion.setProvincia(provinciaRepository.findById(provinciaId).orElseThrow());
		
		// 3. Guardo la dirección actualizada
		Direccion updated = direccionRepository.save(direccion);
		
		// 4. Traigo el contacto existente de la base de datos
		Contacto contactoEntity = contactoRepository.findById(contactoDto.getId())
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el contacto con ID: " + contactoDto.getId()));
		
		// 5. Actualizo la dirección del contacto y guardo
		contactoEntity.setDireccion(updated);
		contactoRepository.save(contactoEntity);
		
		return modelMapper.map(updated, DireccionDto.class);
	}

	@Override @Transactional
	public DireccionDto crearDireccion(LugarDto lugarDto, DireccionDto direccionDto) {
		// 1. Crear y guardar la dirección
	    Direccion direccion = modelMapper.map(direccionDto, Direccion.class);
	    Long provinciaId = direccionDto.getProvinciaId();
	    Long localidadId = direccionDto.getLocalidadId();
	    direccion.setProvincia(provinciaRepository.findById(provinciaId).orElseThrow());
	    direccion.setLocalidad(localidadRepository.findById(localidadId).orElseThrow());
	    Direccion savedDireccion = direccionRepository.save(direccion);
	    
	    // 2. Buscar el lugar real de la base
	    Lugar lugarEntity;
	    if(lugarDto.getId() == null) {								// Si el lugar no existe, lo creo
	    	lugarEntity = modelMapper.map(lugarDto, Lugar.class);
	    	lugarEntity.setDireccion(savedDireccion);
	    	
	    	// Verifico si ya existe un lugar con la misma dirección
	    	if (lugarRepository.existsByDireccion_CalleAndDireccion_AlturaAndDireccion_LocalidadAndDireccion_Provincia(
			        lugarDto.getDireccion().getCalle(),
			        lugarDto.getDireccion().getAltura(),
			        savedDireccion.getLocalidad(), savedDireccion.getProvincia())) {
				throw new EntidadDuplicadaException("El lugar con la dirección ingresada ya existe.");
			}
	    	
	    	lugarRepository.save(lugarEntity);
	    } else {                       								// Si el lugar ya existe, lo actualizo
	    	lugarEntity = lugarRepository.findById(lugarDto.getId())
	    			.orElseThrow();
		    lugarEntity.setDireccion(savedDireccion);
		    
	    	// Verifico si ya existe un lugar con la misma dirección
	    	if (lugarRepository.existsByDireccion_CalleAndDireccion_AlturaAndDireccion_LocalidadAndDireccion_Provincia(
			        lugarDto.getDireccion().getCalle(),
			        lugarDto.getDireccion().getAltura(),
			        savedDireccion.getLocalidad(), savedDireccion.getProvincia())) {
				throw new EntidadDuplicadaException("El lugar con la dirección ingresada ya existe.");
			}
		    
		    lugarRepository.save(lugarEntity);
	    }

	    return modelMapper.map(savedDireccion, DireccionDto.class);
	}

	@Override @Transactional
	public DireccionDto actualizarDireccion(LugarDto lugarDto, DireccionDto direccionDto) {
	    // 1. Buscar el lugar existente
	    Lugar lugarEntity = lugarRepository.findById(lugarDto.getId())
	            .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el lugar con ID: " + lugarDto.getId()));
	    
	    // 2. Actualizo los campos del lugar
	    lugarEntity.setHorarioApertura(lugarDto.getHorarioApertura());
	    lugarEntity.setHorarioCierre(lugarDto.getHorarioCierre());
	    
	    // 3. Obtener la dirección asociada al lugar
	    Direccion direccion = lugarEntity.getDireccion();

	    // 4. Verificar si ya existe un lugar con la misma dirección
	    if (lugarRepository.existsByDireccion_CalleAndDireccion_AlturaAndDireccion_LocalidadAndDireccion_Provincia(
	            direccionDto.getCalle(),
	            direccionDto.getAltura(),
	            localidadRepository.findById(direccionDto.getLocalidadId())
	            	.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la localidad con ID: " + direccionDto.getLocalidadId())), 
	            provinciaRepository.findById(direccionDto.getProvinciaId())
	            	.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la provincia con ID: " + direccionDto.getProvinciaId())))) {
	        throw new EntidadDuplicadaException("El lugar con la dirección ingresada ya existe.");
	    }
	    
	    // 5. Actualizar los campos de la dirección
	    direccion.setCalle(direccionDto.getCalle());
	    direccion.setAltura(direccionDto.getAltura());
	    direccion.setLocalidad(localidadRepository.findById(direccionDto.getLocalidadId())
	    		.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la localidad con ID: " + direccionDto.getLocalidadId())));
	    direccion.setProvincia(provinciaRepository.findById(direccionDto.getProvinciaId())
	    		.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la provincia con ID: " + direccionDto.getProvinciaId())));


	    // 6. Guardar la dirección y luego el lugar
	    direccionRepository.save(direccion);
	    lugarRepository.save(lugarEntity);

	    return modelMapper.map(direccion, DireccionDto.class);
	}

}
