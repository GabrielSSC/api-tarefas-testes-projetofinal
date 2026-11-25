package br.com.ada.tarefas.repository;

import br.com.ada.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data JPA já fornece: save, findAll, findById e delete.
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
