const API_BASE_URL = "http://localhost:8080";

const sessionUser = {
    userId: localStorage.getItem("userId"),
    username: localStorage.getItem("username"),
    userRoles: localStorage.getItem("userRole")
};

function authHeaders() {
    return { "Authorization": "Bearer " + localStorage.getItem("JWT") };
}

function esc(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;");
}

function money(amount) {
    let value = Number(amount) || 0;
    return "Rs." + value.toFixed(2);
}

function orderCode(orderId) {
    return "#" + String(orderId).padStart(4, "0");
}

function fmtDate(dateValue) {
    let d = new Date(dateValue);
    return d.toLocaleDateString() + " " + d.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit"
    });
}

function toast(message, kind) {

    let stack = document.getElementById("toastStack");
    if (!stack) {
        return;
    }

    let el = document.createElement("div");
    el.className = kind ? "toast " + kind : "toast";
    el.textContent = message;
    stack.appendChild(el);

    setTimeout(function () {
        el.remove();
    }, 3500);
}

function refreshAccessTokenIfNeeded() {

    let refreshToken = localStorage.getItem("refreshToken");
    if (!refreshToken) {
        return;
    }

    fetch(API_BASE_URL + "/v1/login/refresh", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ refreshToken: refreshToken })
    })
        .then(response => response.json())
        .then(response => {
            if (response.status === 0 && response.body && response.body.token) {
                localStorage.setItem("JWT", response.body.token);
                localStorage.setItem("refreshToken", response.body.refreshToken);
            }
        })
        .catch(error => console.error("Silent token refresh failed:", error));
}

function initSession(expectedRole) {

    if (!sessionUser.userId || !localStorage.getItem("JWT")) {
        window.location.href = "index.html";
        return;
    }

    if (expectedRole && sessionUser.userRoles !== expectedRole) {
        window.location.href = "index.html";
        return;
    }

    let avatarEl = document.getElementById("userAvatar");
    let nameEl = document.getElementById("userName");
    let roleEl = document.getElementById("userRole");

    if (avatarEl) {
        avatarEl.textContent = sessionUser.username ? sessionUser.username.charAt(0).toUpperCase() : "?";
    }

    if (nameEl) {
        nameEl.textContent = sessionUser.username || "—";
    }

    if (roleEl) {
        roleEl.textContent = sessionUser.userRoles || "—";
    }

    refreshAccessTokenIfNeeded();
    initChatWidget();
    initNotificationBell();
}

