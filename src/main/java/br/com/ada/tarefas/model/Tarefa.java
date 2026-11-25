package br.com.ada.tarefas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private boolean concluida;

    // Construtor sem argumentos usado pelo JPA.
    public Tarefa() {
    }

    // Construtor usado nos exercícios de teste unitário.
    public Tarefa(String titulo, String descricao, boolean concluida) {
        validarCampos(titulo, descricao);
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    // As validações estão em um método simples para serem reutilizadas.
    private void validarCampos(String titulo, String descricao) {
        if (titulo == null) {
            throw new IllegalArgumentException("O titulo nao pode ser nulo.");
        }

        if (descricao == null) {
            throw new IllegalArgumentException("A descricao nao pode ser nula.");
        }
    }

    public void atualizarDados(String novoTitulo, String novaDescricao, boolean novaConclusao) {
        validarCampos(novoTitulo, novaDescricao);
        this.titulo = novoTitulo;
        this.descricao = novaDescricao;
        this.concluida = novaConclusao;
    }

    public Long getId() {
        return id;
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
