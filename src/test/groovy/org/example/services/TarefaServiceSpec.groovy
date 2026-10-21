package org.example.services

import org.example.model.Tarefa
import org.example.model.TarefaStatus
import spock.lang.Specification

import java.time.LocalDateTime
import java.util.ArrayList
import java.util.List

class TarefaServiceSpec extends Specification {

    private Tarefa criarTarefa(String nome, int prioridade, String categoria, TarefaStatus status) {
        return new Tarefa(
                nome,
                "Descrição padrão",
                LocalDateTime.of(2026, 12, 31, 23, 59),
                prioridade,
                categoria,
                status
        )
    }

    // ==========================================
    // Criacao de Tarefas (Create)
    // ==========================================

    void "deve adicionar tarefa com prioridade maior e posiciona-la no inicio da lista"() {
        given: "uma lista de tarefas contendo uma tarefa com prioridade 2"
        Tarefa tarefaPrioridade2 = criarTarefa("Tarefa Existente", 2, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefaPrioridade2))
        Tarefa novaTarefa = criarTarefa("Tarefa Urgente", 5, "Trabalho", TarefaStatus.TODO)

        when: "a nova tarefa de prioridade 5 e adicionada a lista"
        TarefaService.adicionarTarefa(tarefas, novaTarefa)

        then: "a lista deve conter 2 tarefas"
        tarefas.size() == 2

