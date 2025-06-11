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
    public static final String CLIENTE_LUGARES = "cliente/lugares";
    public static final String CLIENTE_LUGARES_SERVICIOS = "cliente/servicio-por-lugar";
    
    //Admin
    public static final String ADMIN_REGISTRAR_PROFESIONAL = "admin/registrar-profesional";
    public static final String ADMIN_SERVICIOS = "admin/servicios/administrar-servicios";
    public static final String ADMIN_LUGARES = "admin/lugares/administrar-lugares";
    public static final String ADMIN_PROFESIONAL = "admin/profesionales/administrar-profesional";
    public static final String ADMIN_ESPECIALIDADES = "admin/especialidades/administrar-especialidades";
    public static final String ADMIN_TURNOS = "admin/administrar-turnos";
    public static final String ADMIN_CONTACTAR_PROFESIONAL = "admin/contactar-profesional";
    
    //Profesional
    public static final String PROFESIONAL_PERFIL = "profesional/perfil";
    public static final String PROFESIONAL_CONTACTO = "profesional/modificar-contacto";
    public static final String PROFESIONAL_DIRECCION = "profesional/modificar-direccion";
    public static final String PROFESIONAL_SERVICIOS = "profesional/lista-servicios";
    public static final String PROFESIONAL_DISPONIBILIDAD = "profesional/ver-disponibilidades";
    public static final String PROFESIONAL_DETALLE_TURNO = "profesional/detalle-turno";
    public static final String PROFESIONAL_CONTRASEÑA = "profesional/cambiar-contraseña";
    public static final String PROFESIONAL_TURNOS_CANCELAR = "profesional/turnos-a-cancelar";
    
    public static final String PROFESIONALES_LISTA_ELIMINAR = "admin/profesionales/lista-eliminar";
    public static final String PROFESIONALES_LISTA_MODIFICAR = "admin/profesionales/lista-modificar";
    public static final String PROFESIONALES_MODIFICAR = "admin/profesionales/modificar";
    public static final String PROFESIONALES_LISTA_GESTION = "admin/profesionales/lista-gestion";
    public static final String PROFESIONALES_GESTION = "admin/profesionales/gestion";
    public static final String PROFESIONALES_DISPONIBILIDADES = "admin/profesionales/generar-disponibilidades";
    public static final String PROFESIONALES_DETALLE_TURNO = "admin/profesionales/detalle-turno";
    
    //Lugares
    public static final String LUGARES_AGREGAR = "admin/lugares/agregar";
    public static final String LUGARES_LISTA_MODIFICAR = "admin/lugares/lista-modificar";
    public static final String LUGARES_MODIFICAR = "admin/lugares/modificar";
    public static final String LUGARES_LISTA_ELIMINAR = "admin/lugares/lista-eliminar";
    
    //Servicios
    public static final String SERVICIOS_AGREGAR = "admin/servicios/agregar";
    public static final String SERVICIOS_LISTA_MODIFICAR = "admin/servicios/lista-modificar";
    public static final String SERVICIOS_MODIFICAR = "admin/servicios/modificar";
    public static final String SERVICIOS_LISTA_ELIMINAR = "admin/servicios/lista-eliminar";
    
    //Especialidades
    public static final String ESPECIALIDADES_AGREGAR = "admin/especialidades/agregar";
    public static final String ESPECIALIDADES_LISTA_MODIFICAR = "admin/especialidades/lista-modificar";
    public static final String ESPECIALIDADES_MODIFICAR = "admin/especialidades/modificar";
    public static final String ESPECIALIDADES_LISTA_ELIMINAR = "admin/especialidades/lista-eliminar";
    
    //Turno
    public static final String TURNO_SOLICITUD = "turno/solicitar-turno";
    public static final String TURNO_SOLICITUD_ELEGIR = "turno/elegir-servicio";
    public static final String TURNO_SOLICITUD_ELEGIR_LUGAR = "turno/elegir-lugar";
    public static final String TURNO_SOLICITUD_ELEGIR_PROFESIONAL = "turno/elegir-profesional";
    public static final String TURNO_SOLICITUD_ELEGIR_DISPONIBILIDAD = "turno/elegir-disponibilidad";
    public static final String TURNO_MODIFICAR_TURNO = "turno/modificar-turno";
    
    
    
    
    
    
}
