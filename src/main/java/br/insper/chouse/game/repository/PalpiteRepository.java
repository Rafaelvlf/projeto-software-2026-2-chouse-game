package br.insper.chouse.game.repository;

import br.insper.chouse.game.model.Palpite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PalpiteRepository extends JpaRepository<Palpite, Long> {
    boolean existsByUsuarioIdAndEnqueteId(Long usuarioId, Long enqueteId);

    List<Palpite> findByUsuarioId(Long usuarioId);
}
