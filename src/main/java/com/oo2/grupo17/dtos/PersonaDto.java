package com.oo2.grupo17.dtos;

public class PersonaDto {
	
	protected Long id;
	private String nombre;
	private int dni;
	private ContactoDto contacto;
	
	public PersonaDto() {}

	public PersonaDto(String nombre, int dni, ContactoDto contacto) {
		super();
		this.nombre = nombre;
		this.dni = dni;
		this.contacto = contacto;
	}
	
	// Getters y Setters

	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public ContactoDto getContacto() {
		return contacto;
	}

	public void setContacto(ContactoDto contacto) {
		this.contacto = contacto;
	}
	
}
