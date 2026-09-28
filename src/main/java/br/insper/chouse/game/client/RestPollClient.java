package br.insper.chouse.game.client;

import br.insper.chouse.game.model.ResultadoEnquete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestPollClient implements PollClient {

    private final RestClient restClient;

    public RestPollClient(@Value("${services.poll.url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    public ResultadoEnquete buscarResultado(Long enqueteId) {
        return restClient.get()
                .uri("/enquetes/{id}/resultado", enqueteId)
                .retrieve()
                .body(ResultadoEnquete.class);
    }
}
