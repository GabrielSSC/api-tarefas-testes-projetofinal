package br.com.ada.tarefas.integration;

import br.com.ada.tarefas.model.Tarefa;
import br.com.ada.tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TarefaIntegracaoTest {

    // Aqui Spring Boot inicia o contexto e usa o H2 em memoria.
    @Autowired
    private MockMvc mockMvc;

    // O Repository e usado apenas para preparar/conferir dados no teste de integracao.
    @Autowired
    private TarefaRepository tarefaRepository;

    @BeforeEach
    void limparDados() {
        tarefaRepository.deleteAll();
    }

    // TODO: Criar de 2 a 3 testes de integracao.
    // Sugestoes: POST com persistencia; GET com dados; PUT ou DELETE.
    //
    // Dicas:
    // - Prepare o JSON de entrada da API (TarefaRequest).
    // - Execute a operacao HTTP com mockMvc.perform(...).
    // - Verifique o JSON da resposta (TarefaResponse), alem do banco quando necessario.
    // - Nao use @Mock para Repository nem Service nesta classe.

    @Test
    void givenJsonValido_whenPostTarefa_thenPersistirNoBanco() throws Exception {
        // Given
        String json = """
                {
                  "titulo": "Estudar JUnit",
                  "descricao": "Resolver exercicios da aula 01",
                  "concluida": false
                }
                """;

        // When: chamada HTTP real passando por Controller, Service e Repository
        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Estudar JUnit"));

        // Then: confirmamos no H2 que a tarefa foi realmente guardada
        List<Tarefa> salvas = tarefaRepository.findAll();
        assertEquals(1, salvas.size());
        assertEquals("Estudar JUnit", salvas.get(0).getTitulo());
        assertEquals("Resolver exercicios da aula 01", salvas.get(0).getDescricao());
        assertFalse(salvas.get(0).isConcluida());
    }

    @Test
    void givenTarefaExistente_whenPutTarefa_thenAtualizarDadosNoBanco() throws Exception {
        // Given: uma tarefa ja salva no banco
        Tarefa existente = tarefaRepository.save(
                new Tarefa("Titulo antigo", "Descricao antiga", false));
        String json = """
                {
                  "titulo": "Titulo novo",
                  "descricao": "Descricao nova",
                  "concluida": true
                }
                """;

        // When
        mockMvc.perform(put("/api/tarefas/" + existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo novo"))
                .andExpect(jsonPath("$.concluida").value(true));

        // Then: os dados persistidos mudaram
        Tarefa atualizada = tarefaRepository.findById(existente.getId()).orElseThrow();
        assertEquals("Titulo novo", atualizada.getTitulo());
        assertEquals("Descricao nova", atualizada.getDescricao());
        assertTrue(atualizada.isConcluida());
    }

    @Test
    void givenTarefaExistente_whenDeleteTarefa_thenRemoverDoBanco() throws Exception {
        // Given
        Tarefa existente = tarefaRepository.save(
                new Tarefa("Estudar JUnit", "Resolver exercicios", false));
        Long id = existente.getId();
        assertTrue(tarefaRepository.existsById(id));

        // When
        mockMvc.perform(delete("/api/tarefas/" + id))
                .andExpect(status().isNoContent());

        // Then
        assertFalse(tarefaRepository.existsById(id));
    }
}
