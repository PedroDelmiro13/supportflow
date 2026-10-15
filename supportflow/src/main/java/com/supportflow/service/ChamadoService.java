
package com.supportflow.service;

import com.supportflow.dto.CadastroChamadoRequest;
import com.supportflow.entity.Chamado;
import com.supportflow.entity.Usuario;
import com.supportflow.exception.ChamadoNaoEncontradoException;
import com.supportflow.repository.ChamadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    @Transactional
    public Chamado criar(
            CadastroChamadoRequest request,
            Usuario usuario) {

        Chamado chamado = new Chamado();
        chamado.setTitulo(request.titulo().trim());
        chamado.setDescricao(request.descricao().trim());
        chamado.setUsuario(usuario);

        return chamadoRepository.save(chamado);
    }


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
