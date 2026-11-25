package br.com.ada.tarefas.controller;

import br.com.ada.tarefas.dto.TarefaRequest;
import br.com.ada.tarefas.dto.TarefaResponse;
import br.com.ada.tarefas.service.TarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// O Controller trabalha APENAS com DTOs e delega as operacoes ao Service.
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponse>> listar() {
        List<TarefaResponse> tarefas = tarefaService.listar();
        ResponseEntity<List<TarefaResponse>> resposta = new ResponseEntity<>(tarefas, HttpStatus.OK);
        return resposta;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable Long id) {
        TarefaResponse tarefa = tarefaService.buscarPorId(id);
        ResponseEntity<TarefaResponse> resposta = new ResponseEntity<>(tarefa, HttpStatus.OK);
        return resposta;
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> cadastrar(@RequestBody TarefaRequest dadosRecebidos) {
        TarefaResponse tarefaCriada = tarefaService.cadastrar(dadosRecebidos);
        ResponseEntity<TarefaResponse> resposta = new ResponseEntity<>(tarefaCriada, HttpStatus.CREATED);
        return resposta;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(@PathVariable Long id, @RequestBody TarefaRequest dadosRecebidos) {
        TarefaResponse tarefaAtualizada = tarefaService.atualizar(id, dadosRecebidos);
        ResponseEntity<TarefaResponse> resposta = new ResponseEntity<>(tarefaAtualizada, HttpStatus.OK);
        return resposta;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        ResponseEntity<Void> resposta = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return resposta;
    }
}
