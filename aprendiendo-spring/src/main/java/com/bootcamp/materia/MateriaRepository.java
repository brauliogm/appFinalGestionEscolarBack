package com.bootcamp.materia;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository <Materia, Long> {

    boolean existsByNombre(String nombre);


}
