package br.insper.chouse.game.strategy;

import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PontuacaoPorMargem implements PontuacaoStrategy {

    private final double fatorMultiplicador;

    public PontuacaoPorMargem(@Value("${game.fator-multiplicador:1.0}") double fatorMultiplicador) {
        this.fatorMultiplicador = fatorMultiplicador;
    }

    @Override
    public int calcular(Palpite palpite, ResultadoEnquete resultado) {
        if (!palpite.isAcertou()) {
            return 0;
        }
        double margem = Math.abs(resultado.getPercentualA() - resultado.getPercentualB());
        return (int) Math.round(margem * fatorMultiplicador);
    }
}
