package com.bootcamp.cuenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cuentas")
@PreAuthorize("hasRole('ADMIN')") //hasRole para cuando solo un rol tiene acceso, para no usar el hasAnyRole
public class CuentaBancariaController {

    private CuentaBancariaService cuentaBancariaService;

    @Autowired
    public CuentaBancariaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @GetMapping
    public Page<CuentaBancaria> getAllCuentas(@PageableDefault(size = 3, page = 0) Pageable pageable) {
        return cuentaBancariaService.getAllCuentas(pageable);
    }

    @GetMapping("{id}")
    public CuentaBancaria getCuentaUnica(@PathVariable("id") Long cuentaId){
        return cuentaBancariaService.getCuentaUnica(cuentaId);
    }

    @PostMapping
    public ResponseEntity<Long> createCuentaBancaria(@RequestBody CuentaBancaria cuentaBancaria){
        Long idCuenta = cuentaBancariaService.createCuentaBancaria(cuentaBancaria);
        return new ResponseEntity<>(idCuenta, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCuentaBancaria(@PathVariable("id") Long cuentaId){
        cuentaBancariaService.deleteCuentaBancaria(cuentaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<CuentaBancaria> actualizarCuentaBancaria(@PathVariable("id") Long cuentaId,@RequestBody CuentaBancaria cuentaBancaria){
        CuentaBancaria cuentaActualizada = cuentaBancariaService.actualizarCuentaBancaria(cuentaId, cuentaBancaria);
        return new ResponseEntity<>(cuentaActualizada, HttpStatus.ACCEPTED);
    }
}
