package com.oo2.grupo17.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("")
    public String index(){
        return ViewRouteHelper.HOME_INDEX;
    }
    
}
