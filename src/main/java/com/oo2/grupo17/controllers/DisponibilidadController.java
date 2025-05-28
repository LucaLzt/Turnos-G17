package com.oo2.grupo17.controllers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oo2.grupo17.services.implementation.DisponibilidadService;

import lombok.Getter;
import lombok.Setter;

@Controller
@RequestMapping("/disponibilidades")
public class DisponibilidadController {
	
	@Autowired
    private DisponibilidadService disponibilidadService;

    // Mostrar formulario HTML
    @GetMapping("/generar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("datosFormulario", new GenerarDisponibilidadForm());
        return "generar-disponibilidades";
    }

    // Procesar el formulario
    @PostMapping("/generar")
    public String generarDisponibilidades(
        @ModelAttribute("datosFormulario") GenerarDisponibilidadForm form,
        RedirectAttributes redirectAttributes
    ) {
        try {
            disponibilidadService.generarDisponibilidadesAutomaticas(
                form.getProfesionalId(),
                LocalTime.parse(form.getHoraInicio()),
                LocalTime.parse(form.getHoraFin()),
                Duration.ofMinutes(form.getDuracionMinutos()),
                LocalDate.parse(form.getFechaInicio()),
                LocalDate.parse(form.getFechaFin())
            );
            redirectAttributes.addFlashAttribute("mensaje", "¡Disponibilidades generadas con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/disponibilidades/generar";
    }

    // Clase para el formulario (DTO interno)
    @Getter @Setter
    public static class GenerarDisponibilidadForm {
        private Long profesionalId;
        private String horaInicio = "09:00";
        private String horaFin = "17:00";
        private int duracionMinutos = 30;
        private String fechaInicio;
        private String fechaFin;
    }

}
