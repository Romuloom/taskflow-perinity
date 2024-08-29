package com.perinity.taskflow_perinity.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.service.TarefaService;
import com.perinity.taskflow_perinity.service.PessoaService;

class TarefaControllerTest {

    @InjectMocks
    private TarefaController tarefaController;

    @Mock
    private TarefaService tarefaService;

    @Mock
    private PessoaService pessoaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTarefas() {
        Tarefa tarefa = new Tarefa();
        when(tarefaService.findAll()).thenReturn(Collections.singletonList(tarefa));
        List<Tarefa> result = tarefaController.getAllTarefas();
        assertFalse(result.isEmpty());
        assertEquals(tarefa, result.get(0));
    }

    @Test
    void testGetTarefaByIdSuccess() {
        Tarefa tarefa = new Tarefa();
        when(tarefaService.findById(1L)).thenReturn(Optional.of(tarefa));
        ResponseEntity<Tarefa> response = tarefaController.getTarefaById(1L);
        assertEquals(ResponseEntity.ok(tarefa), response);
    }

    @Test
    void testGetTarefaByIdNotFound() {
        when(tarefaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Tarefa> response = tarefaController.getTarefaById(1L);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testCreateTarefaSuccess() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Teste");
        tarefa.setDescricao("Teste");
        tarefa.setPrazo(LocalDate.now());
        tarefa.setDepartamento("TI");
        tarefa.setStatus("Pendente");
        tarefa.setDuracao(5);

        when(tarefaService.save(tarefa)).thenReturn(tarefa);
        ResponseEntity<Tarefa> response = tarefaController.createTarefa(tarefa);
        assertEquals(ResponseEntity.ok(tarefa), response);
    }

    @Test
    void testCreateTarefaBadRequest() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(null); // Título é obrigatório

        ResponseEntity<Tarefa> response = tarefaController.createTarefa(tarefa);
        assertEquals(ResponseEntity.badRequest().body(null), response);
    }

    @Test
    void testUpdateTarefaSuccess() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);

        when(tarefaService.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaService.save(any(Tarefa.class))).thenReturn(tarefa);

        ResponseEntity<Tarefa> response = tarefaController.updateTarefa(1L, tarefa);
        assertEquals(ResponseEntity.ok(tarefa), response);
    }

    @Test
    void testUpdateTarefaNotFound() {
        when(tarefaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Tarefa> response = tarefaController.updateTarefa(1L, new Tarefa());
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testDeleteTarefaSuccess() {
        Tarefa tarefa = new Tarefa();
        when(tarefaService.findById(1L)).thenReturn(Optional.of(tarefa));
        doNothing().when(tarefaService).deleteById(1L);
        ResponseEntity<Object> response = tarefaController.deleteTarefa(1L);
        assertEquals(ResponseEntity.noContent().build(), response);
    }

    @Test
    void testDeleteTarefaNotFound() {
        when(tarefaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = tarefaController.deleteTarefa(1L);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testAlocarPessoaSuccess() {
        Tarefa tarefa = new Tarefa();
        tarefa.setDepartamento("TI");

        Pessoa pessoa = new Pessoa();
        pessoa.setDepartamento("TI");

        when(tarefaService.findById(1L)).thenReturn(Optional.of(tarefa));
        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));

        ResponseEntity<Tarefa> response = tarefaController.alocarPessoa(1L, 1L);
        assertEquals(ResponseEntity.ok(tarefa), response);
    }

    @Test
    void testAlocarPessoaDepartamentosDiferentes() {
        Tarefa tarefa = new Tarefa();
        tarefa.setDepartamento("TI");

        Pessoa pessoa = new Pessoa();
        pessoa.setDepartamento("RH");

        when(tarefaService.findById(1L)).thenReturn(Optional.of(tarefa));
        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));

        ResponseEntity<Tarefa> response = tarefaController.alocarPessoa(1L, 1L);
        assertEquals(ResponseEntity.badRequest().body(null), response);
    }

    @Test
    void testFinalizarTarefaSuccess() {
        Tarefa tarefa = new Tarefa();
        tarefa.setStatus("Pendente");

        when(tarefaService.findById(1L)).thenReturn(Optional.of(tarefa));
        ResponseEntity<Tarefa> response = tarefaController.finalizarTarefa(1L);
        assertEquals("Finalizada", tarefa.getStatus());
        assertEquals(ResponseEntity.ok(tarefa), response);
    }

    @Test
    void testFinalizarTarefaNotFound() {
        when(tarefaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Tarefa> response = tarefaController.finalizarTarefa(1L);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testListarTarefasPendentes() {
        Tarefa tarefa = new Tarefa();
        tarefa.setStatus("Pendente");
        tarefa.setPessoaResponsavel(null);
        tarefa.setPrazo(LocalDate.now());

        when(tarefaService.findAll()).thenReturn(Collections.singletonList(tarefa));
        List<Tarefa> result = tarefaController.listarTarefasPendentes();
        assertFalse(result.isEmpty());
        assertEquals(tarefa, result.get(0));
    }
}
