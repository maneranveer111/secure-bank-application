// ======================================
// AUTH.JS (Login + Register + Session)
// ======================================

// ================= LOGIN =================
async function login() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorBox = document.getElementById("error");

    errorBox.innerText = "";

    if (!email || !password) {
        errorBox.innerText = "Please enter email and password";
        return;
    }

    try {
        const response = await API.login({ email, password });

        if (response.status === "success" && response.userId) {
            // Store user data in localStorage
            localStorage.setItem("userId", response.userId);
            localStorage.setItem("email", response.email);
            localStorage.setItem("userName", response.name);

            // Redirect to dashboard
            window.location.href = "dashboard.html";
        } else {
            errorBox.innerText = response.message || "Invalid credentials";
        }
    } catch (error) {
        console.error("Login error:", error);
        errorBox.innerText = "Login failed. Please try again.";
    }
}

// ================= REGISTER =================
async function register() {
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorBox = document.getElementById("error");

    errorBox.innerText = "";

    if (!name || !email || !password) {
        errorBox.innerText = "Please fill all fields";
        return;
    }

    if (password.length < 6) {
        errorBox.innerText = "Password must be at least 6 characters";
        return;
    }

    try {
        const response = await API.register({ name, email, password });

        if (response.status === "success") {
            alert("Registration successful! Please login.");
            window.location.href = "index.html";
        } else {
            errorBox.innerText = response.message || "Registration failed";
        }
    } catch (error) {
        console.error("Registration error:", error);
        errorBox.innerText = "Registration failed. Please try again.";
    }
}

// ================= SESSION HELPERS =================
function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

function getUserId() {
    return localStorage.getItem("userId");
}

function getUserEmail() {
    return localStorage.getItem("email");
}

function getUserName() {
    return localStorage.getItem("userName");
}

function requireLogin() {
    if (!getUserId()) {
        window.location.href = "index.html";
    }
}