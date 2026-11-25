package br.com.ada.tarefas.controller;

import br.com.ada.tarefas.dto.TarefaRequest;
import br.com.ada.tarefas.dto.TarefaResponse;
import br.com.ada.tarefas.exception.ApiExceptionHandler;
import br.com.ada.tarefas.exception.TarefaNaoEncontradaException;
import br.com.ada.tarefas.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TarefaControllerTest {

    // O Controller conhece o Service, mas NAO usa Entity nem Repository.
    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    private MockMvc mockMvc;

    @BeforeEach
    void configurarMockMvc() {
        // Teste de Controller isolado, sem iniciar toda a aplicacao.
        this.mockMvc = MockMvcBuilders.standaloneSetup(tarefaController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    // TODO: Criar de 2 a 3 testes do Controller.
    // Sugestoes: listar 200; cadastrar 201; buscar tarefa inexistente 404.
    //
    // Dicas:
    // - Ao simular o Service, devolva um TarefaResponse (e nao Tarefa Entity).
    // - Use MockMvc para fazer GET/POST e verificar status e JSON.
    // - Se houver erro, a API responde JSON com codigo, mensagem, timestamp e path.
    // - Nao use @SpringBootTest nem injete Repository aqui.

    @Test
    void givenTarefasExistentes_whenListar_thenRetornarStatus200() throws Exception {
        // Given: o service simulado devolve duas tarefas
        List<TarefaResponse> tarefas = List.of(
                new TarefaResponse(1L, "Estudar JUnit", "Resolver exercicios", false),
                new TarefaResponse(2L, "Estudar Mockito", "Ler a documentacao", true));
        when(tarefaService.listar()).thenReturn(tarefas);

        // When + Then
        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo").value("Estudar JUnit"))
                .andExpect(jsonPath("$[1].concluida").value(true));
    }

    @Test
    void givenTarefaValida_whenCadastrar_thenRetornarStatus201() throws Exception {
        // Given
        TarefaResponse criada = new TarefaResponse(1L, "Estudar JUnit", "Resolver exercicios", false);
        when(tarefaService.cadastrar(any(TarefaRequest.class))).thenReturn(criada);
        String json = """
                {
                  "titulo": "Estudar JUnit",
                  "descricao": "Resolver exercicios",
                  "concluida": false
                }
                """;

        // When + Then
        mockMvc.perform(post("/api/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Estudar JUnit"))
                .andExpect(jsonPath("$.descricao").value("Resolver exercicios"))
                .andExpect(jsonPath("$.concluida").value(false));
    }

    @Test
    void givenTarefaInexistente_whenBuscarPorId_thenRetornarStatus404() throws Exception {
        // Given: o service simulado lanca a excecao de tarefa nao encontrada
        when(tarefaService.buscarPorId(99L))
                .thenThrow(new TarefaNaoEncontradaException("Tarefa nao encontrada: 99"));

        // When + Then: o ApiExceptionHandler traduz a excecao em 404 + JSON de erro
        mockMvc.perform(get("/api/tarefas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("TAREFA_NAO_ENCONTRADA"))
                .andExpect(jsonPath("$.mensagem").value("Tarefa nao encontrada: 99"))
                .andExpect(jsonPath("$.path").value("/api/tarefas/99"));
    }

}

