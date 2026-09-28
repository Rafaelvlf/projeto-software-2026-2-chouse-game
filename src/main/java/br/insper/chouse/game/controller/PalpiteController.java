package br.insper.chouse.game.controller;

import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.service.PalpiteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/palpites")
@RequiredArgsConstructor
public class PalpiteController {

    private final PalpiteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Palpite palpitar(@Valid @RequestBody PalpiteRequest request) {
        return service.palpitar(request.getUsuarioId(), request.getEnqueteId(), request.getOpcaoId());
    }

    @GetMapping("/{id}")
    public Palpite buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Palpite> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @Value
    public static class PalpiteRequest {
        @NotNull @Positive Long usuarioId;
        @NotNull @Positive Long enqueteId;
        @NotNull @Positive Long opcaoId;
    }
}
