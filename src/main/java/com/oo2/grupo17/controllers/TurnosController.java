package com.oo2.grupo17.controllers;
import java.security.Principal;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Persona;
import com.oo2.grupo17.entities.Turno;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProvinciaService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;
import com.oo2.grupo17.services.implementation.ClienteService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/turnos")
@PreAuthorize("hasRole('ROLE_CLIENTE')")

public class TurnosController{
	private final ITurnoService turnoService;
	
	@Autowired
	private ClienteService clienteService;

    public TurnosController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }
    

    @GetMapping("/detalle/{id}")
    public String detalleTurno(@PathVariable("id") long id, Model model) {
        TurnoDto turno = turnoService.findById(id);
        model.addAttribute("turno", turno);
        return "turnos/DetalleTurno";
    }
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/lista")
    public String cantidadTurnosCliente(Model model, Principal principal) {
    	String email = principal.getName();
    	ClienteDto cliente = clienteService.findByEmail(email);
        Long clienteId = cliente.getId();

        List<Turno> turnos = turnoService.buscarTurnosPorClienteId(clienteId);
        model.addAttribute("turnos", turnos);

        return "turnos/ListaTurnos";
    }
    
    @GetMapping("/cancelar")
    public String cancelarTurnosCliente(Model model, Principal principal) {
    	String email = principal.getName();
    	ClienteDto cliente = clienteService.findByEmail(email);
        Long clienteId = cliente.getId();

        List<Turno> turnos = turnoService.buscarTurnosPorClienteId(clienteId);
        model.addAttribute("turnos", turnos);

        return "turnos/TurnosACancelar";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return "redirect:/turnos/lista"; 
    }
    
    @GetMapping("/menu")
    public String menuTurnos() {
        return "turnos/Menu"; 
    }
}