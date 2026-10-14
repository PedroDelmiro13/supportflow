package com.supportflow.controller;

import com.supportflow.entity.Usuario;
import com.supportflow.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        chamadoService.excluir(id, usuarioAutenticado.getId());

        return ResponseEntity.noContent().build();
    }
}
