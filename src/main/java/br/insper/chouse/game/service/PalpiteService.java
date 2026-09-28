package br.insper.chouse.game.service;

import br.insper.chouse.game.client.PollClient;
import br.insper.chouse.game.client.UsuarioClient;
import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;
import br.insper.chouse.game.repository.PalpiteRepository;
import br.insper.chouse.game.strategy.PontuacaoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PalpiteService {

    private final PalpiteRepository repository;
    private final PollClient pollClient;
    private final UsuarioClient usuarioClient;
    private final PontuacaoStrategy pontuacaoStrategy;

    @Transactional
    public Palpite palpitar(Long usuarioId, Long enqueteId, Long opcaoId) {
        if (repository.existsByUsuarioIdAndEnqueteId(usuarioId, enqueteId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já palpitou nesta enquete");
        }

        ResultadoEnquete resultado = pollClient.buscarResultado(enqueteId);
        if (resultado == null || resultado.getOpcaoVencedoraId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Enquete ainda não possui resultado");
        }

        boolean acertou = Objects.equals(opcaoId, resultado.getOpcaoVencedoraId());
        Palpite base = new Palpite(usuarioId, enqueteId, opcaoId, acertou, 0);
        int pontos = pontuacaoStrategy.calcular(base, resultado);
        Palpite salvo = repository.save(new Palpite(usuarioId, enqueteId, opcaoId, acertou, pontos));

        if (pontos > 0) {
            usuarioClient.adicionarPontos(usuarioId, pontos);
        }
        return salvo;
    }

    @Transactional(readOnly = true)
    public Palpite buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palpite não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Palpite> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}
