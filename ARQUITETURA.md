# Arquitetura simples da API de Tarefas

## Responsabilidades

| Camada | O que conhece | O que faz |
|---|---|---|
| Controller | `TarefaRequest`, `TarefaResponse`, `TarefaService` | Recebe JSON e devolve HTTP/JSON. **Nao conhece Entity nem Repository.** |
| Service | DTOs, `Tarefa` Entity, `TarefaRepository` | Aplica regras, valida, chama Repository, converte Entity em DTO. |
| Repository | `Tarefa` Entity | Leitura e escrita no H2 com Spring Data JPA. |
| Exception handler | `ErroResponse`, excecoes | Traduz falhas para HTTP 400/404 com JSON padronizado. |

## Fluxo do cadastro

```text
POST /api/tarefas
  JSON -> TarefaRequest
           |  Controller chama cadastrar(request)
           v
        TarefaService
           |  new Tarefa(...) -> valida campos
           |  tarefaRepository.save(tarefa)
           |  converterParaResponse(tarefaSalva)
           v
        TarefaResponse -> HTTP 201 + JSON
```

## Exemplos simples para os testes

- **TarefaTest:** cria a Entity diretamente (sem Spring, sem banco).
- **TarefaServiceTest:** cria um `TarefaRequest`, simula `TarefaRepository` com Mockito e verifica o `TarefaResponse` retornado.
- **TarefaControllerTest:** simula `TarefaService` devolvendo `TarefaResponse`, usa MockMvc e verifica status/JSON.
- **TarefaIntegracaoTest:** inicializa o contexto Spring, executa a API com MockMvc e usa H2 real em memória.

Os testes do aluno estao vazios intencionalmente: comentarios `TODO` mostram como comecar, sem entregar a solucao.
