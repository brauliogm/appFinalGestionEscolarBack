package com.bootcamp.estudiante;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    boolean existsByEmailAndIdIsNot(String email, Long id);

    boolean existsByEmail(String email);

    @Override
    Optional<Estudiante> findById(Long aLong);

    List<Estudiante> findEstudianteByPrimerNombreOrPrimerApellido(String primerNombre, String primerApellido);

}
