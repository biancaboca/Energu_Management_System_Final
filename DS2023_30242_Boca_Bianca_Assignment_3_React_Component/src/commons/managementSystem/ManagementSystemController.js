import React, { useState, useEffect } from 'react';
import APIResponseErrorMessage from '../commons/errorhandling/api-response-error-message';
import {
  Button,
  Card,
  Col,
  Modal,
  ModalBody,
  ModalHeader,
  Row,
} 
from 'reactstrap';

import BackgroundImg from '../commons/images/gray.jpg';

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

function ManagementSystemController() {

     
  
}

export default ManagementSystemController;
