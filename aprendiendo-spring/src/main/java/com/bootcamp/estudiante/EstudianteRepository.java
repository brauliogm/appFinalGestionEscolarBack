package com.bootcamp.estudiante;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    boolean existsByEmailAndIdIsNot(String email, Long id);

    boolean existsByEmail(String email);

//    @Override
//    Optional<Estudiante> findById(Long aLong);

    //Consulta derivada
//    List<Estudiante> findEstudianteByPrimerNombreOrPrimerApellido(String primerNombre, String primerApellido);

    // consulta con JPQL
//    @Query("select e from Estudiante e where e.primerNombre = ?1 or e.primerApellido = ?2")
//    List<Estudiante> findEstudianteByPrimerNombreOrPrimerApellido(String primerNombre, String primerApellido);

    // consulta con SQL
//    @Query(value = "SELECT * FROM estudiante WHERE primer_nombre = ?1 OR primer_apellido = ?2", nativeQuery = true)
//    List<Estudiante> findEstudianteByPrimerNombreOrPrimerApellido(String primerNombre, String primerApellido);

    @Query(value = "SELECT * FROM estudiante WHERE primer_nombre = :primer_nombre OR primer_apellido = :primer_apellido", nativeQuery = true)
    List<Estudiante> findEstudianteByPrimerNombreOrPrimerApellido(
            @Param("primer_nombre") String primerNombre,
            @Param("primer_apellido") String primerApellido
    );

}
