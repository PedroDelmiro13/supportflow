
const API_BASE_URL = window.SUPPORTFLOW_API_BASE_URL;
const TOKEN_STORAGE_KEY = "supportflow_token";

document.addEventListener("DOMContentLoaded", () => {
    const token = sessionStorage.getItem(TOKEN_STORAGE_KEY);

    if (!token) {
        window.location.href = "index.html";
        return;
    }

    const createForm = document.getElementById("create-ticket-form");
    const ticketsContainer = document.getElementById("tickets-container");
    const createUserBtn = document.getElementById("create-user-btn");
    const logoutBtn = document.getElementById("logout-btn");

    function mostrarMensagem(mensagem) {
        alert(mensagem);
    }

    async function mensagemErro(response) {
        try {
            const body = await response.json();

            return body.message
                || body.mensagem
                || "Não foi possível concluir a operação.";
        } catch {
            return "Não foi possível concluir a operação.";
        }
    }

    async function requisicaoApi(url, options = {}) {
        const metodo = options.method || "GET";

        console.log(`[API] Enviando ${metodo} ${url}`);

        const response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
                ...options.headers
            }
        });

        console.log(
            `[API] ${metodo} ${url} -> HTTP ${response.status}`
        );

        if (response.status === 401 || response.status === 403) {
            sessionStorage.removeItem(TOKEN_STORAGE_KEY);
            window.location.href = "index.html";

            throw new Error(
                "Sessão expirada ou acesso não autorizado. Faça login novamente."
            );
        }

        if (!response.ok) {
            const mensagem = await mensagemErro(response);

            console.error(
                `[API] Erro HTTP ${response.status}:`,
                mensagem
            );

            throw new Error(mensagem);
        }

        return response;
    }

    function limparErros(form) {
        form.querySelectorAll("input, textarea").forEach(input => {
            input.classList.remove("error");
        });

        form.querySelectorAll(".error-message").forEach(error => {
            error.textContent = "";
        });
    }

    function mostrarErroCampo(inputId, mensagem) {
        const input = document.getElementById(inputId);
        const error = document.getElementById(`${inputId}-error`);

        if (input) {
            input.classList.add("error");
        }

        if (error) {
            error.textContent = mensagem;
        }
    }

    function renderizarChamados(chamados) {
        ticketsContainer.innerHTML = "";

        if (!Array.isArray(chamados)) {
            console.error(
                "[API] A resposta da listagem não é uma lista:",
                chamados
            );

            ticketsContainer.textContent =
                "A API retornou um formato inesperado.";

            return;
        }

        console.log(
            `[API] Quantidade de chamados recebidos: ${chamados.length}`,
            chamados
        );

        if (chamados.length === 0) {
            ticketsContainer.innerHTML =
                "<p class='empty-message'>Você ainda não possui chamados.</p>";

            return;
        }

        chamados.forEach(chamado => {
            const item = document.createElement("div");
            item.classList.add("ticket-item");

            const info = document.createElement("div");
            info.classList.add("ticket-info");

            const titulo = document.createElement("h3");
            titulo.textContent = chamado.title;

            const descricao = document.createElement("p");
            descricao.textContent = chamado.description;

            const excluirBtn = document.createElement("button");
            excluirBtn.classList.add("btn-danger");
            excluirBtn.textContent = "Excluir";

            excluirBtn.addEventListener("click", async () => {
                const confirmar = window.confirm(
                    "Deseja realmente excluir este chamado?"
                );

                if (!confirmar) {
                    return;
                }

                excluirBtn.disabled = true;

                try {
                    await requisicaoApi(`/tickets/${chamado.id}`, {
                        method: "DELETE"
                    });

                    await carregarChamados();

                    mostrarMensagem("Chamado excluído com sucesso!");
                } catch (error) {
                    console.error(
                        "[FRONTEND] Erro ao excluir chamado:",
                        error
                    );

                    mostrarMensagem(error.message);
                } finally {
                    excluirBtn.disabled = false;
                }
            });

            info.appendChild(titulo);
            info.appendChild(descricao);

            item.appendChild(info);
            item.appendChild(excluirBtn);

            ticketsContainer.appendChild(item);
        });
    }

    async function carregarChamados() {
        console.log("[FRONTEND] Carregando chamados da API...");

        try {
            const response = await requisicaoApi("/tickets");
            const chamados = await response.json();

            renderizarChamados(chamados);
        } catch (error) {
            console.error(
                "[FRONTEND] Erro ao carregar chamados:",
                error
            );

            ticketsContainer.innerHTML =
                "<p class='empty-message'>Não foi possível carregar os chamados. Verifique o Console.</p>";
        }
    }

    createUserBtn.addEventListener("click", () => {
        window.location.href = "index.html?mode=register";
    });

    createForm.addEventListener("submit", async event => {
        event.preventDefault();
        limparErros(createForm);

        const title = document
            .getElementById("ticket-title")
            .value.trim();

        const description = document
            .getElementById("ticket-desc")
            .value.trim();

        let valido = true;

        if (!title) {
            mostrarErroCampo(
                "ticket-title",
                "O título é obrigatório."
            );

            valido = false;
        }

        if (!description) {
            mostrarErroCampo(
                "ticket-desc",
                "A descrição é obrigatória."
            );

            valido = false;
        }

        if (!valido) {
            return;
        }

        const botaoSubmit = createForm.querySelector(
            'button[type="submit"]'
        );

        if (botaoSubmit) {
            botaoSubmit.disabled = true;
        }

        try {
            console.log("[FRONTEND] Enviando novo chamado:", {
                title,
                description
            });

            const response = await requisicaoApi("/tickets", {
                method: "POST",
                body: JSON.stringify({
                    title,
                    description
                })
            });

            // Alguns endpoints respondem sem corpo (HTTP 204).
            if (response.status !== 204) {
                const resposta = await response.text();

                console.log(
                    "[API] Resposta da criação:",
                    resposta || "(resposta sem corpo)"
                );
            }

            createForm.reset();

            await carregarChamados();

            mostrarMensagem("Chamado criado com sucesso!");
        } catch (error) {
            console.error(
                "[FRONTEND] Erro ao criar chamado:",
                error
            );

            mostrarMensagem(
                `Não foi possível criar o chamado: ${error.message}`
            );
        } finally {
            if (botaoSubmit) {
                botaoSubmit.disabled = false;
            }
        }
    });

    logoutBtn.addEventListener("click", () => {
        sessionStorage.removeItem(TOKEN_STORAGE_KEY);
        window.location.href = "index.html";
    });

    carregarChamados();
});
