// ======================================
// DASHBOARD.JS - Complete Banking System
// ======================================

requireLogin();

const userId = getUserId();
const userEmail = getUserEmail();
const userName = getUserName();

let currentBalance = 0;
let accountId = null;
let balanceVisible = false;

// ================= INITIALIZATION =================
window.onload = function() {
    init();
};

async function init() {
    // Set welcome text
    document.getElementById("welcomeText").innerText = userName;

    // Set profile info
    document.getElementById("profileName").innerText = userName;
    document.getElementById("profileEmail").innerText = userEmail;
    document.getElementById("profileUserId").innerText = userId;

    // Load account data
    await loadAccountData();

    // Show default animation in right section
    showDefaultAnimation();
}

// ================= LOAD ACCOUNT DATA =================
async function loadAccountData() {
    try {
        const account = await API.getAccount(userId);
        
        if (account) {
            currentBalance = parseFloat(account.balance);
            accountId = account.accountId;
            document.getElementById("profileAccountId").innerText = accountId;
        }
    } catch (error) {
        console.error("Error loading account:", error);
        showNotification("Error loading account data", "error");
    }
}

// ================= VIEW BALANCE =================
async function viewBalance() {
    if (balanceVisible) {
        // Hide balance
        document.getElementById("balanceAmount").innerText = "...";
        balanceVisible = false;
        return;
    }

    // Show password verification modal
    showPasswordModal("View Balance", async (password) => {
        try {
            const response = await API.verifyPassword({
                email: userEmail,
                password: password
            });

            if (response.status === "success" && response.verified) {
                // Show balance
                document.getElementById("balanceAmount").innerText = `₹${currentBalance.toFixed(2)}`;
                balanceVisible = true;
                closePasswordModal();
            } else {
                document.getElementById("passwordError").innerText = "Incorrect password";
            }
        } catch (error) {
            document.getElementById("passwordError").innerText = "Verification failed";
        }
    });
}

// ================= SHOW DEPOSIT FORM =================
function showDepositForm() {
    const rightSection = document.getElementById("rightSection");
    
    rightSection.innerHTML = `
        <div class="action-container">
            <h2>💰 Deposit Money</h2>
            <p class="instruction">Enter the amount you want to deposit into your account</p>
            
            <div class="form-group">
                <label>Amount (₹)</label>
                <input type="number" id="depositAmount" placeholder="Enter amount" min="1" step="0.01">
            </div>

            <button class="btn-primary" onclick="processDeposit()">Deposit</button>
            <button class="btn-secondary" onclick="showDefaultAnimation()">Cancel</button>
        </div>
    `;
}

async function processDeposit() {
    const amount = document.getElementById("depositAmount").value;

    if (!amount || parseFloat(amount) <= 0) {
        showNotification("Please enter a valid amount", "error");
        return;
    }

    // Show password verification in the same section
    showPasswordVerificationInSection("Deposit", amount, async (password) => {
        try {
            const response = await API.verifyPassword({
                email: userEmail,
                password: password
            });

            if (response.status === "success" && response.verified) {
                // Process deposit
                const depositResponse = await API.deposit({
                    userId: parseInt(userId),
                    amount: parseFloat(amount)
                });

                if (depositResponse.status === "success") {
                    showSuccessPopup(`Successfully deposited ₹${amount}`);
                    await loadAccountData();
                    balanceVisible = false;
                    document.getElementById("balanceAmount").innerText = "...";
                    showDefaultAnimation();
                } else {
                    showNotification(depositResponse.message || "Deposit failed", "error");
                }
            } else {
                document.getElementById("inlinePwdError").innerText = "Incorrect password";
            }
        } catch (error) {
            console.error("Deposit error:", error);
            showNotification("Deposit failed", "error");
        }
    });
}

// ================= SHOW WITHDRAW FORM =================
function showWithdrawForm() {
    const rightSection = document.getElementById("rightSection");
    
    rightSection.innerHTML = `
        <div class="action-container">
            <h2>💳 Withdraw Money</h2>
            <p class="instruction">Enter the amount you want to withdraw from your account</p>
            
            <div class="form-group">
                <label>Amount (₹)</label>
                <input type="number" id="withdrawAmount" placeholder="Enter amount" min="1" step="0.01">
            </div>

            <button class="btn-primary" onclick="processWithdraw()">Withdraw</button>
            <button class="btn-secondary" onclick="showDefaultAnimation()">Cancel</button>
        </div>
    `;
}

