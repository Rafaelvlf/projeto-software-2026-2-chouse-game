package br.insper.chouse.game.client;

import br.insper.chouse.game.model.ResultadoEnquete;

public interface PollClient {
    ResultadoEnquete buscarResultado(Long enqueteId);
}
