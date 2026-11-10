
package br.com.infnet.missioncontrol.controller;

import br.com.infnet.missioncontrol.model.Astronauta;
import br.com.infnet.missioncontrol.model.ErrorResponse;
import br.com.infnet.missioncontrol.model.StatusAstronauta;
import br.com.infnet.missioncontrol.service.AstronautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/astronautas")
public class AstronautaController {

    private final AstronautaService astronautaService;

    public AstronautaController(AstronautaService astronautaService) {
        this.astronautaService = astronautaService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Astronauta astronauta) {
        try {
            Astronauta novoAstronauta = astronautaService.criar(astronauta);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(novoAstronauta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) StatusAstronauta status) {

        if (status == null) {
            return ResponseEntity.ok(astronautaService.listarTodos());
        }

        return ResponseEntity.ok(
                astronautaService.filtrarPorStatus(status));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        if (id <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ID deve ser maior que zero."));
        }

        var astronauta = astronautaService.buscarPorId(id);

        if (astronauta.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Astronauta não encontrado."));
        }

        return ResponseEntity.ok(astronauta.get());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Astronauta astronauta) {

        if (id <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ID deve ser maior que zero."));
        }

        try {
            return ResponseEntity.ok(
                    astronautaService.atualizar(id, astronauta));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        if (id <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ID deve ser maior que zero."));
        }

        try {
            astronautaService.excluir(id);

            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}
