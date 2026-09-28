package br.insper.chouse.game.strategy;

import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;

public interface PontuacaoStrategy {
    int calcular(Palpite palpite, ResultadoEnquete resultado);
}
