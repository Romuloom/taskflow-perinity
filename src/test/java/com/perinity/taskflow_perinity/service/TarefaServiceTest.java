package com.perinity.taskflow_perinity.service;

import com.perinity.taskflow_perinity.model.Pessoa;
import com.perinity.taskflow_perinity.model.Tarefa;
import com.perinity.taskflow_perinity.repository.TarefaRepository;
import com.perinity.taskflow_perinity.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TarefaServiceTest {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    private Tarefa tarefa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setTitulo("Implementar nova funcionalidade");
        tarefa.setDescricao("Descrição da tarefa");
        tarefa.setPrazo(LocalDate.now().plusDays(10));
        tarefa.setDepartamento("TI");
        tarefa.setDuracao(8);
        tarefa.setStatus("Pendente");
    }

    @Test
    void testFindAll() {
        when(tarefaRepository.findAll()).thenReturn(Collections.singletonList(tarefa));
        List<Tarefa> tarefas = tarefaService.findAll();
        assertEquals(1, tarefas.size());
        assertEquals("Implementar nova funcionalidade", tarefas.get(0).getTitulo());
    }

    @Test
    void testFindByIdSuccess() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        Optional<Tarefa> foundTarefa = tarefaService.findById(1L);
        assertTrue(foundTarefa.isPresent());
        assertEquals("Implementar nova funcionalidade", foundTarefa.get().getTitulo());
    }

    @Test
    void testFindByIdNotFound() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Tarefa> foundTarefa = tarefaService.findById(1L);
        assertFalse(foundTarefa.isPresent());
    }

    @Test
    void testSaveTarefaWithPessoaResponsavel() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        tarefa.setPessoaResponsavel(pessoa);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefa savedTarefa = tarefaService.save(tarefa);
        assertNotNull(savedTarefa.getPessoaResponsavel());
        assertEquals(1L, savedTarefa.getPessoaResponsavel().getId());
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    void testSaveTarefaWithoutPessoaResponsavel() {
        tarefa.setPessoaResponsavel(null);
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefa savedTarefa = tarefaService.save(tarefa);
        assertNull(savedTarefa.getPessoaResponsavel());
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    void testDeleteById() {
        doNothing().when(tarefaRepository).deleteById(1L);
        tarefaService.deleteById(1L);
        verify(tarefaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByDepartamento() {
        when(tarefaRepository.findByDepartamento("TI")).thenReturn(Collections.singletonList(tarefa));
        List<Tarefa> tarefas = tarefaService.findByDepartamento("TI");
        assertEquals(1, tarefas.size());
        assertEquals("Implementar nova funcionalidade", tarefas.get(0).getTitulo());
    }

    @Test
    void testFindByPeriodo() {
        LocalDate inicio = LocalDate.now().minusDays(5);
        LocalDate fim = LocalDate.now().plusDays(15);

        when(tarefaRepository.findByPrazoBetween(inicio, fim)).thenReturn(Collections.singletonList(tarefa));

        List<Tarefa> tarefas = tarefaService.findByPeriodo(inicio, fim);
        assertEquals(1, tarefas.size());
        assertEquals("Implementar nova funcionalidade", tarefas.get(0).getTitulo());
    }
}
