package com.bootcamp.libro;

import com.bootcamp.materia.Materia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/libros")
@PreAuthorize("hasAnyRole('BIBL', 'ADMIN')")
public class LibroController {

    private LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BIBL', 'COOR', 'ADMIN')")
    public List<Libro> getLibros(){
        return libroService.findAllLibro();
    }
//    public Page<Libro> getLibros(@PageableDefault(size = 3, page = 0) Pageable pageable){
//        return libroService.findAllLibro(pageable);
//    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('BIBL', 'COOR', 'ADMIN')")
    public Libro getLibroUnico(@PathVariable("id") Long libroId){
        return libroService.getLibroUnico(libroId);
    }

    @PostMapping
    public ResponseEntity<Long> createLibro(@RequestBody Libro libro){
        Long idLibro = libroService.createLibro(libro);
        return new ResponseEntity<>(idLibro, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteLibro(@PathVariable("id") Long libroId){
        libroService.deleteLibro(libroId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable("id") Long libroId,@RequestBody Libro libro){
        Libro libroActualizado = libroService.actualizarLibro(libroId, libro);
        return new ResponseEntity<>(libroActualizado, HttpStatus.ACCEPTED);
    }
}
