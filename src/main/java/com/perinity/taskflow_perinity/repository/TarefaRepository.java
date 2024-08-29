package com.perinity.taskflow_perinity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perinity.taskflow_perinity.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByDepartamento(String departamento);

    List<Tarefa> findByPrazoBetween(LocalDate inicio, LocalDate fim);

}
