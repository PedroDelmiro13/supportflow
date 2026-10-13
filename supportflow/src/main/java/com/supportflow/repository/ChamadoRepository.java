package com.supportflow.repository;

import com.supportflow.entity.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findAllByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    Optional<Chamado> findByIdAndUsuarioId(Long id, Long usuarioId);
}
