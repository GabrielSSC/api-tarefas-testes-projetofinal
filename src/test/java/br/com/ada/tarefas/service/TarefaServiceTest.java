package br.com.ada.tarefas.service;

import br.com.ada.tarefas.dto.TarefaRequest;
import br.com.ada.tarefas.dto.TarefaResponse;
import br.com.ada.tarefas.exception.TarefaNaoEncontradaException;
import br.com.ada.tarefas.model.Tarefa;
import br.com.ada.tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    // Simula o Repository. Nenhum banco H2 e iniciado.
    @Mock
    private TarefaRepository tarefaRepository;

    // Cria o Service com o Repository simulado.
    @InjectMocks
    private TarefaService tarefaService;

    // TODO: Criar de 2 a 3 testes do Service.
    // Sugestoes: cadastrar; buscarPorId existente; buscarPorId inexistente.
    //
    // Dicas para iniciantes:
    // - Para cadastrar, crie um TarefaRequest e simule save() devolvendo uma Tarefa.
    // - Para buscar, simule findById() devolvendo Optional.of(tarefa) ou Optional.empty().
    // - O Service devolve TarefaResponse (DTO), nao a Entity do Repository.
    // - Verifique o TarefaResponse com assertEquals(), ou a chamada com verify().
    // - Nunca use @SpringBootTest nesta parte.

    @Test
    void givenTarefaValida_whenCadastrar_thenSalvarTarefa() {
        // Given: um DTO de entrada valido; o mock devolve a propria entidade recebida
        TarefaRequest request = new TarefaRequest("Estudar JUnit", "Resolver exercicios", false);
        when(tarefaRepository.save(any(Tarefa.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        // When
        TarefaResponse resposta = tarefaService.cadastrar(request);

        // Then 1: o service chamou save() com a entidade montada a partir do DTO
        ArgumentCaptor<Tarefa> captor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(captor.capture());
        Tarefa tarefaEnviada = captor.getValue();
        assertEquals("Estudar JUnit", tarefaEnviada.getTitulo());
        assertEquals("Resolver exercicios", tarefaEnviada.getDescricao());
        assertFalse(tarefaEnviada.isConcluida());

        // Then 2: o DTO de resposta reflete os dados salvos
        assertEquals("Estudar JUnit", resposta.getTitulo());
        assertEquals("Resolver exercicios", resposta.getDescricao());
        assertFalse(resposta.isConcluida());
    }

    @Test
    void givenTarefaExistente_whenBuscarPorId_thenRetornarResponseComDadosEsperados() {
        // Given: o repositorio simulado devolve uma tarefa com id 1
        Tarefa tarefa = new Tarefa("Estudar JUnit", "Resolver exercicios", true);
        ReflectionTestUtils.setField(tarefa, "id", 1L); // Tarefa nao tem setId
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        // When
        TarefaResponse resposta = tarefaService.buscarPorId(1L);

        // Then
        assertEquals(1L, resposta.getId());
        assertEquals("Estudar JUnit", resposta.getTitulo());
        assertEquals("Resolver exercicios", resposta.getDescricao());
        assertTrue(resposta.isConcluida());
    }

    @Test
    void givenTarefaInexistente_whenBuscarPorId_thenLancarExcecao() {
        // Given: o repositorio simulado nao encontra o id 99
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then
        TarefaNaoEncontradaException erro = assertThrows(
                TarefaNaoEncontradaException.class,
                () -> tarefaService.buscarPorId(99L));

        assertEquals("Tarefa nao encontrada: 99", erro.getMessage());
    }
}
