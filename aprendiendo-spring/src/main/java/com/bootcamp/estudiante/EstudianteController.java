package com.bootcamp.estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// @Deprecated //Con esta anotacion ya no se hace uso de esta version, se hace obsoleta
@RestController
@RequestMapping("api/v1/estudiantes")
public class EstudianteController{

    private EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public List<Estudiante> getEstudiantes(){
        return estudianteService.getAllEstudiantes();
    }

    @PostMapping
    public void createEstudiante(@RequestBody Estudiante estudiante){
        estudianteService.createEstudiante(estudiante);
    }

    @DeleteMapping("{id}")
    public void deleteEstudiante(@PathVariable("id") Long estudianteId){
        estudianteService.deleteEstudiante(estudianteId);
    }

    @PutMapping("{id}")
    public Estudiante actualizarEstudiante(@PathVariable("id") Long estudianteId,@RequestBody Estudiante estudiante){
        return estudianteService.actualizarEstudiante(estudianteId, estudiante);
    }

    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(@PathVariable("id") Long estudianteId){
        return estudianteService.getEstudianteUnico(estudianteId);
    }
}
