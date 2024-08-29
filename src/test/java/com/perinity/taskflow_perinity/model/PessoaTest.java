package com.perinity.taskflow_perinity.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collections;

@DataJpaTest
class PessoaTest {

    @Autowired
    private EntityManager entityManager;

    private Pessoa pessoa;
    private Tarefa tarefa;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa();
        pessoa.setNome("João");
        pessoa.setDepartamento("TI");

        tarefa = new Tarefa();
        tarefa.setTitulo("Implementar nova funcionalidade");
        tarefa.setDescricao("Implementar a nova funcionalidade X");
        tarefa.setPrazo(LocalDate.now().plusDays(10));
        tarefa.setDepartamento("TI");
        tarefa.setDuracao(8);
        tarefa.setStatus("Pendente");

        pessoa.setTarefas(Collections.singletonList(tarefa));
        tarefa.setPessoaResponsavel(pessoa);
    }

    @Test
    @Transactional
    void testPersistPessoa() {
        entityManager.persist(pessoa);
        entityManager.flush();

        Pessoa foundPessoa = entityManager.find(Pessoa.class, pessoa.getId());
        assertNotNull(foundPessoa);
        assertEquals("João", foundPessoa.getNome());
        assertEquals("TI", foundPessoa.getDepartamento());
    }

    @Test
    @Transactional
    void testPessoaTarefasRelationship() {
        entityManager.persist(pessoa);
        entityManager.flush();

        Pessoa foundPessoa = entityManager.find(Pessoa.class, pessoa.getId());
        assertNotNull(foundPessoa);
        assertNotNull(foundPessoa.getTarefas());
        assertEquals(1, foundPessoa.getTarefas().size());
        assertEquals(tarefa.getTitulo(), foundPessoa.getTarefas().get(0).getTitulo());
    }

    @Test
    @Transactional
    void testRemovePessoaAndTarefas() {
        entityManager.persist(pessoa);
        entityManager.flush();

        Pessoa foundPessoa = entityManager.find(Pessoa.class, pessoa.getId());
        entityManager.remove(foundPessoa);
        entityManager.flush();

        Pessoa deletedPessoa = entityManager.find(Pessoa.class, pessoa.getId());
        assertNull(deletedPessoa);

        Tarefa deletedTarefa = entityManager.find(Tarefa.class, tarefa.getId());
        assertNull(deletedTarefa);
    }
}
