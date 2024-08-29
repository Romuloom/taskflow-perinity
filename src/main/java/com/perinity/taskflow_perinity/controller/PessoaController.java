package com.perinity.taskflow_perinity.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perinity.taskflow_perinity.DTO.PessoaDTO;
import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.service.PessoaService;
import com.perinity.taskflow_perinity.service.TarefaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public Pessoa createPessoa(@RequestBody Pessoa pessoa) {
        return pessoaService.save(pessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return pessoaService.findById(id)
                .map(existingPessoa -> {
                    pessoa.setId(existingPessoa.getId());
                    return ResponseEntity.ok(pessoaService.save(pessoa));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePessoa(@PathVariable Long id) {
        return pessoaService.findById(id)
                .map(pessoa -> {
                    pessoaService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PessoaDTO> listarPessoasComHoras() {
        List<Pessoa> pessoas = pessoaService.findAll();
        List<PessoaDTO> result = new ArrayList<>();

        for (Pessoa pessoa : pessoas) {
            int totalHoras = pessoaService.calcularHorasPorPessoa(pessoa);
            PessoaDTO pessoaDTO = new PessoaDTO(pessoa.getNome(), pessoa.getDepartamento(), totalHoras);
            result.add(pessoaDTO);
        }

        return result;
    }

    @GetMapping("/gastos")
    public List<Map<String, Object>> buscarPessoasPorNomeEPeriodo(
            @RequestParam String nome,
            @RequestParam String inicio,
            @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Pessoa> pessoas = pessoaService.findByNome(nome);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Pessoa pessoa : pessoas) {
            Map<String, Object> pessoaInfo = new HashMap<>();
            pessoaInfo.put("nome", pessoa.getNome());
            pessoaInfo.put("departamento", pessoa.getDepartamento());

            // Filtra tarefas dentro do período e calcula a média de horas
            List<Tarefa> tarefasNoPeriodo = pessoa.getTarefas().stream()
                    .filter(t -> !t.getPrazo().isBefore(dataInicio) && !t.getPrazo().isAfter(dataFim))
                    .collect(Collectors.toList());

            double mediaHoras = tarefasNoPeriodo.stream().mapToInt(Tarefa::getDuracao).average().orElse(0.0);
            pessoaInfo.put("mediaHoras", mediaHoras);

            result.add(pessoaInfo);
        }

        return result;
    }

    @GetMapping("/departamentos")
    public List<Map<String, Object>> listarDepartamentos() {
        List<Pessoa> pessoas = pessoaService.findAll();
        List<Tarefa> tarefas = tarefaService.findAll();

        Map<String, Map<String, Object>> departamentoInfo = new HashMap<>();

        // Contabilizar as pessoas por departamento
        for (Pessoa pessoa : pessoas) {
            departamentoInfo.putIfAbsent(pessoa.getDepartamento(), new HashMap<>());
            departamentoInfo.get(pessoa.getDepartamento())
                    .put("pessoas",
                            ((int) departamentoInfo.get(pessoa.getDepartamento()).getOrDefault("pessoas", 0)) + 1);
        }

        // Contabilizar as tarefas por departamento
        for (Tarefa tarefa : tarefas) {
            departamentoInfo.putIfAbsent(tarefa.getDepartamento(), new HashMap<>());
            departamentoInfo.get(tarefa.getDepartamento())
                    .put("tarefas",
                            ((int) departamentoInfo.get(tarefa.getDepartamento()).getOrDefault("tarefas", 0)) + 1);
        }

        // Converter para lista
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

}
