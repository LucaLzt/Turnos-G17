package com.oo2.grupo17.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findById(Integer integer);
    
    Optional<UserEntity> findByUsername(String username);

    @Query(value = "from UserEntity u order by u.id")
    List<UserEntity> findAll();

    List<UserEntity> findAllByActive(boolean active);
}
