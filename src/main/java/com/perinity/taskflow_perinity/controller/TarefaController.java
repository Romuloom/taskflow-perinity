package com.perinity.taskflow_perinity.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

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

import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.service.TarefaService;
import com.perinity.taskflow_perinity.service.PessoaService;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public List<Tarefa> getAllTarefas() {
        return tarefaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTarefaById(@PathVariable Long id) {
        return tarefaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarefa> createTarefa(@RequestBody Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getDescricao() == null || tarefa.getPrazo() == null ||
                tarefa.getDepartamento() == null || tarefa.getStatus() == null || tarefa.getDuracao() == 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Tarefa savedTarefa = tarefaService.save(tarefa);
        return ResponseEntity.ok(savedTarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        return tarefaService.findById(id)
                .map(existingTarefa -> {
                    tarefa.setId(existingTarefa.getId());
                    return ResponseEntity.ok(tarefaService.save(tarefa));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTarefa(@PathVariable Long id) {
        return tarefaService.findById(id)
                .map(tarefa -> {
                    tarefaService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/alocar/{id}")
    public ResponseEntity<Tarefa> alocarPessoa(@PathVariable Long id, @RequestParam Long pessoaId) {
        Optional<Tarefa> tarefaOptional = tarefaService.findById(id);
        Optional<Pessoa> pessoaOptional = pessoaService.findById(pessoaId);

        if (tarefaOptional.isPresent() && pessoaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            Pessoa pessoa = pessoaOptional.get();

            if (!tarefa.getDepartamento().equals(pessoa.getDepartamento())) {
                return ResponseEntity.badRequest().body(null); // Retorna erro se os departamentos forem diferentes
            }

            tarefa.setPessoaResponsavel(pessoa);
            tarefaService.save(tarefa);
            return ResponseEntity.ok(tarefa);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<Tarefa> finalizarTarefa(@PathVariable Long id) {
        Optional<Tarefa> tarefaOptional = tarefaService.findById(id);

        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setStatus("Finalizada");
            tarefaService.save(tarefa);
            return ResponseEntity.ok(tarefa);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pendentes")
    public List<Tarefa> listarTarefasPendentes() {
        return tarefaService.findAll().stream()
                .filter(t -> t.getPessoaResponsavel() == null && t.getStatus().equalsIgnoreCase("Pendente"))
                .sorted(Comparator.comparing(Tarefa::getPrazo))
                .limit(3)
                .collect(Collectors.toList());
    }

}
