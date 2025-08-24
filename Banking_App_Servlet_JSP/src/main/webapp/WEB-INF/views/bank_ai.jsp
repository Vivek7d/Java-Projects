<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 1. Redesigned CSS for a ChatGPT-like appearance --%>

<style>
    /* Main Chat Window Structure */
    .ai-chat-wrapper {
        display: flex;
        flex-direction: column;
        height: 75vh;
        width: 100%;
        max-width: 800px;
        margin: 0 auto;
    }

    /* Message Area - THIS IS THE FLEX CONTAINER */
    .chat-messages {
        flex-grow: 1;
        padding: 1rem;
        overflow-y: auto;
        display: flex;          /* 1. Turn this into a flex container */
        flex-direction: column; /* 2. Stack messages vertically */
    }

    /* Input Area Styling (no changes needed) */
    .chat-input-area { padding: 1rem; }
    .chat-input-container { display: flex; align-items: center; background-color: #fff; border: 1px solid #e5e5e5; border-radius: 1.5rem; padding: 0.5rem 0.5rem 0.5rem 1rem; box-shadow: 0 2px 12px rgba(0,0,0,0.05); }
    .chat-input-container input { flex-grow: 1; border: none; outline: none; background-color: transparent; font-size: 1rem; }
    .chat-input-container input:focus { box-shadow: none; }
    .chat-input-container button { background-color: #0d6efd; color: white; border: none; border-radius: 50%; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; }

    /* --- THIS IS THE KEY ALIGNMENT LOGIC --- */

    /* Individual Message Container (the flex item) */
    .message {
        display: flex; /* This is needed for the bubble to fit content */
        max-width: 80%;
        margin-bottom: 1rem;
    }

    /* USER MESSAGE: PUSH TO THE RIGHT */
    .message.user {
        align-self: flex-end;
    }

    /* BOT MESSAGE: KEEP ON THE LEFT */
    .message.bot {
        align-self: flex-start;
    }
    
    /* Bubble Styling */
    .message-bubble {
        padding: 0.75rem 1.25rem;
        border-radius: 1.25rem;
        line-height: 1.5;
    }
    
   .message.user .message-bubble {
        background-color: #e9ecef; /* Light gray background */
        color: #212529;           /* Dark text for contrast */
        border-bottom-right-radius: 0.25rem;
    }
    
    /* Bot's Message Bubble (Left Side) */
   .message.bot .message-bubble {
    background: linear-gradient(90deg, #1e90ff 0%, #4682b4 100%); /* Vibrant blue gradient */
    color: #ffffff; /* Pure white text */
    font-weight: 500; /* Make text sharper */
    border-bottom-left-radius: 0.25rem;
}



    /* --- END OF KEY ALIGNMENT LOGIC --- */

    /* Typing Indicator (no changes needed) */
    .typing-indicator { display: none; padding: 0.5rem 0; margin-left: 1rem; }
    @keyframes bounce { 0%, 80%, 100% { transform: scale(0); } 40% { transform: scale(1.0); } }
    .typing-indicator span { height: 8px; width: 8px; margin: 0 2px; background-color: #6c757d; border-radius: 50%; display: inline-block; animation: bounce 1.4s infinite ease-in-out both; }
    .typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
    .typing-indicator span:nth-child(2) { animation-delay: -0.16s; }
</style>
<%-- 2. Redesigned HTML Structure --%>
<div class="ai-chat-wrapper">
    <!-- This is now the main scrollable area -->
    <div class="chat-messages" id="chatMessages">
        <div class="message bot">
            <div class="message-bubble">
                Hello! I am your Bank AI assistant. How can I help you analyze your customer data today?
            </div>
        </div>
    </div>

    <!-- The input area is now a separate section at the bottom -->
    <div class="chat-input-area">
        <div class="typing-indicator " id="typingIndicator">
            <span></span><span></span><span></span>
        </div>
        <div class="chat-input-container">
            <input type="text" id="chatInput" class="form-control " placeholder="Ask anything about your customers...">
            <button class="btn" onclick="sendMessage()">
                <i class="bi bi-arrow-up"></i>
            </button>
        </div>
    </div>
</div>

<%-- 3. The JavaScript logic remains exactly the same --%>
<script>
    const chatMessages = document.getElementById('chatMessages');
    const chatInput = document.getElementById('chatInput');
    const typingIndicator = document.getElementById('typingIndicator');

    chatInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

 // --- THIS IS THE CORRECTED FUNCTION ---
   function showMessage(sender, text) {
        // 1. Create the main message container div.
        const messageDiv = document.createElement('div');
        
        // 2. CRITICAL FIX: Add BOTH the 'message' class and the sender-specific class ('user' or 'bot').
        messageDiv.classList.add('message', sender);

        // 3. Create the inner bubble div.
        const bubbleDiv = document.createElement('div');
        bubbleDiv.className = 'message-bubble';
        
        // 4. Set the text content of the bubble.
        bubbleDiv.textContent = text;
        
        // 5. Build the structure: bubble -> messageDiv -> chatMessages.
        messageDiv.appendChild(bubbleDiv);
        chatMessages.appendChild(messageDiv);
        
        // 6. Scroll to the bottom.
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    
    async function sendMessage() {
        const message = chatInput.value.trim();
        if (message === "") return;

        showMessage("user", message);
        chatInput.value = "";
        typingIndicator.style.display = 'flex';
        chatMessages.scrollTop = chatMessages.scrollHeight; // Scroll down after showing user message

        try {
            const url = '${pageContext.request.contextPath}/admin/ai';

            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'text/plain' },
                body: message
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || `HTTP error! status: ${response.status}`);
            }

            const result = await response.json();
            showMessage("bot", result.reply || "I received an empty reply.");

        } catch (error) {
            console.error("Error:", error);
            showMessage("bot", "⚠️ Something went wrong while contacting the AI service.");
        } finally {
            typingIndicator.style.display = 'none';
        }
    }
</script>