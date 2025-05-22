package com.oo2.grupo17.dtos;

public class ContactoDto {
	
	private Long id;
	private String email;
	private int movil;
	private int telefono;
	private DireccionDto direccion;
	
	public ContactoDto() {}

	public ContactoDto(String email, int movil, int telefono, DireccionDto direccion) {
		super();
		this.email = email;
		this.movil = movil;
		this.telefono = telefono;
		this.direccion = direccion;
	}
	
	// Getters y Setters
	
	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getMovil() {
		return movil;
	}

	public void setMovil(int movil) {
		this.movil = movil;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public DireccionDto getDireccion() {
		return direccion;
	}

	public void setDireccion(DireccionDto direccion) {
		this.direccion = direccion;
	}
	
}
