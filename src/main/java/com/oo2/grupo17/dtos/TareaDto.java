package com.oo2.grupo17.dtos;

public class TareaDto {
	
	private Long id;
	private String nombre;
	
	public TareaDto(){}
	
	/* Opcional: Lista de profesionales si se necesita relación inversa
	private Set<Long> profesionalesIds;
	*/
	
	public TareaDto(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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
	
}
