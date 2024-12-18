import React, { useState, useEffect, useRef } from 'react';
import { Button, Card, Col, Modal, ModalBody, ModalHeader, Row } from 'reactstrap';
import PersonForm from './components/person-form';
import PersonFormUpdate from './components/person-formUpdate';
import PersonFormDelete from './components/person-formDelete';
import UpdateFormDevice from './components/formUpdateDevice';
import DeviceFormDelete from './components/formDeleteDevice';
import CreateFormDevice from './components/formCreateDevice';
import * as API_USERS from './api/person-api';
import PersonTable from './components/person-table';
import BackgroundImg from '../commons/images/gray.jpg';
import APIResponseErrorMessage from '../commons/errorhandling/api-response-error-message';
import { withRouter } from 'react-router-dom';

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
function ChatBox({ users }) {
  const [selectedUser, setSelectedUser] = useState(users[0]?.id || '');
  const [messages, setMessages] = useState({});
  const [input, setInput] = useState('');
  const [typingStatus, setTypingStatus] = useState({});
  const [seenMessage, setSeenMessage] = useState(false);
  const [lastSeenIndex, setLastSeenIndex] = useState(null);
  const webSocket = useRef(null);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    connectWebSocket();

    return () => {
      if (webSocket.current) {
        webSocket.current.close();
      }
    };
  }, [selectedUser]);

  const connectWebSocket = () => {
    webSocket.current = new WebSocket(`ws://localhost:8084/chat?userId=Admin`);

    webSocket.current.onopen = () => {
      console.log('WebSocket Connected');
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
        setLastSeenIndex(messages[selectedUser]?.length || 0);
      }
    };

    webSocket.current.onclose = () => {
      console.log('WebSocket Disconnected');
    };

    webSocket.current.onerror = (error) => {
      console.error('WebSocket Error:', error);
      webSocket.current.close();
    };
  };

  const showMessage = (message) => {
    const participantKey = message.from === 'Admin' ? message.to : message.from;
    setMessages(prev => ({
      ...prev,
      [participantKey]: [...(prev[participantKey] || []), message],
    }));
  };

  const handleInputChange = (e) => {
    setInput(e.target.value);
    if (webSocket.current) {
      const typingMessage = { from: 'Admin', to: selectedUser, typing: true };
      webSocket.current.send(JSON.stringify(typingMessage));
      clearTimeout(webSocket.current.typingTimeout);
      webSocket.current.typingTimeout = setTimeout(() => {
        webSocket.current.send(JSON.stringify({ from: 'Admin', to: selectedUser, typing: false }));
      }, 1000);
    }
  };

  const handleSend = () => {
    if (input.trim() && webSocket.current) {
      const message = { from: 'Admin', to: selectedUser, text: input, seen: false };
      webSocket.current.send(JSON.stringify(message));
      showMessage(message);
      setInput('');
      setSeenMessage(false);
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(scrollToBottom, [messages, selectedUser]);

  const handleMouseEnter = () => {
    if (webSocket.current) {
      const seenMessage = { from: 'Admin', to: selectedUser, text: '', typing: false, seen: true };
      webSocket.current.send(JSON.stringify(seenMessage));
    }
  };

  return (
    <div style={chatboxContainerStyle} onMouseEnter={handleMouseEnter}>
      <div style={chatboxHeaderStyle}>
        <select value={selectedUser} onChange={e => setSelectedUser(e.target.value)} style={dropdownStyle}>
          {users.map(user => (
            <option key={user.id} value={user.id}>{user.name}</option>
          ))}
        </select>
      </div>
      <div style={chatboxMessagesStyle}>
        {(messages[selectedUser] || []).map((msg, index) => (
          <div key={index} style={{
            ...chatboxMessageStyle,
            ...(msg.from === 'Admin' ? chatboxMessageSelfStyle : chatboxMessageOtherStyle),
          }}>
            <span style={chatboxTextStyle}>{msg.text}</span>
          </div>
        ))}
        {seenMessage && <div style={chatboxSeenStyle}>Seen</div>}
        <div ref={messagesEndRef} />
        {typingStatus[selectedUser] && <div style={chatboxTypingStyle}>Typing...</div>}
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

const dropdownStyle = {
  width: '100%',
  padding: '5px',
  borderRadius: '4px',
};
function PersonContainer() {
  const [isAddModalOpen, setAddModalOpen] = useState(false);
  const [isUpdateModalOpen, setUpdateModalOpen] = useState(false);
  const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);
  const [isUpdateDevOpen, setUpdateDevOpen] = useState(false);
  const [isCreateDevOpen, setCreateDevOpen] = useState(false);
  const [isDeleteDevOpen, setDeleteDevOpen] = useState(false);

  const [tableData, setTableData] = useState([]);
  const [isLoaded, setIsLoaded] = useState(false);
  const [errorStatus, setErrorStatus] = useState(0);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchPersons();
  }, []);

  const fetchPersons = () => {
    API_USERS.getPersons((result, status, err) => {
      if (result !== null && status === 200) {
        setTableData(result);
        setIsLoaded(true);
      } else {
        setErrorStatus(status);
        setError(err);
      }
    });
  };

  const toggleAddModal = () => {
    setAddModalOpen(!isAddModalOpen);
    setUpdateModalOpen(false);
    setDeleteModalOpen(false);
    setCreateDevOpen(false);
    setDeleteDevOpen(false);
    setUpdateDevOpen(false);
  };

  const toggleUpdateModal = () => {
    setUpdateModalOpen(!isUpdateModalOpen);
    setAddModalOpen(false);
    setDeleteModalOpen(false);
    setCreateDevOpen(false);
    setDeleteDevOpen(false);
    setUpdateDevOpen(false);
  };

  const toggleDeleteModal = () => {
    setDeleteModalOpen(!isDeleteModalOpen);
    setUpdateModalOpen(false);
    setAddModalOpen(false);
    setCreateDevOpen(false);
    setDeleteDevOpen(false);
    setUpdateDevOpen(false);
  };

  const toggleDeleteDevModal = () => {
    setDeleteModalOpen(false);
    setUpdateModalOpen(false);
    setAddModalOpen(false);
    setCreateDevOpen(false);
    setDeleteDevOpen(!isDeleteDevOpen);
    setUpdateDevOpen(false);
  };

  const toggleCreateDevModal = () => {
    setDeleteModalOpen(false);
    setUpdateModalOpen(false);
    setAddModalOpen(false);
    setCreateDevOpen(!isCreateDevOpen);
    setDeleteDevOpen(false);
    setUpdateDevOpen(false);
  };

  const toggleUpdateDevModal = () => {
    setDeleteModalOpen(false);
    setUpdateModalOpen(false);
    setAddModalOpen(false);
    setCreateDevOpen(false);
    setDeleteDevOpen(false);
    setUpdateDevOpen(!isUpdateDevOpen);
  };

  const reload = () => {
    setIsLoaded(false);
    fetchPersons();
  };

  return (
    <div style={backgroundStyle}>
      <Card style={formContainerStyle}>
        <br />
        <Row>
          <Col sm={{ size: '8', offset: 1 }}>
            <Button color="primary" onClick={toggleAddModal}>
              Add Person
            </Button>
            <Button color="primary" onClick={toggleUpdateModal}>
              Update Person
            </Button>
            <Button color="primary" onClick={toggleDeleteModal}>
              Delete Person
            </Button>
            <Button color="primary" onClick={toggleCreateDevModal}>
              Create Device
            </Button>
            <Button color="primary" onClick={toggleUpdateDevModal}>
              Update Device
            </Button>
            <Button color="primary" onClick={toggleDeleteDevModal}>
              Delete Device
            </Button>
          </Col>
        </Row>
        <br />
        <Row>
          <Col sm={{ size: '8', offset: 1 }}>
            {isLoaded && <PersonTable tableData={tableData} />}
            {errorStatus > 0 && (
              <APIResponseErrorMessage errorStatus={errorStatus} error={error} />
            )}
          </Col>
        </Row>
      </Card>

      <Modal isOpen={isAddModalOpen} toggle={toggleAddModal}>
        <ModalHeader toggle={toggleAddModal}>Add Person</ModalHeader>
        <ModalBody>
          <PersonForm reloadHandler={reload} />
        </ModalBody>
      </Modal>

      <Modal isOpen={isUpdateModalOpen} toggle={toggleUpdateModal}>
        <ModalHeader toggle={toggleUpdateModal}>Update Person</ModalHeader>
        <ModalBody>
          <PersonFormUpdate reloadHandler={reload} />
        </ModalBody>
      </Modal>

      <Modal isOpen={isDeleteModalOpen} toggle={toggleDeleteModal}>
        <ModalHeader toggle={toggleDeleteModal}>Delete Person</ModalHeader>
        <ModalBody>
          <PersonFormDelete reloadHandler={reload} />
        </ModalBody>
      </Modal>

      <Modal isOpen={isCreateDevOpen} toggle={toggleCreateDevModal}>
        <ModalHeader toggle={toggleCreateDevModal}>Add Device</ModalHeader>
        <ModalBody>
          <CreateFormDevice reloadHandler={reload} />
        </ModalBody>
      </Modal>

      <Modal isOpen={isUpdateDevOpen} toggle={toggleUpdateDevModal}>
        <ModalHeader toggle={toggleUpdateDevModal}>Update Device</ModalHeader>
        <ModalBody>
          <UpdateFormDevice reloadHandler={reload} />
        </ModalBody>
      </Modal>

      <Modal isOpen={isDeleteDevOpen} toggle={toggleDeleteDevModal}>
        <ModalHeader toggle={toggleDeleteDevModal}>Delete Device</ModalHeader>
        <ModalBody>
          <DeviceFormDelete reloadHandler={reload} />
        </ModalBody>
      </Modal>

      {isLoaded && <ChatBox users={tableData} />}
    </div>
  );
}

export default withRouter(PersonContainer);
