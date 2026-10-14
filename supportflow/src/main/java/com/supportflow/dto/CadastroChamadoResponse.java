package com.supportflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CadastroChamadoResponse(
        Long id,

        @JsonProperty("title")
        String titulo,

        @JsonProperty("description")
        String descricao,

        @JsonProperty("createdAt")
        LocalDateTime dataCriacao
) {
}
