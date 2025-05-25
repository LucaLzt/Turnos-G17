package com.oo2.grupo17.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IClienteRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;

@Controller
@RequestMapping("/personas")
public class ClienteProfesionalController {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IProfesionalRepository profesionalRepository;

    // Mostrar formulario HTML
    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("profesional", new Profesional());
        return "formulario";
    }

    // Guardar Cliente
    @PostMapping("/guardar-cliente")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteRepository.save(cliente);
        return "redirect:/personas/formulario";
    }

    // Guardar Profesional
    @PostMapping("/guardar-profesional")
    public String guardarProfesional(@ModelAttribute Profesional profesional) {
        profesionalRepository.save(profesional);
        return "redirect:/personas/formulario";
    }

    // Listar todos
    @GetMapping("/listar")
    public String listarPersonas(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("profesionales", profesionalRepository.findAll());
        return "lista";
    }
}
