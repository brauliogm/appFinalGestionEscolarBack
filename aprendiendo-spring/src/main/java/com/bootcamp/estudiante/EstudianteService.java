package com.bootcamp.estudiante;

import com.bootcamp.libro.Libro;
import com.bootcamp.libro.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

@Transactional
@Service
public class EstudianteService {

    private EstudianteRepository estudianteRepository;
    private LibroRepository libroRepository;

    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository,
                             LibroRepository libroRepository) {
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public List<Estudiante> getAllEstudiantes(){
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return estudiantes;
    }

    @Transactional(readOnly = true)
    public List<Estudiante> getEstudiantesByPrimerNombreOrPrimerApellido(String primerNombre, String primerApellido){
        List<Estudiante> estudiantes = estudianteRepository.findEstudianteByPrimerNombreOrPrimerApellido(primerNombre, primerApellido);
        return estudiantes;
    }

    public void createEstudiante(Estudiante estudiante){

        emailValido(estudiante);

        nuevoEmailRegistrado(estudiante);

        estudianteRepository.save(estudiante);
    }

    public void deleteEstudiante(Long estudianteId){
        // check si id existe, si no se imprime el warning
        boolean existe = estudianteRepository.existsById(estudianteId);
                //getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));

        if (!existe){
            throw new NoSuchElementException("WARNING: el estudiante con el Id " + estudianteId + " no existe");
        }

        estudianteRepository.deleteById(estudianteId);
    }

    public Estudiante actualizarEstudiante(Long estudianteId, Estudiante estudiante){
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));

        //Actualizar Estudiante
        estudianteExistente.setPrimerNombre(estudiante.getPrimerNombre());
        estudianteExistente.setSegundoNombre(estudiante.getSegundoNombre());
        estudianteExistente.setPrimerApellido(estudiante.getPrimerApellido());
        estudianteExistente.setSegundoApellido(estudiante.getSegundoApellido());
        estudianteExistente.setFechaNacimiento(estudiante.getFechaNacimiento());

        //check si el email en valido

        emailValido(estudiante);

        //check si el email que se quiere actualizar ya existe
        emailRegistrado(estudianteId, estudiante);

        //Actualizar email de estudiante
        estudianteExistente.setEmail(estudiante.getEmail());

        return estudianteExistente;
    }

    @Transactional(readOnly = true)
    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(Long estudianteId){
        Optional<Estudiante> estudianteOpcional = estudianteRepository.findById(estudianteId);

        if (estudianteOpcional.isEmpty()){
            throw new NoSuchElementException("No existe ningun estudiante con el id: " + estudianteId);
        }

        return estudianteOpcional.get();

//        return  estudianteRepository.findById(estudianteId)
//                .orElseThrow(() -> new NoSuchElementException("No existe ningun estudiante con el id: " + estudianteId));
    }

    private boolean checkValideszEmail(String email){
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

    private void nuevoEmailRegistrado(Estudiante estudiante) {
        boolean emailExiste = estudianteRepository.existsByEmail(estudiante.getEmail());
        if (emailExiste) {
            throw new IllegalArgumentException("email " + estudiante.getEmail() + " ya esta registrado");
        }
    }

    public Estudiante agregarLibroAEstudiante(Long estudianteId, Libro libro) {
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));

        libro.setEstudiante(estudianteExistente);
        libroRepository.save(libro);
        return estudianteExistente;
    }
}
