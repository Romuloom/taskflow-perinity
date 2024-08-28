package com.perinity.taskflow_perinity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perinity.taskflow_perinity.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
}
