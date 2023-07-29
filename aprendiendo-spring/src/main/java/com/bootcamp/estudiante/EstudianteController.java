package com.bootcamp.estudiante;

import com.bootcamp.cuenta.CuentaBancaria;
import com.bootcamp.libro.Libro;
import com.bootcamp.materia.Materia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Deprecated //Con esta anotacion ya no se hace uso de esta version, se hace obsoleta
@RestController
@RequestMapping("api/v1/estudiantes")
public class EstudianteController{

    private EstudianteService estudianteService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EstudianteController.class);


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
    public ResponseEntity<Long> createEstudiante(@RequestBody Estudiante estudiante){
        Long idEstudiante = estudianteService.createEstudiante(estudiante);
        return new ResponseEntity<>(idEstudiante, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteEstudiante(@PathVariable("id") Long estudianteId){
        estudianteService.deleteEstudiante(estudianteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Estudiante> actualizarEstudiante(@PathVariable("id") Long estudianteId,@RequestBody Estudiante estudiante){
        Estudiante estudianteActualizado = estudianteService.actualizarEstudiante(estudianteId, estudiante);
        return new ResponseEntity<>(estudianteActualizado, HttpStatus.ACCEPTED);
    }


    @PutMapping("{estudianteId}/libros/{libroId}")
    public ResponseEntity<Estudiante> darLibroAEstudiante(@PathVariable("estudianteId") Long estudianteId, @PathVariable("libroId") Long libroId){
        Estudiante estudianteConNuevoLibro = estudianteService.darLibroAEstudiante(estudianteId, libroId);
        return new ResponseEntity<>(estudianteConNuevoLibro, HttpStatus.ACCEPTED);
    }

    @PutMapping("{estudianteId}/materias/{materiaId}")
    public ResponseEntity<Estudiante> darMateriaAEstudiante(@PathVariable("estudianteId") Long estudianteId, @PathVariable("materiaId") Long materiaId){
        Estudiante estudianteConNuevaMateria = estudianteService.darMateriaAEstudiante(estudianteId, materiaId);
        return new ResponseEntity<>(estudianteConNuevaMateria, HttpStatus.ACCEPTED);
    }

    @PutMapping("{estudianteId}/cuentas/{cuentaId}")
    public ResponseEntity<Estudiante> darCuentaAEstudiante(@PathVariable("estudianteId") Long estudianteId, @PathVariable("cuentaId") Long cuentaId){
        Estudiante estudianteConNuevaCuenta = estudianteService.darCuentaAEstudiante(estudianteId, cuentaId);
        return new ResponseEntity<>(estudianteConNuevaCuenta, HttpStatus.ACCEPTED);
    }


}
