package br.insper.chouse.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoEnquete {
    private Long enqueteId;
    private Long opcaoVencedoraId;
    private double percentualA;
    private double percentualB;
}
