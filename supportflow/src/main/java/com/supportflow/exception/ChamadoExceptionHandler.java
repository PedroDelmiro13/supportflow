package com.supportflow.exception;

import com.supportflow.controller.ChamadoController;
import com.supportflow.dto.ErroResposta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento de erros específico dos endpoints de chamados.
 * Mantido em classe separada para não conflitar com o GlobalExceptionHandler,
 * que está sendo alterado em outras branches.
 */
@RestControllerAdvice(assignableTypes = ChamadoController.class)
public class ChamadoExceptionHandler {

    @ExceptionHandler(ChamadoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarChamadoNaoEncontrado(
            ChamadoNaoEncontradoException exception) {

        ErroResposta erro = new ErroResposta(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }
}
