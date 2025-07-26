package com.oo2.grupo17.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Direccion;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Provincia;

@Repository
public interface ILugarRepository extends JpaRepository<Lugar, Long>{
	
	List<Lugar> findByServicios_id(Long servicioId);
	boolean existsByDireccion(Direccion direccion);
	
	boolean existsByDireccion_CalleAndDireccion_AlturaAndDireccion_LocalidadAndDireccion_Provincia(
	        String calle,
	        Integer altura,
	        Localidad localidad,
	        Provincia provincia
	    );
	
	List<Lugar> findByDireccion_CalleContainingIgnoreCase(String nombre);
	Optional<Lugar> findByDireccion_CalleAndDireccion_Altura(String direccion, int altura);
}
