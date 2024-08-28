package com.perinity.taskflow_perinity.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tarefa {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate prazo;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private int duracao; // em horas

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoaResponsavel;
}
