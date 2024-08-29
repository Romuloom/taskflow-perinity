package com.perinity.taskflow_perinity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.repository.PessoaRepository;
import com.perinity.taskflow_perinity.repository.TarefaRepository;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private TarefaService tarefaService;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public void deleteById(Long id) {
        pessoaRepository.deleteById(id);
    }

    public List<Pessoa> findByNome(String nome) {
        return pessoaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Map<String, Object>> listarDepartamentos() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        List<Tarefa> tarefas = tarefaService.findAll(); // Lembre-se de injetar o TarefaService

        Map<String, Map<String, Object>> departamentoInfo = new HashMap<>();

        for (Pessoa pessoa : pessoas) {
            departamentoInfo.putIfAbsent(pessoa.getDepartamento(), new HashMap<>());
            departamentoInfo.get(pessoa.getDepartamento())
                    .put("pessoas",
                            ((int) departamentoInfo.get(pessoa.getDepartamento()).getOrDefault("pessoas", 0)) + 1);
        }

        for (Tarefa tarefa : tarefas) {
            departamentoInfo.putIfAbsent(tarefa.getDepartamento(), new HashMap<>());
            departamentoInfo.get(tarefa.getDepartamento())
                    .put("tarefas",
                            ((int) departamentoInfo.get(tarefa.getDepartamento()).getOrDefault("tarefas", 0)) + 1);
        }

        return departamentoInfo.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("departamento", entry.getKey());
                    map.put("pessoas", entry.getValue().get("pessoas"));
                    map.put("tarefas", entry.getValue().get("tarefas"));
                    return map;
                })
                .collect(Collectors.toList());
    }

    public int calcularHorasPorPessoa(Pessoa pessoa) {
        return pessoa.getTarefas().stream()
                .filter(tarefa -> tarefa.getPessoaResponsavel() != null && tarefa.getPessoaResponsavel().equals(pessoa))
                .mapToInt(Tarefa::getDuracao)
                .sum();
    }

}
