package com.bootcamp.estudiante;

import com.bootcamp.libro.Libro;
import com.bootcamp.materia.Materia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // este metodo podria reemplazar el metodo  que retorna la lista de estudiantes
    @GetMapping("/paged")
    public Page<Estudiante> getEstudiantes(@PageableDefault(size = 3, page = 0)Pageable pageable){
        //size = tamanio de la pagina
        //page = numero de pagina
        //sort = orden sobre alguno de los atributos, podemos agregar direccion "asc" o "desc"
        return estudianteService.findAllEstudiantes(pageable);
    }

    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(@PathVariable("id") Long estudianteId){
        return estudianteService.getEstudianteUnico(estudianteId);
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

    @PostMapping("{estudianteId}/libros")
    public Estudiante agregarLibroAEstudiante(@PathVariable Long estudianteId, @RequestBody Libro libro){
        return estudianteService.agregarLibroAEstudiante(estudianteId, libro);
    }

    @PostMapping("{estudianteId}/materias")
    public Estudiante agregarMateriaAEstudiante(@PathVariable Long estudianteId, @RequestBody Materia materia){
        return estudianteService.agregarMateriaAEstudiante(estudianteId, materia);
    }
}
