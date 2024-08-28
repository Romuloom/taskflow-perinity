package com.perinity.taskflow_perinity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perinity.taskflow_perinity.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
}
