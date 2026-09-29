package br.insper.chouse.game.strategy;

import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PontuacaoStrategyTests {

    private final ResultadoEnquete resultado =
            new ResultadoEnquete(1L, 2L, 70, 30);

    @Test
    void pontuacaoFixaDevePremiarAcerto() {
        PontuacaoFixa strategy = new PontuacaoFixa();

        Palpite palpite = new Palpite(
                1L,
                1L,
                2L,
                true,
                0
        );

        int pontos = strategy.calcular(palpite, resultado);

        assertEquals(10, pontos);
    }

    @Test
    void pontuacaoFixaNaoDevePremiarErro() {
        PontuacaoFixa strategy = new PontuacaoFixa();

        Palpite palpite = new Palpite(
                1L,
                1L,
                3L,
                false,
                0
        );

        int pontos = strategy.calcular(palpite, resultado);

        assertEquals(0, pontos);
    }

    @Test
    void pontuacaoPorMargemDeveCalcularDiferencaDosPercentuais() {
        PontuacaoPorMargem strategy = new PontuacaoPorMargem(1.5);

        Palpite palpite = new Palpite(
                1L,
                1L,
                2L,
                true,
                0
        );

        int pontos = strategy.calcular(palpite, resultado);

        assertEquals(60, pontos);
    }

    @Test
    void pontuacaoPorMargemNaoDevePremiarErro() {
        PontuacaoPorMargem strategy = new PontuacaoPorMargem(1.5);

        Palpite palpite = new Palpite(
                1L,
                1L,
                3L,
                false,
                0
        );

        int pontos = strategy.calcular(palpite, resultado);

        assertEquals(0, pontos);
    }
}