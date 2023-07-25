package com.bootcamp.materia;

import com.bootcamp.estudiante.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/materias")
public class MateriaController {

    private MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public Page<Materia> getMateria(@PageableDefault(size = 3, page = 0) Pageable pageable){
        return materiaService.findAllMaterias(pageable);
    }

    @PostMapping
    public ResponseEntity<Long> createMateria(@RequestBody Materia materia){
        Long idMateria = materiaService.createMateria(materia);
        return new ResponseEntity<>(idMateria, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteMateria(@PathVariable("id") Long materiaId){
        materiaService.deleteMateria(materiaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Materia> actualizarMateria(@PathVariable("id") Long materiaId,@RequestBody Materia materia){
        Materia materiaActualizada = materiaService.actualizarMateria(materiaId, materia);
        return new ResponseEntity<>(materiaActualizada, HttpStatus.ACCEPTED);
    }
}
