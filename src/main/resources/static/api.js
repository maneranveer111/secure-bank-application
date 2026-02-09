const BASE_URL = "http://localhost:8080";

const API = {
    // Generic POST request
    async post(url, body) {
        try {
            const res = await fetch(BASE_URL + url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });

            return await res.json();
        } catch (error) {
            console.error("API POST Error:", error);
            throw error;
        }
    },

    // Generic GET request
    async get(url) {
        try {
            const res = await fetch(BASE_URL + url);
            return await res.json();
        } catch (error) {
            console.error("API GET Error:", error);
            throw error;
        }
    },

    // User endpoints
    register(data) {
        return this.post("/users/register", data);
    },

    login(data) {
        return this.post("/users/login", data);
    },

    verifyPassword(data) {
        return this.post("/users/verify-password", data);
    },

    // Account endpoints
    getAccount(userId) {
        return this.get(`/accounts/${userId}`);
    },

    deposit(data) {
        return this.post("/accounts/deposit", data);
    },

    withdraw(data) {
        return this.post("/accounts/withdraw", data);
    },

    transfer(data) {
        return this.post("/accounts/transfer", data);
    },

    // Transaction endpoints
    getTransactions(userId) {
        return this.get(`/transactions/${userId}`);
    }
};