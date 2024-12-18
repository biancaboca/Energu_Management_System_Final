import React, { useState, useEffect, useRef } from 'react';

function ChatBox({ users }) {
  const [selectedUser, setSelectedUser] = useState(users[0]?.id || '');
  const [messages, setMessages] = useState({});
  const [input, setInput] = useState('');
  const webSocket = useRef(null);

  useEffect(() => {
    connectWebSocket();

    return () => {
      if (webSocket.current) {
        webSocket.current.close();
      }
    };
  }, []);

  const connectWebSocket = () => {
    webSocket.current = new WebSocket(`ws://localhost:8084/chat?userId=Admin`);
    webSocket.current.onopen = () => console.log('WebSocket Connected');
    webSocket.current.onmessage = (event) => {
      const message = JSON.parse(event.data);
      showMessage(message);
    };
    webSocket.current.onclose = () => {
      console.log('WebSocket Disconnected, attempting to reconnect...');
      setTimeout(connectWebSocket, 1000); // Reconnect after 1 second
    };
  };

  const showMessage = (message) => {
    setMessages((prevMessages) => ({
      ...prevMessages,
      [message.from]: [...(prevMessages[message.from] || []), message],
    }));
    if (message.to === 'Admin') {
      markMessageAsSeen(message.from);
    }
  };

  const markMessageAsSeen = (from) => {
    const seenMessage = { from: 'Admin', to: from, seen: true };
    webSocket.current.send(JSON.stringify(seenMessage));
  };

  const handleUserChange = (e) => {
    setSelectedUser(e.target.value);
  };

  const handleSend = () => {
    if (input.trim() && webSocket.current) {
      const message = { from: 'Admin', to: selectedUser, text: input };
      webSocket.current.send(JSON.stringify(message));
      setMessages((prevMessages) => ({
        ...prevMessages,
        [selectedUser]: [...(prevMessages[selectedUser] || []), message],
      }));
      setInput('');
    }
  };

  return (
    <div style={chatboxContainerStyle}>
      <div style={chatboxHeaderStyle}>
        <select value={selectedUser} onChange={handleUserChange} style={dropdownStyle}>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.name}
            </option>
          ))}
        </select>
      </div>
      <div style={chatboxMessagesStyle}>
        {(messages[selectedUser] || []).map((msg, index) => (
          <div key={index} style={{ ...chatboxMessageStyle, ...(msg.from === 'Admin' ? chatboxMessageSelfStyle : {}) }}>
            <span style={chatboxSenderStyle}>{msg.from}</span>
            <span style={chatboxTextStyle}>{msg.text}</span>
            {msg.seen && <span style={chatboxSeenStyle}>Seen</span>}
          </div>
        ))}
      </div>
      <div style={chatboxInputStyle}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type a message..."
          style={chatboxInputFieldStyle}
        />
        <button onClick={handleSend} style={chatboxSendButtonStyle}>
          Send
        </button>
      </div>
    </div>
  );
}

const chatboxSeenStyle = {
  display: 'block',
  fontSize: '0.8em',
  color: '#888',
};

const chatboxContainerStyle = {
  width: '300px',
  border: '1px solid #ccc',
  borderRadius: '8px',
  display: 'flex',
  flexDirection: 'column',
  fontFamily: 'Arial, sans-serif',
  position: 'fixed',
  bottom: '20px',
  right: '20px',
  backgroundColor: '#fff',
  boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
  marginBottom: '10px',
};

const chatboxHeaderStyle = {
  backgroundColor: '#007bff',
  color: '#fff',
  padding: '10px',
  borderTopLeftRadius: '8px',
  borderTopRightRadius: '8px',
  fontWeight: 'bold',
};

const chatboxMessagesStyle = {
  padding: '10px',
  height: '200px',
  overflowY: 'auto',
  flexGrow: 1,
};

const chatboxMessageStyle = {
  marginBottom: '10px',
};

const chatboxMessageSelfStyle = {
  textAlign: 'right',
};

const chatboxSenderStyle = {
  display: 'block',
  fontWeight: 'bold',
};

const chatboxTextStyle = {
  display: 'block',
};

const chatboxInputStyle = {
  display: 'flex',
  padding: '10px',
  borderTop: '1px solid #ddd',
};

const chatboxInputFieldStyle = {
  flexGrow: 1,
  border: '1px solid #ddd',
  borderRadius: '4px',
  padding: '5px',
  marginRight: '10px',
};

const chatboxSendButtonStyle = {
  backgroundColor: '#007bff',
  color: '#fff',
  border: 'none',
  borderRadius: '4px',
  padding: '5px 10px',
};

const dropdownStyle = {
  width: '100%',
  padding: '5px',
  borderRadius: '4px',
};

export default ChatBox;
