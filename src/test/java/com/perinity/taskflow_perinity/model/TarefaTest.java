package com.perinity.taskflow_perinity.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collections;

@DataJpaTest
class TarefaTest {

    @Autowired
    private EntityManager entityManager;

    private Tarefa tarefa;

    @BeforeEach
    void setUp() {
        tarefa = new Tarefa();
        tarefa.setTitulo("Implementar nova funcionalidade");
        tarefa.setDescricao("Implementar a nova funcionalidade X");
        tarefa.setPrazo(LocalDate.now().plusDays(10));
        tarefa.setDepartamento("TI");
        tarefa.setDuracao(8);
        tarefa.setStatus("Pendente");
    }

    @Test
    @Transactional
    void testPersistTarefa() {
        entityManager.persist(tarefa);
        entityManager.flush();

        Tarefa foundTarefa = entityManager.find(Tarefa.class, tarefa.getId());
        assertNotNull(foundTarefa);
        assertEquals("Implementar nova funcionalidade", foundTarefa.getTitulo());
        assertEquals("TI", foundTarefa.getDepartamento());
    }

    @Test
    @Transactional
    void testTarefaPessoaRelationship() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João");
        pessoa.setDepartamento("TI");

        tarefa.setPessoaResponsavel(pessoa);
        pessoa.setTarefas(Collections.singletonList(tarefa));

        entityManager.persist(pessoa);
        entityManager.persist(tarefa);
        entityManager.flush();

        Tarefa foundTarefa = entityManager.find(Tarefa.class, tarefa.getId());
        assertNotNull(foundTarefa);
        assertNotNull(foundTarefa.getPessoaResponsavel());
        assertEquals("João", foundTarefa.getPessoaResponsavel().getNome());
    }

    @Test
    @Transactional
    void testRemoveTarefaAndKeepPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João");
        pessoa.setDepartamento("TI");

        tarefa.setPessoaResponsavel(pessoa);
        pessoa.setTarefas(Collections.singletonList(tarefa));

        entityManager.persist(pessoa);
        entityManager.persist(tarefa);
        entityManager.flush();

        Tarefa foundTarefa = entityManager.find(Tarefa.class, tarefa.getId());
        entityManager.remove(foundTarefa);
        entityManager.flush();

        Tarefa deletedTarefa = entityManager.find(Tarefa.class, tarefa.getId());
        assertNull(deletedTarefa);

        Pessoa foundPessoa = entityManager.find(Pessoa.class, pessoa.getId());
        assertNotNull(foundPessoa);
    }
}
