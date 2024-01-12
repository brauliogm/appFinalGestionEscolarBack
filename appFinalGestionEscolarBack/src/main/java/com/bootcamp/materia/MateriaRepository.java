package com.bootcamp.materia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateriaRepository extends JpaRepository <Materia, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdIsNot(String nombre, Long id);


    @Override
    Optional<Materia> findById(Long materiaId);
}
