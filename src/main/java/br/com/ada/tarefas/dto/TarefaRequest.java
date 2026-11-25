package br.com.ada.tarefas.dto;

// Representa somente os dados que o cliente envia no POST e no PUT.
// Nao e uma Entity JPA e nao conhece o banco de dados.
public class TarefaRequest {

    private String titulo;
    private String descricao;
    private boolean concluida;

    // O Jackson usa este construtor ao ler o JSON da requisicao.
    public TarefaRequest() {
    }

    // Facilita criar dados nos testes unitarios.
    public TarefaRequest(String titulo, String descricao, boolean concluida) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
