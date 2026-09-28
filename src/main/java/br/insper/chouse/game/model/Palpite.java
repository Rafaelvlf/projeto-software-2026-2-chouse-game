package br.insper.chouse.game.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "palpites", uniqueConstraints =
        @UniqueConstraint(name = "uk_palpite_usuario_enquete", columnNames = {"usuario_id", "enquete_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Palpite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "enquete_id", nullable = false)
    private Long enqueteId;

    @Column(name = "opcao_palpitada_id", nullable = false)
    private Long opcaoPalpitadaId;

    @Column(nullable = false)
    private boolean acertou;

    @Column(nullable = false)
    private int pontosGanhos;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHora;

    public Palpite(Long usuarioId, Long enqueteId, Long opcaoPalpitadaId, boolean acertou, int pontosGanhos) {
        this.usuarioId = usuarioId;
        this.enqueteId = enqueteId;
        this.opcaoPalpitadaId = opcaoPalpitadaId;
        this.acertou = acertou;
        this.pontosGanhos = pontosGanhos;
    }

    @PrePersist
    void definirDataHora() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }
}
