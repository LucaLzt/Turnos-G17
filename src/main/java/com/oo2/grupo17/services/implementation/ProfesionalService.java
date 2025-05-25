package com.oo2.grupo17.services.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.entities.Especialidad;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.IEspecialidadRepository;
import com.oo2.grupo17.services.IProfesionalService;

@Service
public class ProfesionalService implements IProfesionalService {
	
	private final IProfesionalRepository profesionalRepository;
	private final IEspecialidadRepository especialidadRepository;
	private final ModelMapper modelMapper;
	
	public ProfesionalService(IProfesionalRepository profesionalRepository,
							IEspecialidadRepository especialidadRepository,
							ModelMapper modelMapper) {
		this.profesionalRepository = profesionalRepository;
		this.especialidadRepository = especialidadRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ProfesionalDto save(ProfesionalDto profesionalDto) {
		Profesional profesional = modelMapper.map(profesionalDto, Profesional.class);
		if(profesional.getContacto() == null) {
			profesional.setContacto(new Contacto());
		}
		if(profesional.getEspecialidadesHabilitadas() == null) {
			profesional.setEspecialidadesHabilitadas(new HashSet<>());
		}
		Profesional saved = profesionalRepository.save(profesional);
		return modelMapper.map(saved, ProfesionalDto.class);
	}

	@Override
	public ProfesionalDto findById(Long id) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(profesional, ProfesionalDto.class);
	}

	@Override
	public List<ProfesionalDto> findAll() {
		return profesionalRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ProfesionalDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ProfesionalDto update(Long id, ProfesionalDto profesionalDto) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		profesional.setNombre(profesionalDto.getNombre());
		profesional.setDni(profesionalDto.getDni());
		profesional.setMatricula(profesionalDto.getMatricula());
		Profesional updated = profesionalRepository.save(profesional);
		return modelMapper.map(updated, ProfesionalDto.class);
	}

	@Override
	public void deleteById(Long id) {
		profesionalRepository.deleteById(id);
	}
	
	@Override
	public ProfesionalDto asociarEspecialidadId(Long especialidadId, ProfesionalDto profesionalDto) {
		Profesional profesional = modelMapper.map(profesionalDto, Profesional.class);
		Especialidad tarea = especialidadRepository.findById(especialidadId)
				.orElseThrow();
		profesional.getEspecialidadesHabilitadas().add(tarea);
		profesionalRepository.save(profesional);
		Profesional updated = profesionalRepository.save(profesional);
		return modelMapper.map(updated, ProfesionalDto.class);
	}
	
	@Override
	public ProfesionalDto asociarEspecialidadNombre(String especialidadNombre, ProfesionalDto profesionalDto) {
		Profesional profesional = modelMapper.map(profesionalDto, Profesional.class);
		Especialidad tarea = especialidadRepository.findByNombreIgnoreCase(especialidadNombre)
				.orElseThrow();
		profesional.getEspecialidadesHabilitadas().add(tarea);
		Profesional updated = profesionalRepository.save(profesional);
		return modelMapper.map(updated, ProfesionalDto.class);
	}

}
