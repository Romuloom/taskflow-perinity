package com.perinity.taskflow_perinity.repository;

import com.perinity.taskflow_perinity.model.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TarefaRepositoryTest {

    @Autowired
    private TarefaRepository tarefaRepository;

    private Tarefa tarefa1;
    private Tarefa tarefa2;

    @BeforeEach
    void setUp() {
        tarefa1 = new Tarefa();
        tarefa1.setTitulo("Desenvolver nova funcionalidade");
        tarefa1.setDescricao("Descrição da tarefa 1");
        tarefa1.setPrazo(LocalDate.of(2023, 9, 10));
        tarefa1.setDepartamento("TI");
        tarefa1.setDuracao(8);
        tarefa1.setStatus("Pendente");

        tarefa2 = new Tarefa();
        tarefa2.setTitulo("Testar API");
        tarefa2.setDescricao("Descrição da tarefa 2");
        tarefa2.setPrazo(LocalDate.of(2023, 9, 15));
        tarefa2.setDepartamento("QA");
        tarefa2.setDuracao(4);
        tarefa2.setStatus("Em andamento");

        tarefaRepository.save(tarefa1);
        tarefaRepository.save(tarefa2);
    }

    @Test
    void testFindByDepartamento() {
        List<Tarefa> tarefasTI = tarefaRepository.findByDepartamento("TI");
        assertEquals(1, tarefasTI.size());
        assertEquals("Desenvolver nova funcionalidade", tarefasTI.get(0).getTitulo());

        List<Tarefa> tarefasQA = tarefaRepository.findByDepartamento("QA");
        assertEquals(1, tarefasQA.size());
        assertEquals("Testar API", tarefasQA.get(0).getTitulo());
    }

    @Test
    void testFindByPrazoBetween() {
        LocalDate inicio = LocalDate.of(2023, 9, 5);
        LocalDate fim = LocalDate.of(2023, 9, 12);

        List<Tarefa> tarefas = tarefaRepository.findByPrazoBetween(inicio, fim);
        assertEquals(1, tarefas.size());
        assertEquals("Desenvolver nova funcionalidade", tarefas.get(0).getTitulo());

        inicio = LocalDate.of(2023, 9, 10);
        fim = LocalDate.of(2023, 9, 20);

        tarefas = tarefaRepository.findByPrazoBetween(inicio, fim);
        assertEquals(2, tarefas.size());
    }
}
