package com.oo2.grupo17.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    // GET: /auth/login
    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(name="error", required=false) String error,
                        @RequestParam(name="logout", required=false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return ViewRouteHelper.USER_LOGIN;
    }

    @GetMapping("/register")
    public String registerAccount(Model model) {
        model.addAttribute("cliente", new ClienteRegistroDto());
        return ViewRouteHelper.CLIENTE_REGISTER;
    }

    @PostMapping("/register")
    public String processRegisterAccount(@ModelAttribute("cliente") ClienteRegistroDto clienteDto,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return ViewRouteHelper.CLIENTE_REGISTER;
        }
        clienteService.registrarCliente(clienteDto);
        return "redirect:/auth/login?registroExitoso";
    }

    // GET: /auth/loginSuccess
    @GetMapping("/loginSuccess")
    public String loginCheck() {
        return "redirect:/index";
    }
}
