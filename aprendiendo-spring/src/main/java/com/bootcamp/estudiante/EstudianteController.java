package com.bootcamp.estudiante;

import com.bootcamp.libro.Libro;
import com.bootcamp.materia.Materia;
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
    public List<Estudiante> getEstudiantes(
            @RequestParam(value = "primerNombre", required = false) String primerNombre,
            @RequestParam(value = "primerApellido", required = false) String primerApellido
    ){
        if (primerNombre != null || primerApellido != null) {
            return estudianteService.getEstudiantesByPrimerNombreOrPrimerApellido(primerNombre, primerApellido);
        }

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

    @PostMapping("{estudianteId}/libros")
    public Estudiante agregarLibroAEstudiante(@PathVariable Long estudianteId, @RequestBody Libro libro){
        return estudianteService.agregarLibroAEstudiante(estudianteId, libro);
    }

    @PostMapping("{estudianteId}/materias")
    public Estudiante agregarMateriaAEstudiante(@PathVariable Long estudianteId, @RequestBody Materia materia){
        return estudianteService.agregarMateriaAEstudiante(estudianteId, materia);
    }
}
