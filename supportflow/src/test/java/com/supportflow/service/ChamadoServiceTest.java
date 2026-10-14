package com.supportflow.service;

import com.supportflow.dto.ChamadoResponse;
import com.supportflow.entity.Chamado;
import com.supportflow.exception.ChamadoNaoEncontradoException;
import com.supportflow.repository.ChamadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    @Test
    void listarDoUsuario_retornaChamadosDoUsuarioMapeadosParaResposta() {
        LocalDateTime criadoEm = LocalDateTime.of(2026, 10, 14, 12, 0);

        Chamado chamado = new Chamado();
        chamado.setId(10L);
        chamado.setTitulo("Erro na página principal");
        chamado.setDescricao("O botão de submissão não funciona.");
        chamado.setDataCriacao(criadoEm);

        when(chamadoRepository.findAllByUsuarioIdOrderByDataCriacaoDesc(1L))
                .thenReturn(List.of(chamado));

        List<ChamadoResponse> resultado = chamadoService.listarDoUsuario(1L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).id());
        assertEquals("Erro na página principal", resultado.get(0).titulo());
        assertEquals("O botão de submissão não funciona.", resultado.get(0).descricao());
        assertEquals(criadoEm, resultado.get(0).dataCriacao());
        verify(chamadoRepository).findAllByUsuarioIdOrderByDataCriacaoDesc(1L);
    }

    @Test
    void listarDoUsuario_retornaListaVaziaQuandoUsuarioNaoPossuiChamados() {
        when(chamadoRepository.findAllByUsuarioIdOrderByDataCriacaoDesc(2L))
                .thenReturn(List.of());

        List<ChamadoResponse> resultado = chamadoService.listarDoUsuario(2L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void excluir_removeChamadoQuandoPertenceAoUsuario() {
        Chamado chamado = new Chamado();
        when(chamadoRepository.findByIdAndUsuarioId(10L, 1L))
                .thenReturn(Optional.of(chamado));

        chamadoService.excluir(10L, 1L);

        verify(chamadoRepository).delete(chamado);
    }

    @Test
    void excluir_lancaExcecaoQuandoChamadoNaoExisteOuEhDeOutroUsuario() {
        when(chamadoRepository.findByIdAndUsuarioId(10L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ChamadoNaoEncontradoException.class,
                () -> chamadoService.excluir(10L, 2L)
        );

        verify(chamadoRepository, never()).delete(any(Chamado.class));
    }
}