function initChatWidget() {

    if (document.getElementById("aiChatToggle")) {
        return;
    }

    let style = document.createElement("style");
    style.textContent = `
    #aiChatToggle {
      position: fixed; bottom: 24px; right: 24px; width: 56px; height: 56px;
      border-radius: 50%; background: #1C2E36; color: #fff; border: none;
      font-size: 22px; cursor: pointer; box-shadow: 0 4px 14px rgba(0,0,0,0.25);
      z-index: 9999;
    }
    #aiChatPanel {
      position: fixed; bottom: 90px; right: 24px; width: 320px; max-width: calc(100vw - 32px);
      height: 420px; background: #fff; border: 1px solid #DCE6EA; border-radius: 12px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.2); display: none; flex-direction: column;
      overflow: hidden; z-index: 9999; font-family: inherit;
    }
    #aiChatPanel.open { display: flex; }
    #aiChatHeader {
      background: #1C2E36; color: #fff; padding: 12px 14px; font-weight: 600; font-size: 14px;
    }
    #aiChatMessages {
      flex: 1; overflow-y: auto; padding: 10px 12px; font-size: 13.5px; background: #FAFAF8;
    }
    .ai-msg { margin-bottom: 10px; padding: 8px 10px; border-radius: 8px; max-width: 85%; line-height: 1.4; }
    .ai-msg-user { background: #1C2E36; color: #fff; margin-left: auto; }
    .ai-msg-bot { background: #EFEFE9; color: #1C2E36; margin-right: auto; white-space: pre-wrap; }
    #aiChatInputRow { display: flex; border-top: 1px solid #DCE6EA; }
    #aiChatInput { flex: 1; border: none; padding: 10px 12px; font-size: 13.5px; outline: none; }
    #aiChatSend { border: none; background: #1C2E36; color: #fff; padding: 0 16px; cursor: pointer; }
  `;
    document.head.appendChild(style);

    let toggle = document.createElement("button");
    toggle.id = "aiChatToggle";
    toggle.textContent = "💬";
    toggle.title = "Ask the assistant";

    let panel = document.createElement("div");
    panel.id = "aiChatPanel";
    panel.innerHTML = `
    <div id="aiChatHeader">Assistant</div>
    <div id="aiChatMessages"></div>
    <div id="aiChatInputRow">
      <input id="aiChatInput" type="text" placeholder="Ask about the menu, orders..." />
      <button id="aiChatSend">Send</button>
    </div>
  `;

    document.body.appendChild(toggle);
    document.body.appendChild(panel);

    let messagesEl = document.getElementById("aiChatMessages");
    let inputEl = document.getElementById("aiChatInput");

    function appendMessage(text, who) {
        let bubble = document.createElement("div");
        bubble.className = "ai-msg " + (who === "user" ? "ai-msg-user" : "ai-msg-bot");
        bubble.textContent = text;
        messagesEl.appendChild(bubble);
        messagesEl.scrollTop = messagesEl.scrollHeight;
    }

    function sendChatMessage() {

        let text = inputEl.value.trim();
        if (text === "") {
            return;
        }

        appendMessage(text, "user");
        inputEl.value = "";
        appendMessage("Thinking...", "bot");
        let thinkingBubble = messagesEl.lastChild;

        fetch(API_BASE_URL + "/v1/chat", {
            method: "POST",
            headers: Object.assign({ "Content-Type": "application/json" }, authHeaders()),
            body: JSON.stringify({ message: text })
        })
            .then(response => response.json())
            .then(response => {
                thinkingBubble.remove();
                if (response.status === 0 && response.body) {
                    appendMessage(response.body.reply, "bot");
                } else {
                    appendMessage(response.message || "Sorry, something went wrong.", "bot");
                }
            })
            .catch(error => {
                console.error("Chat request failed:", error);
                thinkingBubble.remove();
                appendMessage("Sorry, I couldn't reach the assistant. Please try again.", "bot");
            });
    }

    toggle.addEventListener("click", () => {
        panel.classList.toggle("open");
        if (panel.classList.contains("open") && messagesEl.children.length === 0) {
            appendMessage("Hi " + (sessionUser.username || "") + "! Ask me about the menu, your orders, or (if you're staff) the queue and reports.", "bot");
        }
    });

    document.getElementById("aiChatSend").addEventListener("click", sendChatMessage);
    inputEl.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            sendChatMessage();
        }
    });
}

