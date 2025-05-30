package com.oo2.grupo17.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.RoleEntity;
import com.oo2.grupo17.entities.RoleType;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findById(Integer integer);

    Optional<RoleEntity> findByType(RoleType type);
}
