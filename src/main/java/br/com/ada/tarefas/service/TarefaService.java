package br.com.ada.tarefas.service;

import br.com.ada.tarefas.dto.TarefaRequest;
import br.com.ada.tarefas.dto.TarefaResponse;
import br.com.ada.tarefas.exception.TarefaNaoEncontradaException;
import br.com.ada.tarefas.model.Tarefa;
import br.com.ada.tarefas.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<TarefaResponse> listar() {
        List<Tarefa> tarefasEncontradas = tarefaRepository.findAll();
        List<TarefaResponse> respostas = new ArrayList<>();

        for (Tarefa tarefa : tarefasEncontradas) {
            TarefaResponse resposta = converterParaResponse(tarefa);
            respostas.add(resposta);
        }

        return respostas;
    }

    public TarefaResponse buscarPorId(Long id) {
        Tarefa tarefaEncontrada = buscarEntidadePorId(id);
        TarefaResponse resposta = converterParaResponse(tarefaEncontrada);
        return resposta;
    }

    public TarefaResponse cadastrar(TarefaRequest dadosRecebidos) {
        // 1. Ler o DTO recebido da API.
        String titulo = dadosRecebidos.getTitulo();
        String descricao = dadosRecebidos.getDescricao();
        boolean concluida = dadosRecebidos.isConcluida();

        // 2. Criar a Entity. O construtor verifica titulo e descricao nulos.
        Tarefa novaTarefa = new Tarefa(titulo, descricao, concluida);

        // 3. Guardar no banco utilizando o Repository.
        Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);

        // 4. Converter a Entity salva num DTO de resposta.
        TarefaResponse resposta = converterParaResponse(tarefaSalva);
        return resposta;
    }

    public TarefaResponse atualizar(Long id, TarefaRequest dadosRecebidos) {
        Tarefa tarefaExistente = buscarEntidadePorId(id);

        String novoTitulo = dadosRecebidos.getTitulo();
        String novaDescricao = dadosRecebidos.getDescricao();
        boolean novaConclusao = dadosRecebidos.isConcluida();

        tarefaExistente.atualizarDados(novoTitulo, novaDescricao, novaConclusao);
        Tarefa tarefaAtualizada = tarefaRepository.save(tarefaExistente);

        TarefaResponse resposta = converterParaResponse(tarefaAtualizada);
        return resposta;
    }

    public void excluir(Long id) {
        Tarefa tarefaEncontrada = buscarEntidadePorId(id);
        tarefaRepository.delete(tarefaEncontrada);
    }

    // Este metodo so e utilizado pelo Service. O Controller nao recebe Entity.
    private Tarefa buscarEntidadePorId(Long id) {
        Optional<Tarefa> resultado = tarefaRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new TarefaNaoEncontradaException("Tarefa nao encontrada: " + id);
        }

        Tarefa tarefaEncontrada = resultado.get();
        return tarefaEncontrada;
    }

    // Conversao centralizada no Service: Entity -> DTO de resposta.
    private TarefaResponse converterParaResponse(Tarefa tarefa) {
        Long id = tarefa.getId();
        String titulo = tarefa.getTitulo();
        String descricao = tarefa.getDescricao();
        boolean concluida = tarefa.isConcluida();

        TarefaResponse resposta = new TarefaResponse(id, titulo, descricao, concluida);
        return resposta;
    }
}
