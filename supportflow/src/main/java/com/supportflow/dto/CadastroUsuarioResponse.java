
package com.supportflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record CadastroUsuarioResponse(

        Long id,

        @JsonProperty("name")
        String nome,

        String email,

        @JsonProperty("createdAt")
        LocalDateTime dataCriacao

) {
}