        and: "a tarefa de prioridade 5 deve estar no indice 0 e a de prioridade 2 no indice 1"
        tarefas.get(0) == novaTarefa
        tarefas.get(0).getPrioridade() == 5
        tarefas.get(1) == tarefaPrioridade2
        tarefas.get(1).getPrioridade() == 2
    }

    void "deve adicionar tarefa com prioridade menor e posiciona-la ao final da lista"() {
        given: "uma lista contendo tarefas com prioridades 5 e 3"
        Tarefa tarefaPrioridade5 = criarTarefa("Tarefa Alta", 5, "Trabalho", TarefaStatus.TODO)
        Tarefa tarefaPrioridade3 = criarTarefa("Tarefa Media", 3, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefaPrioridade5, tarefaPrioridade3))
        Tarefa novaTarefa = criarTarefa("Tarefa Baixa", 1, "Trabalho", TarefaStatus.TODO)

        when: "a nova tarefa de prioridade 1 e adicionada a lista"
        TarefaService.adicionarTarefa(tarefas, novaTarefa)

        then: "a lista deve conter 3 tarefas"
        tarefas.size() == 3

        and: "a tarefa de prioridade 1 deve estar na ultima posicao da lista"
        tarefas.get(2) == novaTarefa
        tarefas.get(2).getPrioridade() == 1
        tarefas.get(0) == tarefaPrioridade5
        tarefas.get(1) == tarefaPrioridade3
    }

    void "deve adicionar tarefa com sucesso em uma lista inicialmente vazia"() {
        given: "uma lista de tarefas vazia e uma nova tarefa valida"
        List<Tarefa> tarefas = new ArrayList<Tarefa>()
        Tarefa novaTarefa = criarTarefa("Primeira Tarefa", 3, "Estudos", TarefaStatus.TODO)

        when: "a nova tarefa e adicionada a lista"
        TarefaService.adicionarTarefa(tarefas, novaTarefa)

        then: "a lista deve conter exatamente 1 tarefa"
        tarefas.size() == 1

        and: "a tarefa inserida deve corresponder a nova tarefa informada"
        tarefas.get(0) == novaTarefa
        tarefas.get(0).getNome() == "Primeira Tarefa"
        tarefas.get(0).getPrioridade() == 3
        tarefas.get(0).getCategoria() == "Estudos"
        tarefas.get(0).getStatus() == TarefaStatus.TODO
    }

    void "nao deve lancar excecao ao tentar adicionar tarefa em lista nula"() {
        given: "uma lista nula e uma nova tarefa valida"
        List<Tarefa> tarefasNulas = null
        Tarefa novaTarefa = criarTarefa("Tarefa Valida", 4, "Geral", TarefaStatus.TODO)

        when: "o metodo adicionarTarefa e chamado com lista nula"
        TarefaService.adicionarTarefa(tarefasNulas, novaTarefa)

        then: "nenhuma excecao deve ser lancada"
        notThrown(NullPointerException)
    }

    void "nao deve modificar a lista nem lancar excecao ao tentar adicionar tarefa nula"() {
        given: "uma lista contendo uma tarefa existente e uma nova tarefa nula"
        Tarefa tarefaExistente = criarTarefa("Tarefa Existente", 3, "Geral", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefaExistente))
        Tarefa novaTarefaNula = null

        when: "o metodo adicionarTarefa e chamado com tarefa nula"
        TarefaService.adicionarTarefa(tarefas, novaTarefaNula)

        then: "nenhuma excecao deve ser lancada e a lista deve permanecer inalterada"
        notThrown(NullPointerException)
        tarefas.size() == 1
        tarefas.get(0) == tarefaExistente
    }

    // ==========================================
    // Atualizacao de Tarefas (Update)
    // ==========================================

    void "deve atualizar tarefa existente alterando seus atributos e elevando sua prioridade com reordenacao"() {
        given: "uma lista com 2 tarefas: Tarefa A (prioridade 4, indice 0) e Tarefa B (prioridade 2, indice 1)"
        Tarefa tarefaA = criarTarefa("Tarefa A", 4, "Trabalho", TarefaStatus.TODO)
        Tarefa tarefaB = criarTarefa("Tarefa B", 2, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefaA, tarefaB))
        Tarefa tarefaBAtualizada = criarTarefa("Tarefa B Atualizada", 5, "Trabalho Prioritario", TarefaStatus.DOING)

        when: "a tarefa no indice 1 e atualizada com nova prioridade 5"
        boolean sucesso = TarefaService.atualizarTarefa(tarefas, 1, tarefaBAtualizada)

        then: "a atualizacao deve retornar sucesso"
        sucesso

        and: "a lista deve permanecer com 2 tarefas e a tarefaBAtualizada deve ocupar o indice 0"
        tarefas.size() == 2
        tarefas.get(0) == tarefaBAtualizada
        tarefas.get(0).getNome() == "Tarefa B Atualizada"
        tarefas.get(0).getCategoria() == "Trabalho Prioritario"
        tarefas.get(0).getStatus() == TarefaStatus.DOING
        tarefas.get(0).getPrioridade() == 5

        and: "a Tarefa A deve passar para o indice 1"
        tarefas.get(1) == tarefaA
        tarefas.get(1).getPrioridade() == 4
    }

    void "deve atualizar tarefa existente mantendo a prioridade e garantindo permanencia dos dados alterados"() {
        given: "uma lista contendo 2 tarefas"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 3, "Trabalho", TarefaStatus.TODO)
        Tarefa tarefa2 = criarTarefa("Tarefa 2", 1, "Estudos", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefa1, tarefa2))
        Tarefa tarefa1Modificada = criarTarefa("Tarefa 1 Modificada", 3, "Trabalho Concluido", TarefaStatus.DONE)

        when: "a tarefa no indice 0 e atualizada mantendo a prioridade 3"
        boolean sucesso = TarefaService.atualizarTarefa(tarefas, 0, tarefa1Modificada)

        then: "a operacao deve ser bem-sucedida"
        sucesso

        and: "os dados atualizados devem ser refletidos no indice correspondente"
        tarefas.size() == 2
        tarefas.get(0) == tarefa1Modificada
        tarefas.get(0).getNome() == "Tarefa 1 Modificada"
        tarefas.get(0).getCategoria() == "Trabalho Concluido"
        tarefas.get(0).getStatus() == TarefaStatus.DONE
        tarefas.get(1) == tarefa2
    }

    void "deve retornar false e nao alterar a lista ao tentar atualizar sobre lista vazia"() {
        given: "uma lista de tarefas vazia e uma tarefa para atualizacao"
        List<Tarefa> tarefasVazias = new ArrayList<Tarefa>()
        Tarefa tarefaAtualizada = criarTarefa("Tarefa Invalida", 3, "Trabalho", TarefaStatus.TODO)

        when: "a rotina de atualizacao e executada sobre a lista vazia"
        boolean sucesso = TarefaService.atualizarTarefa(tarefasVazias, 0, tarefaAtualizada)

        then: "deve retornar false, nao lancar excecao e a lista deve permanecer vazia"
        !sucesso
        tarefasVazias.isEmpty()
        notThrown(Exception)
    }

    void "deve retornar false ao tentar atualizar com indice fora dos limites da lista"() {
        given: "uma lista contendo 1 tarefa"
        Tarefa tarefa = criarTarefa("Tarefa Unica", 3, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefa))
        Tarefa tarefaAtualizada = criarTarefa("Tarefa Atualizada", 4, "Trabalho", TarefaStatus.DOING)

        when: "a atualizacao e tentada com indice negativo e com indice alem do limite"
        boolean sucessoIndiceNegativo = TarefaService.atualizarTarefa(tarefas, -1, tarefaAtualizada)
        boolean sucessoIndiceAlemLimite = TarefaService.atualizarTarefa(tarefas, 1, tarefaAtualizada)

        then: "ambas as tentativas devem falhar e a lista deve permanecer inalterada"
        !sucessoIndiceNegativo
        !sucessoIndiceAlemLimite
        tarefas.size() == 1
        tarefas.get(0) == tarefa
    }

    void "deve retornar false e nao lancar excecao ao tentar atualizar com lista nula ou tarefa nula"() {
        given: "uma lista valida, uma tarefa valida, uma lista nula e uma tarefa nula"
        Tarefa tarefaValida = criarTarefa("Tarefa Valida", 3, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefasValidas = new ArrayList<Tarefa>(List.of(tarefaValida))
        List<Tarefa> tarefasNulas = null
        Tarefa tarefaNula = null

        when: "a atualizacao e tentada com lista nula ou tarefa nula"
        boolean sucessoListaNula = TarefaService.atualizarTarefa(tarefasNulas, 0, tarefaValida)
        boolean sucessoTarefaNula = TarefaService.atualizarTarefa(tarefasValidas, 0, tarefaNula)

        then: "deve retornar false para ambas as chamadas sem lancar excecao"
        !sucessoListaNula
        !sucessoTarefaNula
        tarefasValidas.size() == 1
        tarefasValidas.get(0) == tarefaValida
        notThrown(NullPointerException)
    }

    // ==========================================
    // Filtro por Categoria
    // ==========================================

    void "deve filtrar tarefas por categoria ignorando diferencas de case e espacos nas extremidades"() {
        given: "uma lista de tarefas contendo categorias Trabalho, TRABALHO  e Estudos"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 3, "Trabalho", TarefaStatus.TODO)
        Tarefa tarefa2 = criarTarefa("Tarefa 2", 2, "TRABALHO ", TarefaStatus.DOING)
        Tarefa tarefa3 = criarTarefa("Tarefa 3", 1, "Estudos", TarefaStatus.DONE)
        List<Tarefa> tarefas = List.of(tarefa1, tarefa2, tarefa3)

        when: "o filtro por categoria e executado com ' trabalho'"
        List<Tarefa> resultado = TarefaService.filtrarPorCategoria(tarefas, " trabalho")

        then: "o resultado deve conter apenas as tarefas da categoria informada"
        resultado.size() == 2
        resultado.contains(tarefa1)
        resultado.contains(tarefa2)
        !resultado.contains(tarefa3)
    }

    void "deve retornar lista vazia quando a categoria pesquisada nao for encontrada"() {
        given: "uma lista de tarefas com categorias conhecidas"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 3, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = List.of(tarefa1)

        when: "o filtro por categoria e executado para uma categoria inexistente"
        List<Tarefa> resultado = TarefaService.filtrarPorCategoria(tarefas, "Lazer")

        then: "o resultado deve ser uma lista vazia"
        resultado.isEmpty()
    }

    void "deve retornar lista vazia ao filtrar por categoria quando a categoria for nula"() {
        given: "uma lista de tarefas valida e uma categoria nula"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 3, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = List.of(tarefa1)
        String categoriaNula = null

        when: "o filtro por categoria e executado com categoria nula"
        List<Tarefa> resultado = TarefaService.filtrarPorCategoria(tarefas, categoriaNula)

        then: "deve retornar uma lista vazia sem lancar excecao"
        resultado.isEmpty()
    }

    void "deve retornar lista vazia ao filtrar por categoria quando a lista de tarefas for nula"() {
        given: "uma lista de tarefas nula"
        List<Tarefa> tarefasNulas = null

        when: "o filtro por categoria e executado"
        List<Tarefa> resultado = TarefaService.filtrarPorCategoria(tarefasNulas, "Trabalho")

        then: "deve retornar uma lista vazia sem lancar NullPointerException"
        resultado.isEmpty()
    }

    void "deve ignorar tarefas que possuem categoria nula ao filtrar por categoria"() {
        given: "uma lista contendo uma tarefa com categoria nula e outra com categoria valida"
        Tarefa tarefaSemCategoria = criarTarefa("Tarefa Sem Categoria", 2, null, TarefaStatus.TODO)
        Tarefa tarefaValida = criarTarefa("Tarefa Valida", 4, "Pessoal", TarefaStatus.TODO)
        List<Tarefa> tarefas = List.of(tarefaSemCategoria, tarefaValida)

        when: "o filtro por categoria e executado buscando 'Pessoal'"
        List<Tarefa> resultado = TarefaService.filtrarPorCategoria(tarefas, "Pessoal")

        then: "apenas a tarefa com categoria valida correspondente deve ser retornada"
        resultado.size() == 1
        resultado.contains(tarefaValida)
        !resultado.contains(tarefaSemCategoria)
    }

    // ==========================================
    // Filtro por Prioridade
    // ==========================================

    void "deve filtrar tarefas pela prioridade informada"() {
        given: "uma lista com 3 tarefas de prioridades 1, 3 e 5"
        Tarefa tarefaPrioridade1 = criarTarefa("Tarefa Baixa", 1, "Geral", TarefaStatus.TODO)
        Tarefa tarefaPrioridade3 = criarTarefa("Tarefa Media", 3, "Geral", TarefaStatus.DOING)
        Tarefa tarefaPrioridade5 = criarTarefa("Tarefa Alta", 5, "Geral", TarefaStatus.DONE)
        List<Tarefa> tarefas = List.of(tarefaPrioridade1, tarefaPrioridade3, tarefaPrioridade5)

        when: "o filtro por prioridade e executado para o valor 3"
        List<Tarefa> resultado = TarefaService.filtrarPorPrioridade(tarefas, 3)

        then: "o resultado deve conter apenas a tarefa com prioridade 3"
        resultado.size() == 1
        resultado.get(0).getPrioridade() == 3
        resultado.contains(tarefaPrioridade3)
    }

    void "deve retornar lista vazia quando nenhuma tarefa possuir a prioridade buscada"() {
        given: "uma lista de tarefas com prioridades 1 e 5"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 1, "Geral", TarefaStatus.TODO)
        Tarefa tarefa5 = criarTarefa("Tarefa 5", 5, "Geral", TarefaStatus.DONE)
        List<Tarefa> tarefas = List.of(tarefa1, tarefa5)

        when: "o filtro por prioridade e executado para a prioridade 2"
        List<Tarefa> resultado = TarefaService.filtrarPorPrioridade(tarefas, 2)

        then: "o resultado deve ser uma lista vazia"
        resultado.isEmpty()
    }

    void "deve retornar lista vazia ao filtrar por prioridade quando a lista de tarefas for nula"() {
        given: "uma lista de tarefas nula"
        List<Tarefa> tarefasNulas = null

        when: "o filtro por prioridade e executado"
        List<Tarefa> resultado = TarefaService.filtrarPorPrioridade(tarefasNulas, 3)

        then: "deve retornar uma lista vazia sem lancar NullPointerException"
        resultado.isEmpty()
    }

    // ==========================================
    // Filtro por Status
    // ==========================================

    void "deve filtrar tarefas pelo status correspondente"() {
        given: "uma lista contendo tarefas nos status TODO, DOING e DONE"
        Tarefa tarefaTodo = criarTarefa("Tarefa TODO", 2, "Geral", TarefaStatus.TODO)
        Tarefa tarefaDoing1 = criarTarefa("Tarefa DOING 1", 3, "Geral", TarefaStatus.DOING)
        Tarefa tarefaDoing2 = criarTarefa("Tarefa DOING 2", 4, "Geral", TarefaStatus.DOING)
        Tarefa tarefaDone = criarTarefa("Tarefa DONE", 5, "Geral", TarefaStatus.DONE)
        List<Tarefa> tarefas = List.of(tarefaTodo, tarefaDoing1, tarefaDoing2, tarefaDone)

        when: "o filtro por status e executado para TarefaStatus.DOING"
        List<Tarefa> resultado = TarefaService.filtrarPorStatus(tarefas, TarefaStatus.DOING)

        then: "apenas as tarefas com status DOING devem ser retornadas"
        resultado.size() == 2
        resultado.contains(tarefaDoing1)
        resultado.contains(tarefaDoing2)
        !resultado.contains(tarefaTodo)
        !resultado.contains(tarefaDone)
    }

    void "deve retornar lista vazia quando nenhuma tarefa possuir o status buscado"() {
        given: "uma lista contendo tarefas apenas com status TODO e DOING"
        Tarefa tarefaTodo = criarTarefa("Tarefa TODO", 2, "Geral", TarefaStatus.TODO)
        Tarefa tarefaDoing = criarTarefa("Tarefa DOING", 3, "Geral", TarefaStatus.DOING)
        List<Tarefa> tarefas = List.of(tarefaTodo, tarefaDoing)

        when: "o filtro por status e executado para TarefaStatus.DONE"
        List<Tarefa> resultado = TarefaService.filtrarPorStatus(tarefas, TarefaStatus.DONE)

        then: "o resultado deve ser uma lista vazia"
        resultado.isEmpty()
    }

    void "deve retornar lista vazia ao filtrar por status quando o status for nulo"() {
        given: "uma lista de tarefas valida e status nulo"
        Tarefa tarefaTodo = criarTarefa("Tarefa TODO", 2, "Geral", TarefaStatus.TODO)
        List<Tarefa> tarefas = List.of(tarefaTodo)
        TarefaStatus statusNulo = null

        when: "o filtro por status e executado com status nulo"
        List<Tarefa> resultado = TarefaService.filtrarPorStatus(tarefas, statusNulo)

        then: "deve retornar uma lista vazia sem lancar excecao"
        resultado.isEmpty()
    }

    void "deve retornar lista vazia ao filtrar por status quando a lista de tarefas for nula"() {
        given: "uma lista de tarefas nula"
        List<Tarefa> tarefasNulas = null

        when: "o filtro por status e executado"
        List<Tarefa> resultado = TarefaService.filtrarPorStatus(tarefasNulas, TarefaStatus.DOING)

        then: "deve retornar uma lista vazia sem lancar NullPointerException"
        resultado.isEmpty()
    }

    // ==========================================
    // Contagem por Status
    // ==========================================

    void "deve contar corretamente as tarefas para cada status"() {
        given: "uma lista contendo 2 tarefas TODO, 1 DOING e 0 DONE"
        Tarefa tarefaTodo1 = criarTarefa("Tarefa TODO 1", 1, "Geral", TarefaStatus.TODO)
        Tarefa tarefaTodo2 = criarTarefa("Tarefa TODO 2", 2, "Geral", TarefaStatus.TODO)
        Tarefa tarefaDoing = criarTarefa("Tarefa DOING", 3, "Geral", TarefaStatus.DOING)
        List<Tarefa> tarefas = List.of(tarefaTodo1, tarefaTodo2, tarefaDoing)

        when: "o metodo contarPorStatus e chamado para cada um dos status"
        long totalTodo = TarefaService.contarPorStatus(tarefas, TarefaStatus.TODO)
        long totalDoing = TarefaService.contarPorStatus(tarefas, TarefaStatus.DOING)
        long totalDone = TarefaService.contarPorStatus(tarefas, TarefaStatus.DONE)

        then: "as quantidades contadas devem coincidir exatamente com os dados da lista"
        totalTodo == 2L
        totalDoing == 1L
        totalDone == 0L
    }

    void "deve retornar 0 ao contar tarefas para status que nao possui ocorrencias"() {
        given: "uma lista contendo apenas tarefas TODO"
        Tarefa tarefaTodo = criarTarefa("Tarefa TODO", 1, "Geral", TarefaStatus.TODO)
        List<Tarefa> tarefas = List.of(tarefaTodo)

        when: "o metodo contarPorStatus e chamado para TarefaStatus.DONE"
        long totalDone = TarefaService.contarPorStatus(tarefas, TarefaStatus.DONE)

        then: "o total retornado deve ser zero"
        totalDone == 0L
    }

    void "deve retornar 0 ao contar tarefas quando o status for nulo"() {
        given: "uma lista valida de tarefas e um status nulo"
        Tarefa tarefaTodo = criarTarefa("Tarefa TODO", 1, "Geral", TarefaStatus.TODO)
        List<Tarefa> tarefas = List.of(tarefaTodo)
        TarefaStatus statusNulo = null

        when: "o metodo contarPorStatus e acionado com status nulo"
        long total = TarefaService.contarPorStatus(tarefas, statusNulo)

        then: "o total retornado deve ser zero"
        total == 0L
    }

    void "deve retornar 0 ao contar tarefas quando a lista de tarefas for nula"() {
        given: "uma lista de tarefas nula"
        List<Tarefa> tarefasNulas = null

        when: "o metodo contarPorStatus e acionado"
        long total = TarefaService.contarPorStatus(tarefasNulas, TarefaStatus.TODO)

        then: "o total retornado deve ser zero sem lancar NullPointerException"
        total == 0L
    }

    // ==========================================
    // Exclusao de Tarefas (Delete)
    // ==========================================

    void "deve excluir tarefa em indice valido reduzindo a lista e retornando o elemento correto"() {
        given: "uma lista com 2 tarefas: Tarefa 1 (indice 0) e Tarefa 2 (indice 1)"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 3, "Trabalho", TarefaStatus.TODO)
        Tarefa tarefa2 = criarTarefa("Tarefa 2", 2, "Estudos", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefa1, tarefa2))

        when: "a tarefa de indice 0 e removida"
        Tarefa tarefaRemovida = TarefaService.removerTarefa(tarefas, 0)

        then: "a tarefa retornada deve ser a Tarefa 1"
        tarefaRemovida == tarefa1
        tarefaRemovida.getNome() == "Tarefa 1"

        and: "a lista deve conter apenas 1 tarefa restante sendo a Tarefa 2"
        tarefas.size() == 1
        tarefas.get(0) == tarefa2
        !tarefas.contains(tarefa1)
    }

    void "deve excluir tarefa no final da lista quando o ultimo indice for informado"() {
        given: "uma lista com 2 tarefas"
        Tarefa tarefa1 = criarTarefa("Tarefa 1", 3, "Trabalho", TarefaStatus.TODO)
        Tarefa tarefa2 = criarTarefa("Tarefa 2", 2, "Estudos", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefa1, tarefa2))

        when: "a tarefa do indice 1 (ultimo elemento) e removida"
        Tarefa tarefaRemovida = TarefaService.removerTarefa(tarefas, 1)

        then: "a tarefa retornada deve ser a Tarefa 2"
        tarefaRemovida == tarefa2

        and: "a lista deve restar apenas com a Tarefa 1"
        tarefas.size() == 1
        tarefas.get(0) == tarefa1
    }

    void "deve retornar null e nao alterar a lista ao tentar excluir sobre lista vazia"() {
        given: "uma lista de tarefas vazia"
        List<Tarefa> tarefasVazias = new ArrayList<Tarefa>()

        when: "a operacao de remocao e acionada sobre a lista vazia"
        Tarefa resultado = TarefaService.removerTarefa(tarefasVazias, 0)

        then: "deve retornar null sem lancar excecao e a lista deve permanecer vazia"
        resultado == null
        tarefasVazias.isEmpty()
        notThrown(Exception)
    }

    void "deve retornar null e nao alterar a lista ao tentar excluir com indice fora dos limites"() {
        given: "uma lista com 1 tarefa"
        Tarefa tarefa = criarTarefa("Tarefa Unica", 3, "Trabalho", TarefaStatus.TODO)
        List<Tarefa> tarefas = new ArrayList<Tarefa>(List.of(tarefa))

        when: "a remocao e tentada com indice negativo e com indice alem do limite"
        Tarefa resultadoNegativo = TarefaService.removerTarefa(tarefas, -1)
        Tarefa resultadoAlemLimite = TarefaService.removerTarefa(tarefas, 1)

        then: "ambas as tentativas devem retornar null e nao lancar excecao"
        resultadoNegativo == null
        resultadoAlemLimite == null

        and: "a lista deve permanecer com a tarefa original"
        tarefas.size() == 1
        tarefas.get(0) == tarefa
        notThrown(IndexOutOfBoundsException)
    }

    void "deve retornar null e nao lancar excecao ao tentar excluir sobre lista nula"() {
        given: "uma lista nula de tarefas"
        List<Tarefa> tarefasNulas = null

        when: "a remocao e tentada com a lista nula"
        Tarefa resultado = TarefaService.removerTarefa(tarefasNulas, 0)

        then: "deve retornar null sem disparar NullPointerException"
        resultado == null
        notThrown(NullPointerException)
    }
}
