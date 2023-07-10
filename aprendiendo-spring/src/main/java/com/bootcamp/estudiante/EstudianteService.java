package com.bootcamp.estudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

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

    public void createEstudiante(Long estudianteId, Estudiante estudiante){

        emailValido(estudiante);

        emailRegistrado(estudianteId, estudiante);

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

        //check si el email en valido

        emailValido(estudiante);

        //check si el email que se quiere actualizar ya existe
        emailRegistrado(estudianteId, estudiante);

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

    boolean checkValideszEmail(String email){
        return Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE
        ).asPredicate().test(email);
    }


    private void emailValido(Estudiante estudiante) {
        if (!checkValideszEmail((estudiante.getEmail()))) {
            throw new IllegalArgumentException("Email " + estudiante.getEmail() + " no es valido");
        }
    }

    private void emailRegistrado(Long estudianteId, Estudiante estudiante) {
        boolean emailExiste = estudianteRepository.existsByEmailAndIdIsNot(estudiante.getEmail(), estudianteId);
        if (emailExiste) {
            throw new IllegalArgumentException("email " + estudiante.getEmail() + " ya esta registrado");
        }
    }

}
