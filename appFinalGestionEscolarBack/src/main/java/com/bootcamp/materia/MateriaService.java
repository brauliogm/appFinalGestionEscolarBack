package com.bootcamp.materia;

import com.bootcamp.estudiante.EstudianteService;
import com.bootcamp.libro.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class MateriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MateriaService.class);
    private MateriaRepository materiaRepository;

    @Autowired
    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Transactional(readOnly = true)
    public Page<Materia> findAllMaterias(Pageable pageable){
        LOGGER.info("buscando lista de materias");
        return  materiaRepository.findAll(pageable);
    }

    public Materia getMateriaUnica(Long materiaId){
        Optional<Materia> materiaUnica = materiaRepository.findById(materiaId);

        if (materiaUnica.isEmpty()){
            throw new NoSuchElementException("No existe ninguna materia con el id: " + materiaId);
        }

        LOGGER.info("buscando materia unica con id: {}", materiaId);
        return materiaUnica.get();
    }

    public Long createMateria(Materia materia){
        //check para saber si una materia ya existe
        boolean nombreExiste = materiaRepository.existsByNombre(materia.getNombre());

        if (nombreExiste){
            throw new NoSuchElementException("La materia con el nombre de " + materia.getNombre() + " ya existe");
        }

        LOGGER.info("Creando materia nueva con el id: {}", materia.getId());
        return  materiaRepository.save(materia).getId();
    }

    public void deleteMateria(Long materiaId){
        // check si id existe, si no se imprime el warning
        boolean existe = materiaRepository.existsById(materiaId);

        //getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));
        if (!existe){
            throw new NoSuchElementException("WARNING: la materia con el Id " + materiaId + " no existe");
        }

        LOGGER.info("borrando materia con id: {}", materiaId);
        materiaRepository.deleteById(materiaId);
    }

    public Materia actualizarMateria(Long materiaId, Materia materia){
        // Check si materia con ese id existe, si no, salta el error
        Materia materiaExistente = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new NoSuchElementException("La materia con ese id no existe, id: " + materiaId));

        //check para saber si el nombre de la materia que se quiere actualizar con este Id ya existe
        boolean nombreExiste = materiaRepository.existsByNombreAndIdIsNot(materia.getNombre(), materiaId);

        if (nombreExiste){
            throw new NoSuchElementException("La materia con el nombre de " + materia.getNombre() + " y el Id: " + materiaId + " ya existe");
        }

        //Actualizar Estudiante
        materiaExistente.setNombre(materia.getNombre());
        materiaExistente.setCreditos(materia.getCreditos());

        LOGGER.info("Actualizando materia con id: {}", materiaId);
        return materiaExistente;
    }


}
