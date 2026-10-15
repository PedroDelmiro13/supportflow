
package com.supportflow.controller;

import com.supportflow.dto.CadastroChamadoRequest;
import com.supportflow.dto.CadastroChamadoResponse;
import com.supportflow.dto.ChamadoResponse;
import com.supportflow.entity.Chamado;
import com.supportflow.entity.Usuario;
import com.supportflow.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<CadastroChamadoResponse> criar(
            @Valid @RequestBody CadastroChamadoRequest request,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        Chamado chamado = chamadoService.criar(
                request,
                usuarioAutenticado
        );

        CadastroChamadoResponse response = new CadastroChamadoResponse(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getDataCriacao()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponse>> listar(
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        return ResponseEntity.ok(
                chamadoService.listarDoUsuario(usuarioAutenticado.getId())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        chamadoService.excluir(id, usuarioAutenticado.getId());

        return ResponseEntity.noContent().build();
    }
}
