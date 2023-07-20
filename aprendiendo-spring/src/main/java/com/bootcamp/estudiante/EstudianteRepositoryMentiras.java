package com.bootcamp.estudiante;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EstudianteRepositoryMentiras {

    private final List<Estudiante> estudiantes;

    public EstudianteRepositoryMentiras() {
        this.estudiantes = new ArrayList<Estudiante>();
//                estudiantes.add(new Estudiante(1L, "Fulana"));
//                estudiantes.add(new Estudiante(2L, "Mary"));
//                estudiantes.add(new Estudiante(3L, "Karla"));
//                estudiantes.add(new Estudiante(4L, "Renata"));
    }

    public List<Estudiante> getEstudiantes(){
        return estudiantes;
    }

    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(Long estudianteId){
        //Estudiante estudianteUnico = null;
//        for (Estudiante e : estudiantes) {
//            if (e.getId().equals(estudianteId)){
//                //estudianteUnico = e;
//                return e;
//            }
//        }
//        //return estudianteUnico;
//        throw new IllegalStateException("no se encontro el estudiante con ese id");

        return estudiantes.stream().filter(estudiante -> estudiante.getId().equals(estudianteId)).findFirst()
                .orElseThrow(() -> new IllegalStateException("no se encontro el estudiante con ese id"));
    }

    public void createEstudiante(Estudiante estudiante){
        System.out.println("repo create estudinte entered");
        estudiantes.add(estudiante);
        System.out.println("repo create estudinte exited");
    }

    public void deleteEstudiante(Long estudianteId){
//        for (int i = 0; i < estudiantes.size(); i++) {
//            Estudiante estudiante = estudiantes.get(i);
//            if(estudiante.getId().equals(estudianteId)){
//                estudiantes.remove(i);
//            }
//        }
        estudiantes.removeIf(e -> e.getId().equals(estudianteId));
    }

    public void actualizarEstudiante(Long estudianteId, Estudiante estudiante){
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(estudianteId)){
                e.getNombre().setPrimerNombre(estudiante.getNombre().getPrimerNombre());
            }
        }
    }

}
