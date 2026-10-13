document.addEventListener("DOMContentLoaded", () => {
    const loginScreen = document.getElementById("login-screen");
    const registerScreen = document.getElementById("register-screen");
    const goToRegister = document.getElementById("go-to-register");
    const goToLogin = document.getElementById("go-to-login");
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");

    // Navegação entre telas
    goToRegister.addEventListener("click", (e) => {
        e.preventDefault();
        loginScreen.classList.add("hidden");
        registerScreen.classList.remove("hidden");
    });

    goToLogin.addEventListener("click", (e) => {
        e.preventDefault();
        registerScreen.classList.add("hidden");
        loginScreen.classList.remove("hidden");
    });

    // Funções auxiliares de erro visual
    function showError(inputId, message) {
        const inputElement = document.getElementById(inputId);
        const errorElement = document.getElementById(`${inputId}-error`);
        inputElement.classList.add("error");
        errorElement.textContent = message;
    }

    function clearErrors(form) {
        const inputs = form.querySelectorAll("input");
        const errors = form.querySelectorAll(".error-message");
        inputs.forEach(input => input.classList.remove("error"));
        errors.forEach(error => error.textContent = "");
    }

    function isValidEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    // Processamento do Login
    loginForm.addEventListener("submit", (e) => {
        e.preventDefault();
        clearErrors(loginForm);

        const email = document.getElementById("login-email").value.trim();
        const password = document.getElementById("login-password").value.trim();
        let isValid = true;

        if (!email) {
            showError("login-email", "O e-mail é obrigatório.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            showError("login-email", "Digite um e-mail válido.");
            isValid = false;
        }

        if (!password) {
            showError("login-password", "A senha é obrigatória.");
            isValid = false;
        }

        if (isValid) {
            // Estrutura pronta para a futura integração (POST /auth/login)
            const loginData = { email, password };
            console.log("Dados prontos para envio (Login):", loginData);
            // Futuramente: chamar função do arquivo api.js passando loginData
        }
    });

    // Processamento do Cadastro
    registerForm.addEventListener("submit", (e) => {
        e.preventDefault();
        clearErrors(registerForm);

        const name = document.getElementById("register-name").value.trim();
        const email = document.getElementById("register-email").value.trim();
        const password = document.getElementById("register-password").value.trim();
        let isValid = true;

        if (!name) {
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

        if (!password) {
            showError("register-password", "A senha é obrigatória.");
            isValid = false;
        } else if (password.length < 6) {
            showError("register-password", "A senha deve ter no mínimo 6 caracteres.");
            isValid = false;
        }

        if (isValid) {
            // Estrutura pronta para a futura integração (POST /users)
            const registerData = { name, email, password };
            console.log("Dados prontos para envio (Cadastro):", registerData);
            // Futuramente: chamar função do arquivo api.js passando registerData
        }
    });
});