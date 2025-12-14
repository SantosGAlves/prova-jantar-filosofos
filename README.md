# prova-jantar-filosofos

# Prova de Programação Paralela e Distribuída: Jantar dos Filósofos

Este repositório contém as soluções implementadas para a avaliação final, abordando problemas de concorrência, *deadlock* e *starvation* em Java.

## 📂 Estrutura do Projeto

* `src/tarefa1`: **Deadlock** (Implementação ingênua).
* `src/tarefa2`: **Prevenção** (Solução por Hierarquia/Ordem de recursos).
* `src/tarefa3`: **Semáforos** (Solução limitando acesso à mesa).
* `src/tarefa4`: **Monitores** (Solução com classe Mesa e `notifyAll`).
* `RELATORIO.md`: Análise comparativa detalhada com gráficos e tabelas.
* `docs/`: Evidências de execução (prints).

## 🚀 Como Executar

### Compilação
Na raiz do projeto, execute:
```bash
javac -d bin src/tarefa*/*.java
Execução dos Testes
Tarefa 1 (Deadlock):

Bash

java -cp bin tarefa1.Main
Tarefa 2 (Hierarquia - 5 min):

Bash

java -cp bin tarefa2.Main
Tarefa 3 (Semáforos - 5 min):

Bash

java -cp bin tarefa3.Main
Tarefa 4 (Monitores - 5 min):

Bash

java -cp bin tarefa4.Main