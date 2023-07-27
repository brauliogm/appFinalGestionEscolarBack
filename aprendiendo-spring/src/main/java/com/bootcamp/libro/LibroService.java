package com.bootcamp.libro;

import com.bootcamp.estudiante.EstudianteService;
import com.bootcamp.materia.Materia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class LibroService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibroService.class);
    private LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public Page<Libro> findAllLibro(Pageable pageable){
        LOGGER.info("buscando lista de libros");
        return  libroRepository.findAll(pageable);
    }

    public Libro getLibroUnico(Long libroId){
        Optional<Libro> libroUnico = libroRepository.findById(libroId);

        if (libroUnico.isEmpty()){
            throw new NoSuchElementException("No existe ningun libro con el id: " + libroId);
        }

        LOGGER.info("Buscando libro por id: {}", libroId);
        return libroUnico.get();
    }

    public Long createLibro(Libro libro){
        //check para saber si un libro ya existe
        boolean tituloExiste = libroRepository.existsByTitulo(libro.getTitulo());

        if (tituloExiste){
            throw new NoSuchElementException("El libro con el titulo de " + libro.getTitulo() + " ya existe");
        }

        LOGGER.info("Guardando nuevo libro con id: {}", libro.getId());
        return  libroRepository.save(libro).getId();
    }

    public void deleteLibro(Long libroId){
        // check si id existe, si no se imprime el warning
        boolean existe = libroRepository.existsById(libroId);

        //getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));
        if (!existe){
            throw new NoSuchElementException("WARNING: el libro con el Id " + libroId + " no existe");
        }

        LOGGER.info("borrando libro con id: {}", libroId);
        libroRepository.deleteById(libroId);
    }

    public Libro actualizarLibro(Long libroId, Libro libro) {
        // Check si el libro con ese id existe, si no, salta el error
        Libro libroExiste = libroRepository.findById(libroId)
                .orElseThrow(() -> new NoSuchElementException("El libro con ese id no existe, id: " + libroId));

        //check para saber si el titulo del libro que se quiere actualizar con este Id ya existe
        boolean nombreExiste = libroRepository.existsByTituloAndIdIsNot(libro.getTitulo(), libroId);

        if (nombreExiste) {
            throw new NoSuchElementException("El libro con el titulo de " + libro.getTitulo() + " y el Id: " + libroId + " ya existe");
        }

        //Actualizar Estudiante
        libroExiste.setTitulo(libro.getTitulo());
        libroExiste.setAutor(libro.getAutor());
        libroExiste.setEstudiante(libro.getEstudiante());

        LOGGER.info("Actualizando libro con id: {}", libroId);
        return libroExiste;
    }
}
