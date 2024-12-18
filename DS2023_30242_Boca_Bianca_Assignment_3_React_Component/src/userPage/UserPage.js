import React, { useState, useEffect, useRef } from 'react';
import { withRouter } from 'react-router-dom';
import BackgroundImg from '../commons/images/gray.jpg';
import APIResponseErrorMessage from '../commons/errorhandling/api-response-error-message';
import { Button, Card, Col, Row } from 'reactstrap';
import UserTable from './UserTable';
import * as API_USERS from '../person/api/person-api';
import { Link } from 'react-router-dom';

const backgroundStyle = {
  backgroundPosition: 'center',
  backgroundSize: 'cover',
  backgroundRepeat: 'no-repeat',
  width: '100vw',
  height: '100vh',
  backgroundImage: `url(${BackgroundImg})`,
};

const formContainerStyle = {
  background: 'rgba(255, 255, 255, 0.8)',
  padding: '20px',
  borderRadius: '10px',
  width: '1200px',
  margin: '0 auto',
  marginTop: '0px',
  left: '50px',
  textAlign: 'center',
  boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

const reconnectInterval = 5000; // Time interval to attempt reconnection in milliseconds
function ChatBox({ user }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [typingStatus, setTypingStatus] = useState({});
  const [lastSeenIndex, setLastSeenIndex] = useState(null);
  const [seenMessage, setSeenMessage] = useState(false);
  const webSocket = useRef(null);
  const reconnectTimeout = useRef(null);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    connectWebSocket();

    return () => {
      if (webSocket.current) {
        webSocket.current.close();
      }
      if (reconnectTimeout.current) {
        clearTimeout(reconnectTimeout.current);
      }
    };
  }, [user.id]);

  const connectWebSocket = () => {
    webSocket.current = new WebSocket(`ws://localhost:8084/chat?userId=${user.id}`);

    webSocket.current.onopen = () => {
      console.log('WebSocket Connected');
      if (reconnectTimeout.current) {
        clearTimeout(reconnectTimeout.current);
      }
    };

    webSocket.current.onmessage = (event) => {
      const message = JSON.parse(event.data);
      console.log('Received message:', message);
      if (message.typing !== undefined) {
        setTypingStatus(prev => ({ ...prev, [message.from]: message.typing }));
      }
      if (message.text || message.text === '') {
        showMessage(message);
      }
      if (message.seen !== undefined) {
        setSeenMessage(true);
        setLastSeenIndex(messages.length);
      }
    };

    webSocket.current.onclose = () => {
      console.log('WebSocket Disconnected');
      attemptReconnect();
    };

    webSocket.current.onerror = (error) => {
      console.error('WebSocket Error:', error);
      webSocket.current.close();
    };
  };

  const attemptReconnect = () => {
    reconnectTimeout.current = setTimeout(() => {
      console.log('Attempting WebSocket Reconnection...');
      connectWebSocket();
    }, reconnectInterval);
  };

  const showMessage = (message) => {
    setMessages(prevMessages => [...prevMessages, message]);
  };

  const handleInputChange = (e) => {
    setInput(e.target.value);
    if (webSocket.current) {
      const typingMessage = { from: user.id, to: 'Admin', typing: true };
      webSocket.current.send(JSON.stringify(typingMessage));
      clearTimeout(webSocket.current.typingTimeout);
      webSocket.current.typingTimeout = setTimeout(() => {
        webSocket.current.send(JSON.stringify({ from: user.id, to: 'Admin', typing: false }));
      }, 1000);
    }
  };

  const handleSend = () => {
    if (input.trim() && webSocket.current) {
      const message = { from: user.id, to: 'Admin', text: input, seen: false };
      webSocket.current.send(JSON.stringify(message));
      showMessage(message);
      setInput('');
      setSeenMessage(false);
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(scrollToBottom, [messages]);

  const handleMouseEnter = () => {
    if (webSocket.current) {
      const seenMessage = { from: user.id, to: 'Admin', text: '', typing: false, seen: true };
      webSocket.current.send(JSON.stringify(seenMessage));
    }
  };

  return (
    <div style={chatboxContainerStyle} onMouseEnter={handleMouseEnter}>
      <div style={chatboxHeaderStyle}>{user.name}</div>
      <div style={chatboxMessagesStyle}>
        {messages.map((msg, index) => (
          <div key={index}>
            <div style={{
              ...chatboxMessageStyle,
              ...(msg.from === user.id ? chatboxMessageSelfStyle : chatboxMessageOtherStyle),
              ...(msg.typing ? chatboxTypingStyle : {}),
            }}>
              <span style={chatboxTextStyle}>{msg.text}</span>
            </div>
          </div>
        ))}
        {seenMessage && <div style={chatboxSeenStyle}>Seen</div>}
        <div ref={messagesEndRef} />
        {typingStatus['Admin'] && <div style={chatboxTypingStyle}>Admin is typing...</div>}
      </div>
      <div style={chatboxInputStyle}>
        <input
          type="text"
          value={input}
          onChange={handleInputChange}
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

const chatboxContainerStyle = {
  width: '350px',
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
  height: '300px',
  overflowY: 'auto',
  flexGrow: 1,
};

const chatboxMessageStyle = {
  marginBottom: '10px',
};

const chatboxMessageSelfStyle = {
  textAlign: 'right',
};

const chatboxMessageOtherStyle = {
  textAlign: 'left',
};

const chatboxTypingStyle = {
  fontStyle: 'italic',
  color: '#888',
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

const chatboxSeenStyle = {
  display: 'block',
  fontStyle: 'italic',
  color: '#888',
  marginTop: '5px',
  textAlign: 'right',
};

function UserPage(props) {
  const [tableData, setTableData] = useState([]);
  const [isLoaded, setIsLoaded] = useState(false);
  const [errorStatus, setErrorStatus] = useState(0);
  const [error, setError] = useState(null);
  const [notification, setNotification] = useState('');
  const [showNotification, setShowNotification] = useState(false);

  const fetchPersons = () => {
    API_USERS.getAllDevicesOfUser(sessionStorage.getItem('userId'), (result, status, err) => {
      if (result !== null && status === 200) {
        setTableData(result);
        setIsLoaded(true);
      } else {
        setErrorStatus(status);
        setError(err);
      }
    });
  };

  useEffect(() => {
    fetchPersons();
  }, []);

  const NotificationPopup = ({ message, onClose, show }) => {
    return (
      <div style={{
        position: 'fixed',
        bottom: '20px',
        right: '20px',
        backgroundColor: 'white',
        padding: '20px',
        border: '1px solid #ddd',
        boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.2)',
        display: show ? 'block' : 'none',
      }}>
        <p>{message}</p>
        <button onClick={onClose}>Close</button>
      </div>
    );
  };

  return (
    <div style={backgroundStyle}>
      <Card style={formContainerStyle}>
        <Link to='/chart'>
          <Button size="20px">WATCH CART</Button>
        </Link>

        <Row>
          <Col sm={{ size: '8', offset: 1 }}>
            {isLoaded && <UserTable tableData={tableData} />}
            {errorStatus > 0 && (
              <APIResponseErrorMessage errorStatus={errorStatus} error={error} />
            )}
          </Col>
        </Row>
      </Card>
      <NotificationPopup
        message={notification}
        onClose={() => setShowNotification(false)}
        show={showNotification}
      />
      {isLoaded && <ChatBox user={{ id: sessionStorage.getItem('userId'), name: 'User' }} />} {/* Adjust to fetch the actual user */}
    </div>
  );
}

export default withRouter(UserPage);
