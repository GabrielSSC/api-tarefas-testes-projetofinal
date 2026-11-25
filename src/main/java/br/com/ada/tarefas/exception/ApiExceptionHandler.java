package br.com.ada.tarefas.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

// Traduz as excecoes da aplicacao para respostas HTTP consistentes.
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> tratarTarefaNaoEncontrada(
            TarefaNaoEncontradaException erro, HttpServletRequest request) {
        String mensagem = erro.getMessage();
        ResponseEntity<ErroResponse> resposta = criarResposta(
                HttpStatus.NOT_FOUND, "TAREFA_NAO_ENCONTRADA", mensagem, request);
        return resposta;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarDadosInvalidos(
            IllegalArgumentException erro, HttpServletRequest request) {
        String mensagem = erro.getMessage();
        ResponseEntity<ErroResponse> resposta = criarResposta(
                HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", mensagem, request);
        return resposta;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException erro, HttpServletRequest request) {
        String mensagem = "JSON invalido ou incompleto.";
        ResponseEntity<ErroResponse> resposta = criarResposta(
                HttpStatus.BAD_REQUEST, "JSON_INVALIDO", mensagem, request);
        return resposta;
    }

    private ResponseEntity<ErroResponse> criarResposta(
            HttpStatus status, String codigo, String mensagem, HttpServletRequest request) {
        String timestamp = Instant.now().toString();
        String path = request.getRequestURI();
        ErroResponse erroResponse = new ErroResponse(codigo, mensagem, timestamp, path);
        ResponseEntity<ErroResponse> resposta = new ResponseEntity<>(erroResponse, status);
        return resposta;
    }
}
