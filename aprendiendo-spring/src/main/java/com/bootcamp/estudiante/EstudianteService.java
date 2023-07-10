package com.bootcamp.estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstudianteService {

    private EstudianteRepositoryMentiras estudianteRepositoryMentiras;
    private EstudianteRepository estudianteRepository;

    @Autowired
    public EstudianteService(EstudianteRepositoryMentiras estudianteRepositoryMentiras, EstudianteRepository estudianteRepository) {
        this.estudianteRepositoryMentiras = estudianteRepositoryMentiras;
        this.estudianteRepository = estudianteRepository;
    }

    public List<Estudiante> getAllEstudiantes(){
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return estudiantes;
    }

    public void createEstudiante(Estudiante estudiante){

        estudianteRepository.save(estudiante);
    }

    public void deleteEstudiante(Long estudianteId){
        // check si id existe, si no se imprime el warning
        boolean existe = getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));

        if (!existe){
            System.out.println("WARNING: ese Id no existe");
            return;
        }

        estudianteRepository.deleteById(estudianteId);
    }

    public void actualizarEstudiante(Long estudianteId, Estudiante estudiante){
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));

        //check si el email que se quiere actualizar ya existe
        boolean emailExiste = estudianteRepository.existsByEmailAndIdIsNot(estudiante.getEmail(), estudianteId);
        if (emailExiste) {
            throw new IllegalArgumentException("email " + estudiante.getEmail() + " ya esta registrado");
        }

        //Actualizar Estudiante
        estudianteExistente.setPrimerNombre(estudiante.getPrimerNombre());
        estudianteExistente.setSegundoNombre(estudiante.getSegundoNombre());
        estudianteExistente.setPrimerApellido(estudiante.getPrimerApellido());
        estudianteExistente.setSegundoApellido(estudiante.getSegundoApellido());
        estudianteExistente.setFechaNacimiento(estudiante.getFechaNacimiento());
        estudianteExistente.setEmail(estudiante.getEmail());

        estudianteRepository.save(estudianteExistente);
    }

    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(Long estudianteId){

        return estudianteRepositoryMentiras.getEstudianteUnico(estudianteId);
    }

}
