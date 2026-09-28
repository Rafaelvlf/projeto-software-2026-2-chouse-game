package br.insper.chouse.game.strategy;

import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PontuacaoStrategyTests {

    private final ResultadoEnquete resultado = new ResultadoEnquete(1L, 2L, 70, 30);

    @Test
    void pontuacaoFixaDevePremiarSomenteAcerto() {
        PontuacaoFixa strategy = new PontuacaoFixa();

        assertEquals(10, strategy.calcular(new Palpite(1L, 1L, 2L, true, 0), resultado));
        assertEquals(0, strategy.calcular(new Palpite(1L, 1L, 3L, false, 0), resultado));
    }

    @Test
    void pontuacaoPorMargemDeveUsarDiferencaDosPercentuais() {
        PontuacaoPorMargem strategy = new PontuacaoPorMargem(1.5);

        assertEquals(60, strategy.calcular(new Palpite(1L, 1L, 2L, true, 0), resultado));
    }
}
