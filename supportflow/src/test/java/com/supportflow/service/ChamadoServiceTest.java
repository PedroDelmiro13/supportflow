package com.supportflow.service;

import com.supportflow.entity.Chamado;
import com.supportflow.exception.ChamadoNaoEncontradoException;
import com.supportflow.repository.ChamadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
