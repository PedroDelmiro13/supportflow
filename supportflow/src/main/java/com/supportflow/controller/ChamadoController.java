package com.supportflow.controller;

import com.supportflow.dto.ChamadoResponse;
import com.supportflow.entity.Usuario;
import com.supportflow.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

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
