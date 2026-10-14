package com.supportflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supportflow.entity.Chamado;

import java.time.LocalDateTime;

public record ChamadoResponse(

        Long id,

        @JsonProperty("title")
        String titulo,

        @JsonProperty("description")
        String descricao,

        @JsonProperty("createdAt")
        LocalDateTime dataCriacao

) {

    public static ChamadoResponse de(Chamado chamado) {
        return new ChamadoResponse(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getDataCriacao()
        );
    }
}
