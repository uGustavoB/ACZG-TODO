let tarefas = [];
let indiceEdicao = -1;

const formularioTarefa = document.getElementById("formularioTarefa");
const nome = document.getElementById("nome");
const descricao = document.getElementById("descricao");
const dataTermino = document.getElementById("dataTermino");
const prioridade = document.getElementById("prioridade");
const categoria = document.getElementById("categoria");
const status = document.getElementById("status");
const botaoSalvar = document.getElementById("botaoSalvar");
const botaoCancelar = document.getElementById("botaoCancelar");

const filtroStatus = document.getElementById("filtroStatus");
const botaoLimparFiltros = document.getElementById("botaoLimparFiltros");
const corpoTabelaTarefas = document.getElementById("corpoTabelaTarefas");

function ordenarTarefasPorPrioridade() {
    tarefas.sort((primeira, segunda) => segunda.prioridade - primeira.prioridade);
}

function renderizarTarefas() {
    corpoTabelaTarefas.innerHTML = "";

    const valorFiltroStatus = filtroStatus.value;

    const tarefasFiltradas = tarefas.filter((tarefa) => {
        return !valorFiltroStatus || tarefa.status === valorFiltroStatus;
    });

    if (tarefasFiltradas.length === 0) {
        const linhaVazia = document.createElement("tr");
        linhaVazia.innerHTML = `<td colspan="7">Nenhuma tarefa encontrada.</td>`;
        corpoTabelaTarefas.appendChild(linhaVazia);
        return;
    }

    tarefasFiltradas.forEach((tarefa) => {
        const indiceReal = tarefas.indexOf(tarefa);
        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${tarefa.nome}</td>
            <td>${tarefa.descricao}</td>
            <td>${tarefa.dataTermino}</td>
            <td>${tarefa.prioridade}</td>
            <td>${tarefa.categoria}</td>
            <td>${tarefa.status}</td>
            <td>
                <button onclick="carregarTarefaParaEdicao(${indiceReal})">Editar</button>
                <button onclick="removerTarefa(${indiceReal})">Excluir</button>
            </td>
        `;

        corpoTabelaTarefas.appendChild(linha);
    });
}

function limparFormulario() {
    formularioTarefa.reset();
    indiceEdicao = -1;
    botaoSalvar.textContent = "Salvar Tarefa";
    botaoCancelar.style.display = "none";
}

formularioTarefa.addEventListener("submit", function (evento) {
    evento.preventDefault();

    const novaTarefa = {
        nome: nome.value.trim(),
        descricao: descricao.value.trim(),
        dataTermino: dataTermino.value,
        prioridade: parseInt(prioridade.value),
        categoria: categoria.value.trim(),
        status: status.value
    };

    if (indiceEdicao === -1) {
        tarefas.push(novaTarefa);
    } else {
        tarefas[indiceEdicao] = novaTarefa;
    }

    ordenarTarefasPorPrioridade();
    limparFormulario();
    renderizarTarefas();
});

function carregarTarefaParaEdicao(indice) {
    const tarefa = tarefas[indice];
    if (!tarefa) return;

    indiceEdicao = indice;
    nome.value = tarefa.nome;
    descricao.value = tarefa.descricao;
    dataTermino.value = tarefa.dataTermino;
    prioridade.value = tarefa.prioridade;
    categoria.value = tarefa.categoria;
    status.value = tarefa.status;

    botaoSalvar.textContent = "Atualizar Tarefa";
    botaoCancelar.style.display = "inline-block";
}

function removerTarefa(indice) {
    const confirmar = confirm("Tem certeza que deseja excluir esta tarefa?");
    if (confirmar) {
        tarefas.splice(indice, 1);
        if (indiceEdicao === indice) {
            limparFormulario();
        }
        renderizarTarefas();
    }
}

botaoCancelar.addEventListener("click", function () {
    limparFormulario();
});

filtroStatus.addEventListener("change", renderizarTarefas);

botaoLimparFiltros.addEventListener("click", function () {
    filtroStatus.value = "";
    renderizarTarefas();
});

window.carregarTarefaParaEdicao = carregarTarefaParaEdicao;
window.removerTarefa = removerTarefa;

ordenarTarefasPorPrioridade();
renderizarTarefas();
