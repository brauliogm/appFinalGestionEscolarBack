package com.bootcamp.estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class EstudianteServiceMentiras {

    private EstudianteRepositoryMentiras estudianteRepositoryMentiras;
    private EstudianteRepository estudianteRepository;

    @Autowired
    public EstudianteServiceMentiras(EstudianteRepositoryMentiras estudianteRepositoryMentiras, EstudianteRepository estudianteRepository) {
        this.estudianteRepositoryMentiras = estudianteRepositoryMentiras;
        this.estudianteRepository = estudianteRepository;
    }

    public List<Estudiante> getEstudiantes(){
        List<Estudiante> estudiantes = estudianteRepositoryMentiras.getEstudiantes();
        //logica de negocio

        return estudiantes;
    }

    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(Long estudianteId){

        return estudianteRepositoryMentiras.getEstudianteUnico(estudianteId);
    }

    public void createEstudiante(Estudiante estudiante){
        //logica ...

        System.out.println("serv create estudinte entered");
        estudianteRepositoryMentiras.createEstudiante(estudiante);
        System.out.println("serv create estudinte exited");
    }

    public void deleteEstudiante(Long estudianteId){
        // check si id existe, si no imprimimos Warning
        boolean existe = false;

        for (Estudiante e : getEstudiantes()) {
            if (e.getId().equals(estudianteId)){
                existe = true;
                break;
            }
        }

        if (!existe){
            System.out.println("WARNING: ese Id no existe");
            return;
        }

        estudianteRepositoryMentiras.deleteEstudiante(estudianteId);
    }

    public void actualizarEstudiante(Long estudianteId, Estudiante estudiante){
        // check si id existe, si no imprimimos Warning
        //boolean existe = false;

//        for (Estudiante e : getEstudiantes()) {
//            if (e.getId().equals(estudianteId)){
//                existe = true;
//                break;
//            }
//        }

        boolean existe = getEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));

        if (!existe){
            System.out.println("WARNING: el estudiante con ese Id no existe");
            return;
        }

        estudianteRepositoryMentiras.actualizarEstudiante(estudianteId, estudiante);
    }

}
