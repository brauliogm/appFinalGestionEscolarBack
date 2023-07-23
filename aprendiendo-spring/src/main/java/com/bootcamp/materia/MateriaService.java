package com.bootcamp.materia;

import com.bootcamp.estudiante.Estudiante;
import com.bootcamp.estudiante.EstudianteService;
import com.bootcamp.estudiante.Nombre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Transactional
@Service
public class MateriaService {

    private MateriaRepository materiaRepository;

    @Autowired
    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Transactional(readOnly = true)
    public Page<Materia> findAllMaterias(Pageable pageable){
        return  materiaRepository.findAll(pageable);
    }

    //Agregar validacion para que no se repitan las materias
    public Long createMateria(Materia materia){
        //check para saber si una materia ya existe
        boolean nombreExiste = materiaRepository.existsByNombre(materia.getNombre());

        if (nombreExiste){
            throw new NoSuchElementException("La materia con el nombre de " + materia.getNombre() + " ya existe");
        }

        return  materiaRepository.save(materia).getId();
    }

    public void deleteMateria(Long materiaId){
        // check si id existe, si no se imprime el warning
        boolean existe = materiaRepository.existsById(materiaId);

        //getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));
        if (!existe){
            throw new NoSuchElementException("WARNING: la materia con el Id " + materiaId + " no existe");
        }

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

        return materiaExistente;
    }


}
