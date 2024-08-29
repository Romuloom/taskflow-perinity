package com.perinity.taskflow_perinity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PessoaDTO {
    private String nome;
    private String departamento;
    private int totalHoras;

}
