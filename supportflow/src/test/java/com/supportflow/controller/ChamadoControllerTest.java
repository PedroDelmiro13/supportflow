package com.supportflow.controller;

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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
