package br.insper.chouse.game.service;

import br.insper.chouse.game.client.PollClient;
import br.insper.chouse.game.client.UsuarioClient;
import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;
import br.insper.chouse.game.repository.PalpiteRepository;
import br.insper.chouse.game.strategy.PontuacaoStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PalpiteServiceTests {

    @Mock
    private PalpiteRepository repository;

    @Mock
    private PollClient pollClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private PontuacaoStrategy pontuacaoStrategy;

    private PalpiteService service;

    @BeforeEach
    void configurar() {
        service = new PalpiteService(
                repository,
                pollClient,
                usuarioClient,
                pontuacaoStrategy
        );
    }

    @Test
    void deveRegistrarPalpiteCorretoECreditarPontos() {
        ResultadoEnquete resultado =
                new ResultadoEnquete(20L, 30L, 60, 40);

        Palpite salvo =
                new Palpite(10L, 20L, 30L, true, 10);

        when(repository.existsByUsuarioIdAndEnqueteId(10L, 20L))
                .thenReturn(false);

        when(pollClient.buscarResultado(20L))
                .thenReturn(resultado);

        when(pontuacaoStrategy.calcular(
                any(Palpite.class),
                any(ResultadoEnquete.class)
        )).thenReturn(10);

        when(repository.save(any(Palpite.class)))
                .thenReturn(salvo);

        Palpite retorno = service.palpitar(10L, 20L, 30L);

        assertSame(salvo, retorno);
        assertTrue(retorno.isAcertou());
        assertEquals(10, retorno.getPontosGanhos());

        verify(usuarioClient).adicionarPontos(10L, 10);
    }

    @Test
    void deveRegistrarPalpiteErradoSemCreditarPontos() {
        ResultadoEnquete resultado =
                new ResultadoEnquete(20L, 30L, 60, 40);

        Palpite salvo =
                new Palpite(10L, 20L, 31L, false, 0);

        when(repository.existsByUsuarioIdAndEnqueteId(10L, 20L))
                .thenReturn(false);

        when(pollClient.buscarResultado(20L))
                .thenReturn(resultado);

        when(pontuacaoStrategy.calcular(
                any(Palpite.class),
                any(ResultadoEnquete.class)
        )).thenReturn(0);

        when(repository.save(any(Palpite.class)))
                .thenReturn(salvo);

        Palpite retorno = service.palpitar(10L, 20L, 31L);

        assertFalse(retorno.isAcertou());
        assertEquals(0, retorno.getPontosGanhos());

        verify(usuarioClient, never())
                .adicionarPontos(anyLong(), anyInt());
    }

    @Test
    void deveRejeitarSegundoPalpiteNaMesmaEnquete() {
        when(repository.existsByUsuarioIdAndEnqueteId(10L, 20L))
                .thenReturn(true);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.palpitar(10L, 20L, 30L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        assertEquals(
                "Usuário já palpitou nesta enquete",
                erro.getReason()
        );

        verifyNoInteractions(
                pollClient,
                pontuacaoStrategy,
                usuarioClient
        );

        verify(repository, never()).save(any());
    }

    @Test
    void deveRejeitarEnqueteSemResultado() {
        when(repository.existsByUsuarioIdAndEnqueteId(10L, 20L))
                .thenReturn(false);

        when(pollClient.buscarResultado(20L))
                .thenReturn(null);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.palpitar(10L, 20L, 30L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        assertEquals(
                "Enquete ainda não possui resultado",
                erro.getReason()
        );

        verifyNoInteractions(pontuacaoStrategy, usuarioClient);
        verify(repository, never()).save(any());
    }

    @Test
    void deveRejeitarResultadoSemOpcaoVencedora() {
        ResultadoEnquete resultado =
                new ResultadoEnquete(20L, null, 50, 50);

        when(repository.existsByUsuarioIdAndEnqueteId(10L, 20L))
                .thenReturn(false);

        when(pollClient.buscarResultado(20L))
                .thenReturn(resultado);

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.palpitar(10L, 20L, 30L)
        );

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        assertEquals(
                "Enquete ainda não possui resultado",
                erro.getReason()
        );

        verifyNoInteractions(pontuacaoStrategy, usuarioClient);
        verify(repository, never()).save(any());
    }

    @Test
    void deveBuscarPalpiteExistente() {
        Palpite palpite =
                new Palpite(10L, 20L, 30L, true, 10);

        when(repository.findById(1L))
                .thenReturn(Optional.of(palpite));

        Palpite retorno = service.buscar(1L);

        assertSame(palpite, retorno);
    }

    @Test
    void deveRetornarNotFoundAoBuscarPalpiteInexistente() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(
                ResponseStatusException.class,
                () -> service.buscar(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
        assertEquals("Palpite não encontrado", erro.getReason());
    }

    @Test
    void deveListarPalpitesDoUsuario() {
        List<Palpite> palpites = List.of(
                new Palpite(10L, 20L, 30L, true, 10),
                new Palpite(10L, 21L, 31L, false, 0)
        );

        when(repository.findByUsuarioId(10L))
                .thenReturn(palpites);

        List<Palpite> retorno =
                service.listarPorUsuario(10L);

        assertSame(palpites, retorno);

        verify(repository).findByUsuarioId(10L);
    }
}