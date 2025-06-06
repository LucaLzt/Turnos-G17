package com.oo2.grupo17.helpers;

public class ViewRouteHelper {

    private ViewRouteHelper(){}

    //Auth
    public static final String USER_LOGIN = "authentication/login";
    public static final String CLIENTE_REGISTER = "authentication/register";

    //Home
    public static final String HOME_INDEX = "home/index";
    
    //Cliente
    public static final String CLIENTE_PERFIL = "cliente/perfil";
    public static final String CLIENTE_CONTACTO = "cliente/modificar-contacto";
    public static final String CLIENTE_DIRECCION = "cliente/modificar-direccion";
    public static final String CLIENTE_SERVICIOS = "cliente/servicios";
    public static final String CLIENTE_SERVICIOS_LUGARES = "cliente/lugar-por-servicio";
    
    //Admin
    public static final String ADMIN_REGISTRAR_PROFESIONAL = "admin/registrar-profesional";
    public static final String ADMIN_SERVICIOS = "admin/administrar-servicios";
    public static final String ADMIN_LUGARES = "admin/administrar-lugares";
    public static final String ADMIN_PROFESIONAL = "admin/administrar-profesional";
    
    //Profesional
    public static final String PROFESIONAL_PERFIL = "profesional/perfil";
    public static final String PROFESIONAL_LISTA_ELIMINAR = "profesionales/lista-eliminar";
    public static final String PROFESIONAL_LISTA_MODIFICAR = "profesionales/lista-modificar";
    public static final String PROFESIONAL_MODIFICAR = "profesionales/modificar";
    public static final String PROFESIONAL_LISTA_GESTION = "profesionales/lista-gestion";
    public static final String PROFESIONAL_GESTION = "profesionales/gestion";
    
    //Lugares
    public static final String LUGARES_AGREGAR = "lugares/agregar";
    public static final String LUGARES_LISTA_MODIFICAR = "lugares/lista-modificar";
    public static final String LUGARES_MODIFICAR = "lugares/modificar";
    public static final String LUGARES_LISTA_ELIMINAR = "lugares/lista-eliminar";
    
    //Servicios
    public static final String SERVICIOS_AGREGAR = "servicios/agregar";
    public static final String SERVICIOS_LISTA_MODIFICAR = "servicios/lista-modificar";
    public static final String SERVICIOS_MODIFICAR = "servicios/modificar";
    public static final String SERVICIOS_LISTA_ELIMINAR = "servicios/lista-eliminar";
    
    
}
