document.addEventListener("DOMContentLoaded", () => {
    if (!sessionStorage.getItem("supportflow_token")) {
        window.location.href = "index.html";
        return;
    }
    const createForm = document.getElementById("create-ticket-form");
    const ticketsContainer = document.getElementById("tickets-container");
    const createUserBtn = document.getElementById("create-user-btn");
    const logoutBtn = document.getElementById("logout-btn");

    // Array provisório para simular o retorno da API (Pronto para GET /tickets)
    let mockTickets = [
        { id: 1, title: "Erro na página principal", description: "O botão de submissão não funciona." },
        { id: 2, title: "Alteração de e-mail", description: "Preciso de mudar o e-mail associado à conta." }
    ];

    createUserBtn.addEventListener("click", () => {
        window.location.href = "index.html?mode=register";
    });

    // Funções de validação visual
    function showError(inputId, message) {
        const inputElement = document.getElementById(inputId);
        const errorElement = document.getElementById(`${inputId}-error`);
        inputElement.classList.add("error");
        errorElement.textContent = message;
    }

    function clearErrors(form) {
        const inputs = form.querySelectorAll("input, textarea");
        const errors = form.querySelectorAll(".error-message");
        inputs.forEach(input => input.classList.remove("error"));
        errors.forEach(error => error.textContent = "");
    }

    // Função para renderizar a lista no HTML
    function renderTickets() {
        ticketsContainer.innerHTML = ""; // Limpa a lista atual

        if (mockTickets.length === 0) {
            ticketsContainer.innerHTML = "<p class='empty-message'>Não possui nenhum chamado aberto.</p>";
            return;
        }

        mockTickets.forEach(ticket => {
            const ticketDiv = document.createElement("div");
            ticketDiv.classList.add("ticket-item");

            ticketDiv.innerHTML = `
                <div class="ticket-info">
                    <h3>${ticket.title}</h3>
                    <p>${ticket.description}</p>
                </div>
                <button class="btn-danger" onclick="deleteTicket(${ticket.id})">Excluir</button>
            `;
            ticketsContainer.appendChild(ticketDiv);
        });
    }

    // Excluir Chamado (Pronto para DELETE /tickets/{id})
    window.deleteTicket = function(id) {
        console.log(`Pronto para envio (Exclusão): DELETE /tickets/${id}`);
        // Na interface local, filtra o array e volta a desenhar a lista
        mockTickets = mockTickets.filter(ticket => ticket.id !== id);
        renderTickets();
    };

    // Criar Chamado (Pronto para POST /tickets)
    createForm.addEventListener("submit", (e) => {
        e.preventDefault();
        clearErrors(createForm);

        const title = document.getElementById("ticket-title").value.trim();
        const description = document.getElementById("ticket-desc").value.trim();
        let isValid = true;

        if (!title) {
            showError("ticket-title", "O título é obrigatório.");
            isValid = false;
        }

        if (!description) {
            showError("ticket-desc", "A descrição é obrigatória.");
            isValid = false;
        }

        if (isValid) {
            const newTicket = { title, description };
            console.log("Dados prontos para envio (Criação):", newTicket);
            
            // Simulação de resposta com sucesso da API
            const newId = mockTickets.length > 0 ? mockTickets[mockTickets.length - 1].id + 1 : 1;
            mockTickets.push({ id: newId, title: newTicket.title, description: newTicket.description });
            
            renderTickets(); // Atualiza a vista
            createForm.reset(); // Limpa os campos de escrita
        }
    });

    // Simular Logout (Pronto para POST /auth/logout)
    logoutBtn.addEventListener("click", () => {
        console.log("Pronto para Logout: Limpar Token e redirecionar");
        window.location.href = "index.html"; // Redireciona para o login
    });

    // Executa o GET simulado ao carregar a página
    renderTickets();
});
