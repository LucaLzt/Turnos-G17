# Proyecto Programación Orientada a Objetos II UNLa - Sistema de Gestión de Turnos

Este proyecto corresponde a la materia **Programación Orientada a Objetos II** de la Universidad Nacional de Lanús (UNLa). Su objetivo principal es el desarrollo de un **Sistema de Gestión de Turnos** que permite administrar, reservar y asignar turnos para distintas entidades, optimizando la organización y atención de usuarios.

## Descripción

El sistema fue desarrollado como trabajo práctico integrador. Este proyecto permite:

- Registrar usuarios y entidades que otorgan turnos.
- Asignar y cancelar turnos.
- Visualizar turnos disponibles y reservados.
- Gestionar notificaciones y recordatorios.

## Características Principales

- Gestión de usuarios y roles (administrador, profesional, cliente).
- Alta, baja y modificación de turnos.
- Filtrado y búsqueda de turnos por fecha, usuario o entidad.
- Notificaciones automáticas.
- Interfaz intuitiva y amigable.

## Requisitos

- Java 17 o superior.
- Maven 3 o superior.
- Plugin de lombok configurado en su IDE.
- MySQL como Base de Datos.
- Crear una Base de Datos con la siguiente instrucción **create database nombre;**
- Abrir el proyecto y revisar que se descarguen las dependencias, en caso contrario, abrir una terminal en la raíz del proyecto y ejecutar la instrucción: **mvn clean install**.
- Configurar las variables de entorno para que el archivo application.properties las reconozca antes de iniciar la aplicación:
  - DB_URL -> Colocar la url de la Base de Datos.
  - DB_USERNAME -> Colocar tu usuario de la Base de Datos.
  - DB_PASSWORD -> Colocar tu password de la Base de Datos.
  - MAIL_USERNAME -> Colocar tu email para envíos de correos.
  - MAIL_PASSWORD -> Colocar tu contraseña para aplicaciones para envíos de correos.
- Ejecutar el proyecto. Si todo está correcto aparece que la aplicación inició en x segundos en el puerto 8080.
- Inicializar el init.sql dentro de nuestra aplicación de manejo de bases de datos.
- Abrir el navegador e ir a la siguiente url: [http//localhost:8080/auth/login](http://localhost:8080/auth/login)
- Al entrar a la página vas a tener tres opciones:
    - Ingresar como **ADMIN**: admin1234@admin.com **password**: admin1234
    - Ingresar como **CLIENTE**: cliente1234@cliente.com **password**: cliente1234
    - Ingresar como **PROFESIONAL**: profesional1234@profesional.com **password**: profesional1234
