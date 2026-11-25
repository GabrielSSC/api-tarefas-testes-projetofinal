package br.com.ada.tarefas.dto;

// Representa os dados devolvidos pela API.
// Nao e a Entity usada pelo JPA.
public class TarefaResponse {

    private final Long id;
    private final String titulo;
    private final String descricao;
    private final boolean concluida;

    public TarefaResponse(Long id, String titulo, String descricao, boolean concluida) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }
}
