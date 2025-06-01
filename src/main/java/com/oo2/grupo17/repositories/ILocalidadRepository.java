package com.oo2.grupo17.repositories;

import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILocalidadRepository extends JpaRepository<Localidad, Long> {

    List<Localidad> findByProvincia(Provincia provincia);
    boolean existsByNombreIgnoreCaseAndProvincia(String nombre, ProvinciaDto provincia);
    List<Localidad> findAllByOrderByNombreAsc();
	
}
