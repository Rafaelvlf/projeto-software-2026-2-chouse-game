package br.insper.chouse.game.controller;

import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.service.PalpiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PalpiteControllerTests {

    @Mock
    private PalpiteService service;

    private PalpiteController controller;

    @BeforeEach
    void configurar() {
        controller = new PalpiteController(service);
    }

    @Test
    void deveEncaminharNovoPalpiteAoServico() {
        PalpiteController.PalpiteRequest request =
                new PalpiteController.PalpiteRequest(10L, 20L, 30L);

        Palpite palpite =
                new Palpite(10L, 20L, 30L, true, 10);

        when(service.palpitar(10L, 20L, 30L))
                .thenReturn(palpite);

        Palpite resposta = controller.palpitar(request);

        assertSame(palpite, resposta);
        verify(service).palpitar(10L, 20L, 30L);
    }

    @Test
    void deveBuscarPalpitePeloId() {
        Palpite palpite =
                new Palpite(10L, 20L, 30L, true, 10);

        when(service.buscar(1L)).thenReturn(palpite);

        assertSame(palpite, controller.buscar(1L));
        verify(service).buscar(1L);
    }

    @Test
    void deveListarPalpitesDoUsuario() {
        List<Palpite> palpites = List.of(
                new Palpite(10L, 20L, 30L, true, 10)
        );

        when(service.listarPorUsuario(10L))
                .thenReturn(palpites);

        assertSame(
                palpites,
                controller.listarPorUsuario(10L)
        );

        verify(service).listarPorUsuario(10L);
    }
}