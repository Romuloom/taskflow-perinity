package com.perinity.taskflow_perinity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perinity.taskflow_perinity.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    List<Pessoa> findByNomeContainingIgnoreCase(String nome);
}
