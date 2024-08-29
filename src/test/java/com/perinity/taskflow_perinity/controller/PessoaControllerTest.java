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
import com.perinity.taskflow_perinity.DTO.PessoaDTO;
import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.service.PessoaService;
import com.perinity.taskflow_perinity.service.TarefaService;

class PessoaControllerTest {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private TarefaService tarefaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePessoa() {
        Pessoa pessoa = new Pessoa();
        when(pessoaService.save(pessoa)).thenReturn(pessoa);
        Pessoa result = pessoaController.createPessoa(pessoa);
        assertEquals(pessoa, result);
    }

    @Test
    void testUpdatePessoaSuccess() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);

        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaService.save(any(Pessoa.class))).thenReturn(pessoa);

        ResponseEntity<Pessoa> response = pessoaController.updatePessoa(1L, pessoa);
        assertEquals(ResponseEntity.ok(pessoa), response);
    }

    @Test
    void testUpdatePessoaNotFound() {
        when(pessoaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Pessoa> response = pessoaController.updatePessoa(1L, new Pessoa());
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testDeletePessoaSuccess() {
        Pessoa pessoa = new Pessoa();
        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaService).deleteById(1L);
        ResponseEntity<Object> response = pessoaController.deletePessoa(1L);
        assertEquals(ResponseEntity.noContent().build(), response);
    }

    @Test
    void testDeletePessoaNotFound() {
        when(pessoaService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = pessoaController.deletePessoa(1L);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void testListarPessoasComHoras() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Teste");
        pessoa.setDepartamento("TI");

        List<Pessoa> pessoas = Collections.singletonList(pessoa);
        when(pessoaService.findAll()).thenReturn(pessoas);
        when(pessoaService.calcularHorasPorPessoa(pessoa)).thenReturn(10);

        List<PessoaDTO> result = pessoaController.listarPessoasComHoras();
        assertFalse(result.isEmpty());
        assertEquals("Teste", result.get(0).getNome());
        assertEquals(10, result.get(0).getTotalHoras());
    }

    @Test
    void testBuscarPessoasPorNomeEPeriodo() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Teste");
        pessoa.setDepartamento("TI");

        Tarefa tarefa = new Tarefa();
        tarefa.setDuracao(5);
        tarefa.setPrazo(LocalDate.of(2023, 1, 1));
        pessoa.setTarefas(Collections.singletonList(tarefa));

        when(pessoaService.findByNome("Teste")).thenReturn(Collections.singletonList(pessoa));

        List<Map<String, Object>> result = pessoaController.buscarPessoasPorNomeEPeriodo("Teste", "2023-01-01",
                "2023-12-31");
        assertFalse(result.isEmpty());
        assertEquals(5.0, result.get(0).get("mediaHoras"));
    }

    @Test
    void testListarDepartamentos() {
        Pessoa pessoa = new Pessoa();
        pessoa.setDepartamento("TI");

        Tarefa tarefa = new Tarefa();
        tarefa.setDepartamento("TI");

        when(pessoaService.findAll()).thenReturn(Collections.singletonList(pessoa));
        when(tarefaService.findAll()).thenReturn(Collections.singletonList(tarefa));

        List<Map<String, Object>> result = pessoaController.listarDepartamentos();
        assertFalse(result.isEmpty());
        assertEquals("TI", result.get(0).get("departamento"));
        assertEquals(1, result.get(0).get("pessoas"));
        assertEquals(1, result.get(0).get("tarefas"));
    }
}
