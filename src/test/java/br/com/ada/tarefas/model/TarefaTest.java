package br.com.ada.tarefas.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TarefaTest {

    // TODO: Criar de 2 a 3 testes para a classe Tarefa.
    // Sugestões: titulo nulo, descricao nula, construtor com dados válidos.
    // Use JUnit 5: assertThrows(), assertEquals(), assertNotNull() etc.
    //
    // Exemplo de nomenclatura (não é um teste pronto):
    // givenTituloNulo_whenCriarTarefa_thenLancarExcecao()
    @Test
    void givenTituloNulo_whenCriarTarefa_thenLancarExcecao() {
        // Given: um titulo nulo e uma descricao valida
        String tituloNulo = null;
        String descricao = "Resolver exercicios da aula";

        // When: criamos a tarefa -> Then: o construtor rejeita o titulo nulo
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> new Tarefa(tituloNulo, descricao, false));

        assertEquals("O titulo nao pode ser nulo.", erro.getMessage());
    }

    @Test
    void givenDescricaoNula_whenCriarTarefa_thenLancarExcecao() {
        // Given: um titulo valido e uma descricao nula
        String titulo = "Estudar JUnit";
        String descricaoNula = null;

        // When + Then
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> new Tarefa(titulo, descricaoNula, false));

        assertEquals("A descricao nao pode ser nula.", erro.getMessage());
    }

    @Test
    void givenDadosValidos_whenCriarTarefa_thenTarefaCriadaCorretamente() {
        // Given
        String titulo = "Estudar JUnit";
        String descricao = "Resolver exercicios da aula 01";

        // When
        Tarefa tarefa = new Tarefa(titulo, descricao, true);

        // Then: todos os campos foram guardados como enviados
        assertEquals("Estudar JUnit", tarefa.getTitulo());
        assertEquals("Resolver exercicios da aula 01", tarefa.getDescricao());
        assertTrue(tarefa.isConcluida());
        assertNull(tarefa.getId()); // o id so e gerado pelo banco ao salvar
    }
}
