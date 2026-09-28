package br.insper.chouse.game.config;

import lombok.Getter;

@Getter
public final class ConfiguracaoJogo {

    private static final ConfiguracaoJogo instancia = new ConfiguracaoJogo();

    private final int pontosPorAcerto = 10;

    private ConfiguracaoJogo() {
    }

    public static ConfiguracaoJogo getInstance() {
        return instancia;
    }
}
