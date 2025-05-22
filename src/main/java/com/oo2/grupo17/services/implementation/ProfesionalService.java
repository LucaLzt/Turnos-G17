package com.oo2.grupo17.services.implementation;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.ITareaRepository;
import com.oo2.grupo17.services.IProfesionalService;

@Service("profesionalService")
public class ProfesionalService 
	extends PersonaService<Profesional, ProfesionalDto> 
	implements IProfesionalService {
	
	private final IProfesionalRepository profesionalRepository;
	private final ITareaRepository tareaRepository;
	
	public ProfesionalService(IProfesionalRepository profesionalRepository,
							ITareaRepository tareaRepository,
							ModelMapper modelMapper) {
		super(profesionalRepository, modelMapper, ProfesionalDto.class);
		this.profesionalRepository = profesionalRepository;
		this.tareaRepository = tareaRepository;
	}
	
	@Override
	public Set<ProfesionalDto> findByTareaHabilitada(String nombreTarea) {
		return profesionalRepository.findByTareasHabilitadas_Nombre(nombreTarea).stream()
				.map(prof -> modelMapper.map(prof, ProfesionalDto.class))
				.collect(Collectors.toSet());
	}
	
	@Override
	public void asignarTareas(Long idProfesional, Set<Long> tareasIds) {
		Profesional profesional = profesionalRepository.findById(idProfesional).orElseThrow();
		profesional.setTareasHabilitadas(
				tareaRepository.findAllById(tareasIds).stream().collect(Collectors.toSet())		
		);
		profesionalRepository.save(profesional);
	}
	

}
