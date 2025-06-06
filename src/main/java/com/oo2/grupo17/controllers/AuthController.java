package com.oo2.grupo17.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.implementation.ClienteService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/auth")
public class AuthController {
	
    private ClienteService clienteService;

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(name="error", required=false) String error,
                        @RequestParam(name="logout", required=false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return ViewRouteHelper.USER_LOGIN;
    }
    
    @GetMapping("/loginSuccess")
    public String loginCheck() {
        return "redirect:/index";
    }

    @GetMapping("/register")
    public String registerAccount(Model model) {
        model.addAttribute("cliente", new ClienteRegistroDto());
        return ViewRouteHelper.CLIENTE_REGISTER;
    }

    @PostMapping("/register")
    public String registerAccountPost(@ModelAttribute("cliente") ClienteRegistroDto clienteDto,
    		BindingResult result) {
        if (result.hasErrors()) {
            return ViewRouteHelper.CLIENTE_REGISTER;
        }
        clienteService.registrarCliente(clienteDto);
        return "redirect:/auth/login?registroExitoso";
    }
    
}
