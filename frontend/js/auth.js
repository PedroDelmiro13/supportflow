
const API_BASE_URL = window.SUPPORTFLOW_API_BASE_URL;
const TOKEN_STORAGE_KEY = "supportflow_token";

document.addEventListener("DOMContentLoaded", () => {
    const loginScreen = document.getElementById("login-screen");
    const registerScreen = document.getElementById("register-screen");
    const goToRegister = document.getElementById("go-to-register");
    const goToLogin = document.getElementById("go-to-login");
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");

    const isRegisterMode =
        new URLSearchParams(window.location.search).get("mode") === "register";

    if (sessionStorage.getItem(TOKEN_STORAGE_KEY) && !isRegisterMode) {
        window.location.href = "chamados.html";
        return;
    }

    if (isRegisterMode) {
        loginScreen.classList.add("hidden");
        registerScreen.classList.remove("hidden");
    }

    goToRegister.addEventListener("click", (event) => {
        event.preventDefault();
        clearFeedback();
        loginScreen.classList.add("hidden");
        registerScreen.classList.remove("hidden");
    });

    goToLogin.addEventListener("click", (event) => {
        event.preventDefault();
        clearFeedback();
        registerScreen.classList.add("hidden");
        loginScreen.classList.remove("hidden");
    });

    function showError(inputId, message) {
        document.getElementById(inputId).classList.add("error");
        document.getElementById(`${inputId}-error`).textContent = message;
    }

    function clearErrors(form) {
        form.querySelectorAll("input").forEach((input) => {
            input.classList.remove("error");
        });

        form.querySelectorAll(".error-message").forEach((error) => {
            error.textContent = "";
        });
    }

    function setFeedback(id, message, type) {
        const feedback = document.getElementById(id);
        feedback.textContent = message;
        feedback.className = `form-feedback ${type}`;
    }

    function clearFeedback() {
        ["login-feedback", "register-feedback"].forEach((id) => {
            const feedback = document.getElementById(id);
            feedback.textContent = "";
            feedback.className = "form-feedback";
        });
    }

    function isValidEmail(email) {
        return /^\S+@\S+\.\S+$/.test(email);
    }

    async function getErrorMessage(response) {
        try {
            const body = await response.json();
            return body.message
                || body.mensagem
                || "Não foi possível concluir a operação.";
        } catch {
            return "Não foi possível concluir a operação.";
        }
    }

    // LOGIN
    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearErrors(loginForm);
        clearFeedback();

        const email = document.getElementById("login-email").value.trim();
        const senha = document.getElementById("login-password").value;

        let isValid = true;

        if (!email) {
            showError("login-email", "O e-mail é obrigatório.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            showError("login-email", "Digite um e-mail válido.");
            isValid = false;
        }

        if (!senha) {
            showError("login-password", "A senha é obrigatória.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, senha })
            });

            if (!response.ok) {
                throw new Error(await getErrorMessage(response));
            }

            const { token } = await response.json();

            if (!token) {
                throw new Error("A API não retornou o token de acesso.");
            }

            sessionStorage.setItem(TOKEN_STORAGE_KEY, token);
            window.location.href = "chamados.html";
        } catch (error) {
            setFeedback(
                "login-feedback",
                error.message || "Não foi possível realizar o login.",
                "error"
            );
        }
    });

    // CADASTRO PÚBLICO
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearErrors(registerForm);
        clearFeedback();

        const nome = document.getElementById("register-name").value.trim();
        const email = document.getElementById("register-email").value.trim();
        const senha = document.getElementById("register-password").value;

        let isValid = true;

        if (!nome) {
            showError("register-name", "O nome é obrigatório.");
            isValid = false;
        }

        if (!email) {
            showError("register-email", "O e-mail é obrigatório.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            showError("register-email", "Digite um e-mail válido.");
            isValid = false;
        }

        if (!senha) {
            showError("register-password", "A senha é obrigatória.");
            isValid = false;
        } else if (senha.length < 8) {
            showError(
                "register-password",
                "A senha deve ter no mínimo 8 caracteres."
            );
            isValid = false;
        }

        if (!isValid) return;

        try {
            // Cadastro público: não exige token de autenticação.
            const response = await fetch(`${API_BASE_URL}/users/create`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ nome, email, senha })
            });

            if (!response.ok) {
                throw new Error(await getErrorMessage(response));
            }

            setFeedback(
                "register-feedback",
                "Cadastro realizado com sucesso. Agora faça login.",
                "success"
            );

            document.getElementById("login-email").value = email;
            registerForm.reset();
        } catch (error) {
            setFeedback(
                "register-feedback",
                error.message || "Não foi possível realizar o cadastro.",
                "error"
            );
        }
    });
});
