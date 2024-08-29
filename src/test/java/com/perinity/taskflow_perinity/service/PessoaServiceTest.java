package com.perinity.taskflow_perinity.service;

import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private TarefaService tarefaService;

    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setDepartamento("TI");
    }

    @Test
    void testFindAll() {
        when(pessoaRepository.findAll()).thenReturn(Collections.singletonList(pessoa));
        List<Pessoa> pessoas = pessoaService.findAll();
        assertEquals(1, pessoas.size());
        assertEquals("João Silva", pessoas.get(0).getNome());
    }

    @Test
    void testFindByIdSuccess() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        Optional<Pessoa> foundPessoa = pessoaService.findById(1L);
        assertTrue(foundPessoa.isPresent());
        assertEquals("João Silva", foundPessoa.get().getNome());
    }

    @Test
    void testFindByIdNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Pessoa> foundPessoa = pessoaService.findById(1L);
        assertFalse(foundPessoa.isPresent());
    }

    @Test
    void testSavePessoa() {
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);
        Pessoa savedPessoa = pessoaService.save(pessoa);
        assertEquals("João Silva", savedPessoa.getNome());
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void testDeleteById() {
        doNothing().when(pessoaRepository).deleteById(1L);
        pessoaService.deleteById(1L);
        verify(pessoaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByNome() {
        when(pessoaRepository.findByNomeContainingIgnoreCase("joão")).thenReturn(Collections.singletonList(pessoa));
        List<Pessoa> pessoas = pessoaService.findByNome("joão");
        assertEquals(1, pessoas.size());
        assertEquals("João Silva", pessoas.get(0).getNome());
    }

    @Test
    void testListarDepartamentos() {
        Tarefa tarefa = new Tarefa();
        tarefa.setDepartamento("TI");

        when(pessoaRepository.findAll()).thenReturn(Collections.singletonList(pessoa));
        when(tarefaService.findAll()).thenReturn(Collections.singletonList(tarefa));

        List<Map<String, Object>> departamentos = pessoaService.listarDepartamentos();
        assertEquals(1, departamentos.size());
        assertEquals("TI", departamentos.get(0).get("departamento"));
        assertEquals(1, departamentos.get(0).get("pessoas"));
        assertEquals(1, departamentos.get(0).get("tarefas"));
    }

    @Test
    void testCalcularHorasPorPessoa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setDuracao(8);
        tarefa.setPessoaResponsavel(pessoa);

        pessoa.setTarefas(Collections.singletonList(tarefa));

        int horas = pessoaService.calcularHorasPorPessoa(pessoa);
        assertEquals(8, horas);
    }
}
