package com.supportflow.controller;

import com.supportflow.dto.ChamadoResponse;
import com.supportflow.entity.Usuario;
import com.supportflow.enums.UserRole;
import com.supportflow.exception.ChamadoExceptionHandler;
import com.supportflow.exception.ChamadoNaoEncontradoException;
import com.supportflow.service.ChamadoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChamadoControllerTest {

    private static final Long USUARIO_ID = 1L;
    private static final Long CHAMADO_ID = 10L;

    private ChamadoService chamadoService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        chamadoService = mock(ChamadoService.class);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new ChamadoController(chamadoService))
                .setControllerAdvice(new ChamadoExceptionHandler())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();

        Usuario usuario = new Usuario();
        usuario.setId(USUARIO_ID);
        usuario.setRole(UserRole.USER);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        usuario, null, usuario.getAuthorities()
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void listar_retorna200ComChamadosDoUsuarioAutenticado() throws Exception {
        when(chamadoService.listarDoUsuario(USUARIO_ID)).thenReturn(List.of(
                new ChamadoResponse(
                        CHAMADO_ID,
                        "Erro na página principal",
                        "O botão de submissão não funciona.",
                        LocalDateTime.of(2026, 10, 14, 12, 0)
                )
        ));

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(CHAMADO_ID))
                .andExpect(jsonPath("$[0].title").value("Erro na página principal"))
                .andExpect(jsonPath("$[0].description").value("O botão de submissão não funciona."));

        verify(chamadoService).listarDoUsuario(USUARIO_ID);
    }

    @Test
    void listar_retorna200ComListaVaziaQuandoUsuarioNaoPossuiChamados() throws Exception {
        when(chamadoService.listarDoUsuario(USUARIO_ID)).thenReturn(List.of());

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void excluir_retorna204QuandoChamadoEhExcluido() throws Exception {
        mockMvc.perform(delete("/tickets/{id}", CHAMADO_ID))
                .andExpect(status().isNoContent());

        verify(chamadoService).excluir(CHAMADO_ID, USUARIO_ID);
    }

    @Test
    void excluir_retorna404QuandoChamadoNaoEncontradoOuDeOutroUsuario() throws Exception {
        doThrow(new ChamadoNaoEncontradoException("Chamado não encontrado."))
                .when(chamadoService).excluir(CHAMADO_ID, USUARIO_ID);

        mockMvc.perform(delete("/tickets/{id}", CHAMADO_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Chamado não encontrado."));
    }
}
