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
    public Page<Materia> getEstudiantes(@PageableDefault(size = 3, page = 0) Pageable pageable){
        return materiaService.findAllEstudiantes(pageable);
    }

    @PostMapping
    public void createMateria(@RequestBody Estudiante estudiante){
        materiaService.createEstudiante(estudiante);
    }

    @DeleteMapping("{id}")
    public void deleteMateria(@PathVariable("id") Long estudianteId){
        materiaService.deleteEstudiante(estudianteId);
    }

    @PutMapping("{id}")
    public Estudiante actualizarMateria(@PathVariable("id") Long estudianteId,@RequestBody Estudiante estudiante){
        return materiaService.actualizarEstudiante(estudianteId, estudiante);
    }
}
