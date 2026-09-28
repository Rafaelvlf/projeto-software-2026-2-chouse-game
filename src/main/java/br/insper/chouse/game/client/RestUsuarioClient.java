package br.insper.chouse.game.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestUsuarioClient implements UsuarioClient {

    private final RestClient restClient;

    public RestUsuarioClient(@Value("${services.user.url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    public void adicionarPontos(Long usuarioId, int pontos) {
        restClient.post()
                .uri("/usuarios/{id}/pontos", usuarioId)
                .body(new PontosRequest(pontos))
                .retrieve()
                .toBodilessEntity();
    }

    @lombok.Value
    private static class PontosRequest {
        int pontos;
    }
}
