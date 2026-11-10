package br.com.infnet.missioncontrol.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Astronauta {

    private Long id;
    private String nome;
    private String pais;
    private Integer idade;
    private Integer anosExperiencia;
    private StatusAstronauta status;
}