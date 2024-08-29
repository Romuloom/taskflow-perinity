package com.perinity.taskflow_perinity.repository;

import com.perinity.taskflow_perinity.model.Pessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    private Pessoa pessoa1;
    private Pessoa pessoa2;

    @BeforeEach
    void setUp() {
        pessoa1 = new Pessoa();
        pessoa1.setNome("João Silva");
        pessoa1.setDepartamento("TI");

        pessoa2 = new Pessoa();
        pessoa2.setNome("Maria Joaquina");
        pessoa2.setDepartamento("RH");

        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
    }

    @Test
    void testFindByNomeContainingIgnoreCase() {
        List<Pessoa> pessoas = pessoaRepository.findByNomeContainingIgnoreCase("joão");
        assertEquals(2, pessoas.size());

        pessoas = pessoaRepository.findByNomeContainingIgnoreCase("silva");
        assertEquals(1, pessoas.size());
        assertEquals("João Silva", pessoas.get(0).getNome());
    }
}
