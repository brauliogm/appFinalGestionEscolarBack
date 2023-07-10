package com.bootcamp.estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/estudiantes") //ejemplo de como sería una version 2 de la aplicacion
public class EstudianteControllerV2 {

    private EstudianteServiceMentiras estudianteService;

    @Autowired
    public EstudianteControllerV2(EstudianteServiceMentiras estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public List<Estudiante> getEstudiantes(){

        return estudianteService.getEstudiantes();
    }

    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(@PathVariable("id") Long estudianteId){

        return estudianteService.getEstudianteUnico(estudianteId);
    }

    @PostMapping
    public void createEstudiante(@RequestBody Estudiante estudiante){
        System.out.println("controller create estudinte entered");
        estudianteService.createEstudiante(estudiante);
        System.out.println("controller create estudinte exited");
    }

    @DeleteMapping("{id}")
    public void deleteEstudiante(@PathVariable("id") Long estudianteId){
        estudianteService.deleteEstudiante(estudianteId);
    }

    @PutMapping("{id}")
    public void actualizarEstudiante(@PathVariable("id") Long estudianteId,@RequestBody Estudiante estudiante){
        estudianteService.actualizarEstudiante(estudianteId, estudiante);
    }
}
