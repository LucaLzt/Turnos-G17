package com.oo2.grupo17.controllers;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo17.dtos.*;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.*;

import lombok.Builder;

@Controller
@Builder
@SessionAttributes("turno")
@RequestMapping("/cliente")
public class TurnosController {

    private final IServicioService servicioService;
    private final IDisponibilidadService disponibilidadService;
    private final IEmailService emailService;
    private final ILugarService lugarService;
    private final IClienteService clienteService;
    private final IProvinciaService provinciaService;
    private final ILocalidadService localidadService;
    private final IProfesionalService profesionalService;
    private final ITurnoService turnoService;
    private final ModelMapper modelMapper;

    // Mantener turno entre requests
    @ModelAttribute("turno")
    public TurnoDto inicializarTurno() {
        return new TurnoDto();
    }

    // Paso 1: Elegir servicio
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/solicitar-turno")
    public String solicitarTurnos(Model model, Principal principal, @ModelAttribute("turno") TurnoDto turno) {
        String email = principal.getName();
        ClienteDto cliente = clienteService.findByEmail(email);
        if (cliente != null) {
            turno.setCliente(cliente);
        }
        List<ServicioDto> servicios = servicioService.findAllByOrderByNombreAsc();
        model.addAttribute("servicios", servicios);
        model.addAttribute("turno", turno);
        return ViewRouteHelper.TURNO_SOLICITUD_ELEGIR;
    }

    // Paso 2: Elegir lugar por servicio
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/solicitar-turno/{servicioId}/elegir-lugar")
    public String elegirLugar(@PathVariable Long servicioId, Model model, @ModelAttribute("turno") TurnoDto turno) {
        ServicioDto servicio = servicioService.findById(servicioId);
        List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(servicioId);

        turno.setServicio(servicio);

        model.addAttribute("servicio", servicio);
        model.addAttribute("lugares", lugares);
        model.addAttribute("turno", turno);
        return ViewRouteHelper.TURNO_SOLICITUD_ELEGIR_LUGAR;
    }

    // Paso 3: Elegir profesional por lugar
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/solicitar-turno/{lugarId}/elegir-profesional")
    public String elegirProfesional(@PathVariable Long lugarId, Model model, @ModelAttribute("turno") TurnoDto turno) {
        LugarDto lugar = lugarService.findById(lugarId);
        DireccionDto direccion = lugar.getDireccion();
        ProvinciaDto provincia = provinciaService.findById(direccion.getProvinciaId());
        LocalidadDto localidad = localidadService.findById(direccion.getLocalidadId());
        List<Profesional> profesionales = profesionalService.obtenerProfesionalesPorLugar(lugarId);

        turno.setLugar(modelMapper.map(lugar, LugarDto.class));
        
        model.addAttribute("lugar", lugar);
        model.addAttribute("provincia", provincia);
        model.addAttribute("localidad", localidad);
        model.addAttribute("profesionales", profesionales);
        model.addAttribute("turno", turno);
        return ViewRouteHelper.TURNO_SOLICITUD_ELEGIR_PROFESIONAL;
    }

    // Paso 4: Confirmar profesional y mostrar disponibilidades
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/solicitar-turno/{lugarId}/elegir-profesional/{profesionalId}/elegir-horario")
    public String elegirHorario(@PathVariable Long lugarId,
                                @PathVariable Long profesionalId,
                                Model model,
                                @ModelAttribute("turno") TurnoDto turno) {
        LugarDto lugar = lugarService.findById(lugarId);
        DireccionDto direccion = lugar.getDireccion();
        ProvinciaDto provincia = provinciaService.findById(direccion.getProvinciaId());
        LocalidadDto localidad = localidadService.findById(direccion.getLocalidadId());
        ProfesionalDto profesional = profesionalService.findById(profesionalId);

        // Guardamos el profesional elegido en el turno en sesión
        turno.setProfesional(profesional);

        // Filtrar disponibilidades libres y futuras
        List<DisponibilidadDto> disponibilidades = disponibilidadService.obtenerDisponibilidadesPorProfesionalLibres(profesionalId);

        model.addAttribute("disponibilidades", disponibilidades);
        model.addAttribute("lugar", lugar);
        model.addAttribute("provincia", provincia);
        model.addAttribute("localidad", localidad);
        model.addAttribute("profesional", profesional);
        model.addAttribute("turno", turno);
        return "turno/elegir-disponibilidad";
    }
    
    @GetMapping("/solicitar-turno/preconfirmacion")
    public String preconfirmarTurno(
            @RequestParam("disponibilidadId") Long disponibilidadId,
            Model model,
            @ModelAttribute("turno") TurnoDto turno) {

        // Buscar la disponibilidad seleccionada
        DisponibilidadDto disponibilidad = disponibilidadService.findById(disponibilidadId);

        // Validar que existan datos previos y que la disponibilidad corresponda al profesional elegido
        if (turno.getProfesional() == null || turno.getLugar() == null || disponibilidad == null ||
            !disponibilidad.getProfesional().getId().equals(turno.getProfesional().getId())) {
            return "redirect:/cliente/solicitar-turno?error=datos";
        }

        // Setear la disponibilidad seleccionada en el turno en sesión
        turno.setDisponibilidad(disponibilidad);

        // Obtener los IDs de localidad y provincia desde la dirección del lugar
        Long localidadId = turno.getLugar().getDireccion().getLocalidadId();
        Long provinciaId = turno.getLugar().getDireccion().getProvinciaId();
        LocalidadDto localidad = localidadService.findById(localidadId);
        ProvinciaDto provincia = provinciaService.findById(provinciaId);

        // Pasar los objetos al modelo
        model.addAttribute("localidad", localidad);
        model.addAttribute("provincia", provincia);
        model.addAttribute("turno", turno);
        return "turno/confirmar-turno";
    }
   
    // Paso 5: Confirmar turno con disponibilidad elegida
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @PostMapping("/solicitar-turno/confirmar")
    public String confirmarTurno(@RequestParam Long disponibilidadId,
                                 Model model,
                                 @ModelAttribute("turno") TurnoDto turno) {
        DisponibilidadDto disp = disponibilidadService.findById(disponibilidadId);

        // Validar que la disponibilidad corresponda al profesional/lugar/servicio del turno
        if (disp == null
            || turno.getProfesional() == null
            || !disp.getProfesional().getId().equals(turno.getProfesional().getId())) {
            // Manejo de error: datos inconsistentes
            return "redirect:/cliente/solicitar-turno?error=disponibilidad";
        }

        turno.setDisponibilidad(disp);

        // Guardar el turno en la base de datos
        turnoService.crearTurno(turno);
        
        // Enviar email al cliente y profesional
        emailService.enviarEmailConfirmacion(turno);

        // Limpiar la sesión
        model.asMap().remove("turno");

        return "redirect:/index?turnoConfirmado=ok";
    }
    
}