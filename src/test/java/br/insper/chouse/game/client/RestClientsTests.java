package br.insper.chouse.game.client;

import br.insper.chouse.game.model.ResultadoEnquete;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestClientsTests {

    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void iniciarServidor() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.start();

        baseUrl = "http://localhost:"
                + server.getAddress().getPort();
    }

    @AfterEach
    void pararServidor() {
        server.stop(0);
    }

    @Test
    void deveBuscarResultadoDaEnquete() {
        server.createContext(
                "/enquetes/20/resultado",
                exchange -> {
                    assertEquals(
                            "GET",
                            exchange.getRequestMethod()
                    );

                    responderJson(
                            exchange,
                            """
                            {
                              "enqueteId": 20,
                              "opcaoVencedoraId": 30,
                              "percentualA": 70.0,
                              "percentualB": 30.0
                            }
                            """
                    );
                }
        );

        RestPollClient client = new RestPollClient(baseUrl);

        ResultadoEnquete resultado =
                client.buscarResultado(20L);

        assertEquals(20L, resultado.getEnqueteId());
        assertEquals(30L, resultado.getOpcaoVencedoraId());
        assertEquals(70.0, resultado.getPercentualA());
        assertEquals(30.0, resultado.getPercentualB());
    }

    @Test
    void deveEnviarPontosAoUsuario() {
        AtomicReference<String> corpoRecebido =
                new AtomicReference<>();

        server.createContext(
                "/usuarios/10/pontos",
                exchange -> {
                    assertEquals(
                            "POST",
                            exchange.getRequestMethod()
                    );

                    corpoRecebido.set(
                            new String(
                                    exchange
                                            .getRequestBody()
                                            .readAllBytes(),
                                    StandardCharsets.UTF_8
                            )
                    );

                    exchange.sendResponseHeaders(204, -1);
                    exchange.close();
                }
        );

        RestUsuarioClient client =
                new RestUsuarioClient(baseUrl);

        client.adicionarPontos(10L, 15);

        assertEquals(
                "{\"pontos\":15}",
                corpoRecebido.get()
        );
    }

    private void responderJson(
            HttpExchange exchange,
            String json
    ) throws IOException {
        byte[] resposta =
                json.getBytes(StandardCharsets.UTF_8);

        exchange
                .getResponseHeaders()
                .add("Content-Type", "application/json");

        exchange.sendResponseHeaders(
                200,
                resposta.length
        );

        exchange
                .getResponseBody()
                .write(resposta);

        exchange.close();
    }
}