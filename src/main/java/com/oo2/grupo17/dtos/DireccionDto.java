package com.oo2.grupo17.dtos;

public class DireccionDto {
	
	private Long id;
	private String calle;
	private int altura;
	private String localidad;
	private String provincia;
	
	public DireccionDto() {}

	public DireccionDto(String calle, int altura, String localidad, String provincia) {
		super();
		this.calle = calle;
		this.altura = altura;
		this.localidad = localidad;
		this.provincia = provincia;
	}
	
	// Getters y Setters
	
	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
}
