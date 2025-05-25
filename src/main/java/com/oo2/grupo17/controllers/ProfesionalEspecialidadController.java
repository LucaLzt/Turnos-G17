package com.oo2.grupo17.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.ProfesionalEspecialidadesForm;
import com.oo2.grupo17.entities.Especialidad;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IEspecialidadRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;

@Controller
@RequestMapping("/profesionales")
public class ProfesionalEspecialidadController {

    @Autowired
    private IProfesionalRepository profesionalRepository;
    
    @Autowired
    private IEspecialidadRepository especialidadRepository;

    // Mostrar formulario de selección
    @GetMapping("/{id}/especialidades")
    public String mostrarFormularioEspecialidades(@PathVariable Long id, Model model) {
        Profesional profesional = profesionalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
        
        List<Especialidad> todasEspecialidades = especialidadRepository.findAllByOrderByNombreAsc();
        
        model.addAttribute("profesional", profesional);
        model.addAttribute("todasEspecialidades", todasEspecialidades);
        model.addAttribute("form", new ProfesionalEspecialidadesForm());
        
        return "especialidades-form";
    }

    // Procesar selección
    @PostMapping("/{id}/especialidades")
    public String guardarEspecialidades(
            @PathVariable Long id,
            @ModelAttribute ProfesionalEspecialidadesForm form) {
        
        Profesional profesional = profesionalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
        
        Set<Especialidad> especialidades = form.getEspecialidadesSeleccionadas().stream()
            .map(especialidadId -> especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada")))
            .collect(Collectors.toSet());
        
        profesional.setEspecialidadesHabilitadas(especialidades);
        profesionalRepository.save(profesional);
        
        return "redirect:/profesionales/" + id;
    }
    
    @GetMapping("/{id}")
    public String verProfesional(@PathVariable Long id, Model model) {
        Profesional profesional = profesionalRepository.findById(id)
            .orElseThrow();
        
        model.addAttribute("profesional", profesional);
        return "detalles";
    }
}
