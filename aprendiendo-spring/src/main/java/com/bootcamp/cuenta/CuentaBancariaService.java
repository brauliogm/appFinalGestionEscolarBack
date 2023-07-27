package com.bootcamp.cuenta;

import com.bootcamp.estudiante.EstudianteService;
import com.bootcamp.materia.Materia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class CuentaBancariaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuentaBancariaService.class);
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    public CuentaBancariaService(CuentaBancariaRepository cuentaBancariaRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }

    @Transactional(readOnly = true)
    public Page<CuentaBancaria> getAllCuentas(Pageable pageable) {
        LOGGER.info("Buscando lista de cuentas bancarias");
        return cuentaBancariaRepository.findAll(pageable);
    }

    public CuentaBancaria getCuentaUnica(Long cuentaId){
        Optional<CuentaBancaria> cuentaUnica = cuentaBancariaRepository.findById(cuentaId);

        LOGGER.info("Buscando cuenta unicia con id: {}", cuentaId);
        if (cuentaUnica.isEmpty()){
            throw new NoSuchElementException("No existe ninguna Cuenta Bancaria con el id: " + cuentaId);
        }

        LOGGER.info("Mostrando cuenta con id: {}", cuentaId);
        return cuentaUnica.get();
    }

    public Long createCuentaBancaria(CuentaBancaria cuentaBancaria){
        //check para saber si un numero de cuenta ya existe
        boolean numeroCuentaExiste = cuentaBancariaRepository.existsByNumeroCuenta(cuentaBancaria.getNumeroCuenta());

        LOGGER.info("comprobando que no exista ya el mismo numero de cuenta: {}", cuentaBancaria.getNumeroCuenta());
        if (numeroCuentaExiste){
            throw new IllegalArgumentException("El numero de cuenta con este numero " + cuentaBancaria.getNumeroCuenta() + " ya existe");
        }

        LOGGER.info("Guandando nueva cuenta bancaria");
        return  cuentaBancariaRepository.save(cuentaBancaria).getId();
    }

    public void deleteCuentaBancaria(Long cuentaId){

        // check si id existe, si no se imprime el warning
        boolean existe = cuentaBancariaRepository.existsById(cuentaId);

        if (!existe){
            throw new NoSuchElementException("WARNING: la cuenta bancaria con el Id " + cuentaId + " no existe");
        }

        //otra forma de saber si existe el id, nomas que para el estudiante
        //getAllEstudiantes().stream().anyMatch(e -> e.getId().equals(estudianteId));

        LOGGER.info("cuenta con id: {} borrado", cuentaId);
        cuentaBancariaRepository.deleteById(cuentaId);
    }

    public CuentaBancaria actualizarCuentaBancaria(Long cuentaId, CuentaBancaria cuentaBancaria){
        // Check si cuenta bancaria con ese id existe, si no, salta el error
        CuentaBancaria cuentaExiste = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(() -> new NoSuchElementException("La cuenta Bancaria con ese id no existe, id: " + cuentaId));

        //check para saber si un numero de cuenta ya existe
        boolean numeroCuentaExiste = cuentaBancariaRepository.existsByNumeroCuenta(cuentaBancaria.getNumeroCuenta());

        if (!numeroCuentaExiste){
            throw new NoSuchElementException("El numero de cuenta con este numero " + cuentaBancaria.getNumeroCuenta() + " no existe, asi que " +
                    "no se puede actualizar");
        }

        //Actualizar Cuenta Bancaria
        cuentaExiste.setBanco(cuentaBancaria.getBanco());
        cuentaExiste.setTitular(cuentaBancaria.getTitular());
        cuentaExiste.setEstudiante(cuentaBancaria.getEstudiante());

        LOGGER.info("cuenta con id: {} actializada", cuentaId);
        return cuentaExiste;
    }
}