async function processWithdraw() {
    const amount = document.getElementById("withdrawAmount").value;

    if (!amount || parseFloat(amount) <= 0) {
        showNotification("Please enter a valid amount", "error");
        return;
    }

    // Show password verification in the same section
    showPasswordVerificationInSection("Withdraw", amount, async (password) => {
        try {
            const response = await API.verifyPassword({
                email: userEmail,
                password: password
            });

            if (response.status === "success" && response.verified) {
                // Process withdrawal
                const withdrawResponse = await API.withdraw({
                    userId: parseInt(userId),
                    amount: parseFloat(amount)
                });

                if (withdrawResponse.status === "success") {
                    showSuccessPopup(`Successfully withdrew ₹${amount}`);
                    await loadAccountData();
                    balanceVisible = false;
                    document.getElementById("balanceAmount").innerText = "...";
                    showDefaultAnimation();
                } else {
                    showNotification(withdrawResponse.message || "Insufficient balance", "error");
                }
            } else {
                document.getElementById("inlinePwdError").innerText = "Incorrect password";
            }
        } catch (error) {
            console.error("Withdraw error:", error);
            showNotification("Withdrawal failed", "error");
        }
    });
}

// ================= SHOW TRANSFER FORM =================
function showTransferForm() {
    const rightSection = document.getElementById("rightSection");
    
    rightSection.innerHTML = `
        <div class="action-container">
            <h2>🔄 Transfer Money</h2>
            <p class="instruction">Enter recipient details and amount to transfer</p>
            
            <div class="form-group">
                <label>Recipient User ID</label>
                <input type="number" id="recipientUserId" placeholder="Enter recipient user ID" min="1">
            </div>

            <div class="form-group">
                <label>Amount (₹)</label>
                <input type="number" id="transferAmount" placeholder="Enter amount" min="1" step="0.01">
            </div>

            <button class="btn-primary" onclick="processTransfer()">Transfer</button>
            <button class="btn-secondary" onclick="showDefaultAnimation()">Cancel</button>
        </div>
    `;
}

async function processTransfer() {
    const recipientId = document.getElementById("recipientUserId").value;
    const amount = document.getElementById("transferAmount").value;

    if (!recipientId || parseInt(recipientId) <= 0) {
        showNotification("Please enter a valid recipient User ID", "error");
        return;
    }

    if (parseInt(recipientId) === parseInt(userId)) {
        showNotification("Cannot transfer to yourself", "error");
        return;
    }

    if (!amount || parseFloat(amount) <= 0) {
        showNotification("Please enter a valid amount", "error");
        return;
    }

    // Show password verification in the same section
    showPasswordVerificationInSection("Transfer", amount, async (password) => {
        try {
            const response = await API.verifyPassword({
                email: userEmail,
                password: password
            });

            if (response.status === "success" && response.verified) {
                // Process transfer
                const transferResponse = await API.transfer({
                    fromUserId: parseInt(userId),
                    toUserId: parseInt(recipientId),
                    amount: parseFloat(amount)
                });

                if (transferResponse.status === "success") {
                    showSuccessPopup(`Successfully transferred ₹${amount} to User ID: ${recipientId}`);
                    await loadAccountData();
                    balanceVisible = false;
                    document.getElementById("balanceAmount").innerText = "...";
                    showDefaultAnimation();
                } else {
                    showNotification(transferResponse.message || "Transfer failed", "error");
                }
            } else {
                document.getElementById("inlinePwdError").innerText = "Incorrect password";
            }
        } catch (error) {
            console.error("Transfer error:", error);
            showNotification("Transfer failed", "error");
        }
    });
}

// ================= VIEW TRANSACTION HISTORY =================
async function viewTransactionHistory() {
    // Show password verification modal for history
    showPasswordModal("View Transaction History", async (password) => {
        try {
            const response = await API.verifyPassword({
                email: userEmail,
                password: password
            });

            if (response.status === "success" && response.verified) {
                closePasswordModal();
                await loadTransactionHistory();
            } else {
                document.getElementById("passwordError").innerText = "Incorrect password";
            }
        } catch (error) {
            document.getElementById("passwordError").innerText = "Verification failed";
        }
    });
}

async function loadTransactionHistory() {
    try {
        const transactions = await API.getTransactions(userId);
        
        const historyContainer = document.getElementById("historyContainer");
        const historyContent = document.getElementById("historyContent");

        if (!transactions || transactions.length === 0) {
            historyContent.innerHTML = `
                <p class="no-transactions">No transactions found</p>
            `;
        } else {
            let html = '<div class="transaction-list">';
            
            transactions.forEach(tx => {
                const date = new Date(tx.transactionTime).toLocaleString();
                const typeClass = tx.type.includes('IN') || tx.type === 'DEPOSIT' ? 'credit' : 'debit';
                const sign = tx.type.includes('IN') || tx.type === 'DEPOSIT' ? '+' : '-';
                
                html += `
                    <div class="transaction-item ${typeClass}">
                        <div class="tx-info">
                            <strong>${tx.type.replace(/_/g, ' ')}</strong>
                            <small>${date}</small>
                        </div>
                        <div class="tx-amount">
                            ${sign}₹${parseFloat(tx.amount).toFixed(2)}
                        </div>
                    </div>
                `;
            });
            
            html += '</div>';
            historyContent.innerHTML = html;
        }

        historyContainer.style.display = 'block';
    } catch (error) {
        console.error("Error loading transactions:", error);
        showNotification("Failed to load transaction history", "error");
    }
}

