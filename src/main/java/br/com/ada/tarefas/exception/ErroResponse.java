package br.com.ada.tarefas.exception;

// Resposta de erro padronizada, semelhante a API de Produtos.
public class ErroResponse {

    private final String codigo;
    private final String mensagem;
    private final String timestamp;
    private final String path;

    public ErroResponse(String codigo, String mensagem, String timestamp, String path) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }
}
