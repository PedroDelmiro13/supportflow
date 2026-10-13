package com.supportflow.exception;

import com.supportflow.dto.ErroResposta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResposta> tratarEmailDuplicado(
            EmailJaCadastradoException exception) {

        ErroResposta erro = new ErroResposta(
                HttpStatus.CONFLICT.value(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarDadosInvalidos(
            MethodArgumentNotValidException exception) {

        String mensagem = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.joining(" "));

        ErroResposta erro = new ErroResposta(
                HttpStatus.BAD_REQUEST.value(),
                mensagem
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }
}