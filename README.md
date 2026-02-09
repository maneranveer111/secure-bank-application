# 🏦 Secure Bank – Banking Transaction System

> A Full Stack Banking Web Application built using **Spring Boot + MySQL + HTML/CSS/JavaScript**

This project simulates a real-world banking system where users can securely manage their accounts, perform transactions, and track financial activities through a modern dashboard.

---

## 🚀 Features

✅ User Registration & Login  
✅ Account Creation (Auto on Register)  
✅ Deposit Money  
✅ Withdraw Money  
✅ Transfer Between Users  
✅ Transaction History  
✅ Password-Protected Operations  
✅ Clean & Modern Dashboard UI  
✅ REST APIs with Spring Boot  
✅ MySQL Database Integration  

---

## 🛠 Tech Stack

### 🔹 Backend
- Java 17
- Spring Boot
- JDBC
- Maven

### 🔹 Database
- MySQL 8+

### 🔹 Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)

### 🔹 Tools
- Git
- GitHub
- VS Code / IntelliJ

---

## 📂 Project Structure

src/main/java/com/ranveer/bankingtransactionsystem
│
├── controller
│ ├── UserController.java
│ ├── AccountController.java
│ └── TransactionController.java
│
├── service
├── dao
├── model
└── util


Frontend files:
src/main/resources/static


---

## ⚙️ Setup Guide (Run Locally)

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/maneranveer11/secure-bank-application.git
cd secure-bank-application
🗄️ Database Setup
Create Database
CREATE DATABASE banking_app;
USE banking_app;
Create Tables
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    balance DECIMAL(15,2) DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    type VARCHAR(20),
    amount DECIMAL(15,2),
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
🔧 Configure Database Credentials
Open:

src/main/java/.../util/DBConnection.java
Update:

private static final String USER = "root";
private static final String PASSWORD = "your_password";
▶️ Run Backend Server
mvn clean install
mvn spring-boot:run
Server runs at:

http://localhost:8080
🌐 Access Frontend
Since frontend is served by Spring Boot:

Open browser:

http://localhost:8080/index.html
🔐 Security Features
Password verification before viewing balance

Password verification for every transaction

Protected transaction history

Session handling using localStorage

⚠️ Note: Passwords are stored in plain text (for learning purpose only).
For production → use BCrypt hashing.

📊 API Endpoints
Users
Method	Endpoint	Description
POST	/users/register	Register new user
POST	/users/login	Login
POST	/users/verify-password	Verify password
Accounts
Method	Endpoint
GET	/accounts/{userId}
POST	/accounts/deposit
POST	/accounts/withdraw
POST	/accounts/transfer
Transactions
Method	Endpoint
GET	/transactions/{userId}
🧪 How to Test
1. Register
Create new account

2. Login
Login using credentials

3. Deposit
Add money to account

4. Withdraw
Remove money

5. Transfer
Send money to another user

6. View History
Check all transactions

🎨 Dashboard Highlights
Left Section
Logo

Navigation

Profile Info

Logout

Middle Section
Welcome message

Balance card

Deposit / Withdraw / Transfer

Transaction history

Right Section
Dynamic forms

Information cards

Transaction panels

🚀 Deployment Options
You can deploy using:

Backend → Render

Database → Render MySQL

Frontend → Served automatically by Spring Boot

📈 Future Improvements
BCrypt password hashing

JWT authentication

Email verification

Transaction search/filter

Export statements (PDF/Excel)

Mobile responsive UI

Dark mode

Notifications

👨‍💻 Author
Ranveer Mane

GitHub: https://github.com/maneranveer11 
