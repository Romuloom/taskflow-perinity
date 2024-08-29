package com.perinity.taskflow_perinity.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.repository.TarefaRepository;
import com.perinity.taskflow_perinity.repository.PessoaRepository;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> findById(Long id) {
        return tarefaRepository.findById(id);
    }

    public Tarefa save(Tarefa tarefa) {
        if (tarefa.getPessoaResponsavel() != null && tarefa.getPessoaResponsavel().getId() != null) {
            Optional<Pessoa> pessoa = pessoaRepository.findById(tarefa.getPessoaResponsavel().getId());
            pessoa.ifPresent(tarefa::setPessoaResponsavel);
        }
        return tarefaRepository.save(tarefa);
    }

    public void deleteById(Long id) {
        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> findByDepartamento(String departamento) {
        return tarefaRepository.findByDepartamento(departamento);
    }

    public List<Tarefa> findByPeriodo(LocalDate inicio, LocalDate fim) {
    return tarefaRepository.findByPrazoBetween(inicio, fim);
}
}