function closeHistory() {
    document.getElementById("historyContainer").style.display = 'none';
}

// ================= TOGGLE PROFILE =================
function toggleProfile() {
    const profileDetails = document.getElementById("profileDetails");
    const isVisible = profileDetails.style.display === 'block';
    profileDetails.style.display = isVisible ? 'none' : 'block';
}

// ================= PASSWORD VERIFICATION MODAL =================
function showPasswordModal(title, onVerify) {
    const modal = document.getElementById("passwordModal");
    document.getElementById("modalTitle").innerText = title;
    document.getElementById("passwordInput").value = "";
    document.getElementById("passwordError").innerText = "";
    
    modal.style.display = "flex";
    
    // Store the callback
    window.currentPasswordCallback = onVerify;
}

function closePasswordModal() {
    document.getElementById("passwordModal").style.display = "none";
    window.currentPasswordCallback = null;
}

async function verifyPasswordModal() {
    const password = document.getElementById("passwordInput").value;
    
    if (!password) {
        document.getElementById("passwordError").innerText = "Please enter password";
        return;
    }

    if (window.currentPasswordCallback) {
        await window.currentPasswordCallback(password);
    }
}

// ================= INLINE PASSWORD VERIFICATION (for transactions) =================
function showPasswordVerificationInSection(action, amount, onVerify) {
    const rightSection = document.getElementById("rightSection");
    const currentContent = rightSection.innerHTML;
    
    rightSection.innerHTML = currentContent + `
        <div class="password-verify-section">
            <h3>🔒 Verify Password</h3>
            <p>Please enter your password to confirm ${action.toLowerCase()} of ₹${amount}</p>
            
            <div class="form-group">
                <input type="password" id="inlinePassword" placeholder="Enter password">
                <p id="inlinePwdError" class="error-text"></p>
            </div>

            <button class="btn-primary" onclick="verifyInlinePassword()">Confirm</button>
            <button class="btn-secondary" onclick="showDefaultAnimation()">Cancel</button>
        </div>
    `;

    // Store callback
    window.currentInlinePasswordCallback = onVerify;
}

async function verifyInlinePassword() {
    const password = document.getElementById("inlinePassword").value;
    
    if (!password) {
        document.getElementById("inlinePwdError").innerText = "Please enter password";
        return;
    }

    if (window.currentInlinePasswordCallback) {
        await window.currentInlinePasswordCallback(password);
    }
}

// ================= DEFAULT ANIMATION/INFO =================
function showDefaultAnimation() {
    const rightSection = document.getElementById("rightSection");
    
    rightSection.innerHTML = `
        <div class="info-section">
            <div class="info-card animate-fade-in">
                <div class="icon">💰</div>
                <h3>Secure Banking</h3>
                <p>Manage your finances with confidence. Your money is protected with bank-level security.</p>
            </div>

            <div class="info-card animate-fade-in" style="animation-delay: 0.2s">
                <div class="icon">🚀</div>
                <h3>Instant Transfers</h3>
                <p>Send money to anyone instantly. Fast, reliable, and secure transactions 24/7.</p>
            </div>

            <div class="info-card animate-fade-in" style="animation-delay: 0.4s">
                <div class="icon">📊</div>
                <h3>Track Every Transaction</h3>
                <p>View your complete transaction history. Stay informed about all your financial activities.</p>
            </div>

            <div class="info-card animate-fade-in" style="animation-delay: 0.6s">
                <div class="icon">🔒</div>
                <h3>Password Protected</h3>
                <p>Every sensitive operation requires password verification for maximum security.</p>
            </div>

            <div class="welcome-animation">
                <h2>Welcome to Secure Bank!</h2>
                <p>Select an action from the menu to get started</p>
            </div>
        </div>
    `;
}

// ================= NOTIFICATIONS =================
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerText = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => notification.classList.add('show'), 100);
    
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

function showSuccessPopup(message) {
    const popup = document.createElement('div');
    popup.className = 'success-popup';
    popup.innerHTML = `
        <div class="success-content">
            <div class="success-icon">✓</div>
            <h3>Success!</h3>
            <p>${message}</p>
            <button onclick="this.parentElement.parentElement.remove()">OK</button>
        </div>
    `;
    
    document.body.appendChild(popup);
    
    setTimeout(() => popup.classList.add('show'), 100);
}

// Allow Enter key to submit password
document.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        const passwordModal = document.getElementById('passwordModal');
        if (passwordModal && passwordModal.style.display === 'flex') {
            verifyPasswordModal();
        }
    }
});