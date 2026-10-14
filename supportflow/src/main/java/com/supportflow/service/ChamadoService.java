package com.supportflow.service;

import com.supportflow.entity.Chamado;
import com.supportflow.exception.ChamadoNaoEncontradoException;
import com.supportflow.repository.ChamadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    /**
     * Exclui um chamado do usuário informado.
     * Se o chamado não existir ou pertencer a outro usuário, lança
     * {@link ChamadoNaoEncontradoException} (mesmo erro nos dois casos,
     * para não revelar a existência de chamados de terceiros).
     */
    @Transactional
    public void excluir(Long chamadoId, Long usuarioId) {
        Chamado chamado = chamadoRepository
                .findByIdAndUsuarioId(chamadoId, usuarioId)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(
                        "Chamado não encontrado."
                ));

        chamadoRepository.delete(chamado);
    }
}
