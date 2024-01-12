package com.bootcamp.estudiante;

import com.bootcamp.cuenta.CuentaBancaria;
import com.bootcamp.cuenta.CuentaBancariaRepository;
import com.bootcamp.libro.Libro;
import com.bootcamp.libro.LibroRepository;
import com.bootcamp.materia.Materia;
import com.bootcamp.materia.MateriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EstudianteService.class);
    private EstudianteRepository estudianteRepository;
    private LibroRepository libroRepository;
    private final MateriaRepository materiaRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository,
                             LibroRepository libroRepository,
                             MateriaRepository materiaRepository,
                             CuentaBancariaRepository cuentaBancariaRepository) {
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
        this.materiaRepository = materiaRepository;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }

    @Transactional(readOnly = true)
    public List<Estudiante> getAllEstudiantes(){
        LOGGER.info("buscando lista de estudiantes");
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        return estudiantes;
    }

    @Transactional(readOnly = true)
    public Page<Estudiante> findAllEstudiantes(Pageable pageable){
        LOGGER.info("buscando lista de estudiantes");
        return  estudianteRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Estudiante> getEstudiantesByPrimerNombreOrPrimerApellido(String primerNombre, String primerApellido){
        LOGGER.info("Buscando estudiante unico");
        List<Estudiante> estudiantes = estudianteRepository.findEstudianteByPrimerNombreOrPrimerApellido(primerNombre, primerApellido);
        return estudiantes;
    }

    public Long createEstudiante(Estudiante estudiante){
        LOGGER.info("creando estudiante {} ", estudiante);

        //check si el email es valido
        emailValido(estudiante);

        //check para ver si el email existe
        nuevoEmailRegistrado(estudiante);

        Long id = estudianteRepository.save(estudiante).getId();
        LOGGER.info("Estudiante con id {} fue guardado exitosamente", id);
        return id;
    }

    public void deleteEstudiante(Long estudianteId){
        // check si id existe, si no se imprime el warning
        boolean existe = estudianteRepository.existsById(estudianteId);
                //getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));

        if (!existe){
            throw new NoSuchElementException("WARNING: el estudiante con el Id " + estudianteId + " no existe");
        }

        LOGGER.info("estudiante con id: {} borrado", estudianteId);
        estudianteRepository.deleteById(estudianteId);
    }

    public Estudiante actualizarEstudiante(Long estudianteId, Estudiante estudiante){
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));
        LOGGER.info("estudiante con id: {} encontrado para actualizarlo", estudianteId);

        //Actualizar Estudiante
        Nombre nombre = new Nombre();
        nombre.setPrimerNombre(estudiante.getNombre().getPrimerNombre());
        nombre.setSegundoNombre(estudiante.getNombre().getSegundoNombre());
        nombre.setPrimerApellido(estudiante.getNombre().getPrimerApellido());
        nombre.setSegundoApellido(estudiante.getNombre().getSegundoApellido());
        estudianteExistente.setNombre(nombre);
        estudianteExistente.setFechaNacimiento(estudiante.getFechaNacimiento());

        //check si el email en valido

        emailValido(estudiante);

        //check si el email que se quiere actualizar ya existe
        emailRegistrado(estudianteId, estudiante);

        //Actualizar email de estudiante
        estudianteExistente.setEmail(estudiante.getEmail());
        LOGGER.info("estudiante con id: {} actualizado", estudianteId);

        return estudianteExistente;
    }

    @Transactional(readOnly = true)
    @GetMapping("{id}")
    public Estudiante getEstudianteUnico(Long estudianteId){
        Optional<Estudiante> estudianteOpcional = estudianteRepository.findById(estudianteId);

        if (estudianteOpcional.isEmpty()){
            throw new NoSuchElementException("No existe ningun estudiante con el id: " + estudianteId);
        }
        LOGGER.info("estudiante con id: {} encontrado con getEstudianteUnico", estudianteId);
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
            LOGGER.warn("Email {} ya esta registrado", estudiante.getEmail());
            throw new IllegalArgumentException("email " + estudiante.getEmail() + " ya esta registrado");
        }
    }

    public Estudiante darLibroAEstudiante(Long estudianteId, Long libroId){
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));

        Libro libroExistente = libroRepository.findById(libroId)
                .orElseThrow(() -> new NoSuchElementException("Libro con ese id no existe, id: " + estudianteId));

        //Agregar libro a estudiante
        libroExistente.setEstudiante(estudianteExistente);
//        estudianteExistente.addLibro(libroExistente);

        return estudianteExistente;
    }

    public Estudiante darMateriaAEstudiante(Long estudianteId, Long materiaId){
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));

        Materia materiaExistente = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new NoSuchElementException("Libro con ese id no existe, id: " + estudianteId));

        //Agregar libro a estudiante
        estudianteExistente.addMateria(materiaExistente);

        return estudianteExistente;
    }

    public Estudiante darCuentaAEstudiante(Long estudianteId, Long cuentaId){
        // Check si estudiante con ese id existe, si no, salta el error
        Estudiante estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new NoSuchElementException("Estudiante con ese id no existe, id: " + estudianteId));

        CuentaBancaria cuentaExistente = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(() -> new NoSuchElementException("Libro con ese id no existe, id: " + estudianteId));

        //Agregar libro a estudiante
        estudianteExistente.setCuenta(cuentaExistente);

        return estudianteExistente;
    }
}
