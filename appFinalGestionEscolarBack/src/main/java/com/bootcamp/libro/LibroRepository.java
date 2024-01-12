package com.bootcamp.libro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTitulo(String titulo);

    boolean existsByTituloAndIdIsNot(String titulo, Long libroId);

    @Override
    Optional<Libro> findById(Long libroId);
}
