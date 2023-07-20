package com.bootcamp.materia;

import com.bootcamp.estudiante.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/materias")
public class MateriaController {

    private MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping("/paged")
    public Page<Materia> getMateria(@PageableDefault(size = 3, page = 0) Pageable pageable){
        return materiaService.findAllMaterias(pageable);
    }

    @PostMapping
    public void createMateria(@RequestBody Materia materia){
        materiaService.createMateria(materia);
    }

    @DeleteMapping("{id}")
    public void deleteMateria(@PathVariable("id") Long materiaId){
        materiaService.deleteMateria(materiaId);
    }

    @PutMapping("{id}")
    public Materia actualizarMateria(@PathVariable("id") Long materiaId,@RequestBody Materia materia){
        return materiaService.actualizarMateria(materiaId, materia);
    }
}