function initNotificationBell() {

    if (document.getElementById("notifBellToggle")) {
        return;
    }

    let style = document.createElement("style");
    style.textContent = `
    #notifBellToggle {
      position: fixed; bottom: 24px; right: 90px; width: 56px; height: 56px;
      border-radius: 50%; background: #1C2E36; color: #fff; border: none;
      font-size: 20px; cursor: pointer; box-shadow: 0 4px 14px rgba(0,0,0,0.25);
      z-index: 9999;
    }
    #notifBellBadge {
      position: absolute; top: -2px; right: -2px; background: #B23A34; color: #fff;
      border-radius: 10px; font-size: 11px; font-family: monospace; padding: 1px 6px;
      display: none;
    }
    #notifBellPanel {
      position: fixed; bottom: 90px; right: 90px; width: 320px; max-width: calc(100vw - 32px);
      max-height: 420px; background: #fff; border: 1px solid #DCE6EA; border-radius: 12px;
      box-shadow: 0 8px 24px rgba(0,0,0,0.2); display: none; flex-direction: column;
      overflow: hidden; z-index: 9999; font-family: inherit;
    }
    #notifBellPanel.open { display: flex; }
    #notifBellHeader {
      background: #1C2E36; color: #fff; padding: 12px 14px; font-weight: 600; font-size: 14px;
    }
    #notifBellList { flex: 1; overflow-y: auto; background: #FAFAF8; }
    .notif-item { position: relative; padding: 10px 34px 10px 14px; border-bottom: 1px solid #EFEFE9; font-size: 13px; color: #1C2E36; cursor: pointer; }
    .notif-item-close { position: absolute; top: 8px; right: 8px; width: 20px; height: 20px; border: none; background: transparent; color: #6B5F4B; font-size: 14px; line-height: 1; cursor: pointer; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
    .notif-item-close:hover { background: #EFEFE9; color: #1C2E36; }    
    .notif-item:last-child { border-bottom: none; }
    .notif-item.unread { background: #FBEBCE; font-weight: 600; }
    .notif-item .notif-time { display: block; font-size: 11px; color: #6B5F4B; font-weight: 400; margin-top: 3px; }
    .notif-empty { padding: 20px 14px; text-align: center; color: #6B5F4B; font-size: 13px; }
  `;
    document.head.appendChild(style);

    let toggle = document.createElement("button");
    toggle.id = "notifBellToggle";
    toggle.title = "Notifications";
    toggle.innerHTML = "🔔<span id=\"notifBellBadge\">0</span>";

    let panel = document.createElement("div");
    panel.id = "notifBellPanel";
    panel.innerHTML = `
    <div id="notifBellHeader">Notifications</div>
    <div id="notifBellList"><div class="notif-empty">Loading…</div></div>
  `;

    document.body.appendChild(toggle);
    document.body.appendChild(panel);

    let listEl = document.getElementById("notifBellList");
    let badgeEl = document.getElementById("notifBellBadge");

    function renderNotifications(notifications) {

        if (!notifications || notifications.length === 0) {
            listEl.innerHTML = '<div class="notif-empty">No notifications yet</div>';
            badgeEl.style.display = "none";
            return;
        }

        let unreadCount = notifications.filter(n => !n.read).length;
        badgeEl.textContent = unreadCount;
        badgeEl.style.display = unreadCount > 0 ? "inline-block" : "none";

        listEl.innerHTML = notifications.map(n => `
      <div class="notif-item ${n.read ? "" : "unread"}" data-notif-id="${n.notificationId}">
        <button class="notif-item-close" data-notif-delete="${n.notificationId}" title="Dismiss">✕</button>
        ${esc(n.message)}
        <span class="notif-time">${fmtDate(n.createdAt)}</span>
      </div>
    `).join("");
    }

    function loadNotifications() {

        fetch(API_BASE_URL + "/v1/notification/user/" + sessionUser.userId, { headers: authHeaders() })
            .then(response => response.json())
            .then(response => renderNotifications(response.body || []))
            .catch(error => console.error("Failed to load notifications:", error));
    }

    listEl.addEventListener("click", function (event) {

        let deleteId = event.target.closest("[data-notif-delete]")?.dataset.notifDelete;

        if (deleteId) {

            fetch(API_BASE_URL + "/v1/notification/" + deleteId, {
                method: "DELETE",
                headers: authHeaders()
            })
                .then(() => {
                    loadNotifications();
                })
                .catch(error => console.error("Failed to delete notification:", error));

            return;
        }

        let item = event.target.closest("[data-notif-id]");
        if (!item || !item.classList.contains("unread")) {
            return;
        }

        let notificationId = item.dataset.notifId;

        fetch(API_BASE_URL + "/v1/notification/" + notificationId + "/read", {
            method: "PATCH",
            headers: authHeaders()
        })
            .then(() => {
                item.classList.remove("unread");
                let unreadCount = listEl.querySelectorAll(".notif-item.unread").length;
                badgeEl.textContent = unreadCount;
                badgeEl.style.display = unreadCount > 0 ? "inline-block" : "none";
            })
            .catch(error => console.error("Failed to mark notification read:", error));
    });

    toggle.addEventListener("click", function () {
        panel.classList.toggle("open");
        if (panel.classList.contains("open")) {
            loadNotifications();
        }
    });

    loadNotifications();
    setInterval(loadNotifications, 30000);
}