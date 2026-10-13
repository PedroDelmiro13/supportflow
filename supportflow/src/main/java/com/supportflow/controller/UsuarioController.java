
package com.supportflow.controller;

import com.supportflow.dto.CadastroUsuarioRequest;
import com.supportflow.dto.CadastroUsuarioResponse;
import com.supportflow.entity.Usuario;
import com.supportflow.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<CadastroUsuarioResponse> cadastrar(
            @Valid @RequestBody CadastroUsuarioRequest request) {

        Usuario usuario = usuarioService.cadastrar(request);

        CadastroUsuarioResponse response = new CadastroUsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCriacao()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
