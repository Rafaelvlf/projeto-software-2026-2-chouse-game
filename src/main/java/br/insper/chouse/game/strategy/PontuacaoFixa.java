package br.insper.chouse.game.strategy;

import br.insper.chouse.game.config.ConfiguracaoJogo;
import br.insper.chouse.game.model.Palpite;
import br.insper.chouse.game.model.ResultadoEnquete;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PontuacaoFixa implements PontuacaoStrategy {

    private final int pontosPorAcerto = ConfiguracaoJogo.getInstance().getPontosPorAcerto();

    @Override
    public int calcular(Palpite palpite, ResultadoEnquete resultado) {
        return palpite.isAcertou() ? pontosPorAcerto : 0;
    }
}
