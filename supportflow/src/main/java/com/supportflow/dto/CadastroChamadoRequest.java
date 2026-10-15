package com.supportflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroChamadoRequest(

        @JsonProperty("title")
        @NotBlank(message = "O t├¡tulo do chamado ├® obrigat├│rio.")
        @Size(min = 3, max = 120,
                message = "O t├¡tulo deve ter entre 3 e 120 caracteres.")
        String titulo,

        @JsonProperty("description")
        @NotBlank(message = "A descri├º├úo do chamado ├® obrigat├│ria.")
        @Size(min = 10, max = 2000,
                message = "A descri├º├úo deve ter entre 10 e 2000 caracteres.")
        String descricao

) {
}
