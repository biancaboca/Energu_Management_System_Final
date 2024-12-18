import React from 'react';
import { useState } from 'react';
import BackgroundImg from '../../commons/images/gray.jpg'; // Update the path based on your project structure
import { Button, Container, Jumbotron, Form, FormGroup, Label, Input } from 'reactstrap';
import { postPerson } from '../../person/api/person-api';


const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: '100%',
    height: '100vh',
    backgroundImage: `url(${BackgroundImg})`
};

const imageStyle = {
    display: 'block',
    margin: '0 auto',
    width: '30%',
    height: '30%',
};

const textStyle = {
    color: 'black',
    fontWeight: 'bold',
    textDecoration: 'none',
    textAlign: 'center',
};
const formContainerStyle = {
    background: 'rgba(255, 255, 255, 0.8)', // Semi-transparent white background
    padding: '20px',
    borderRadius: '8px',
    width: '500px',
    margin: '0 auto',
    marginTop: '50px',
    textAlign: 'center',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)', // Box shadow for depth
  };
  const buttonStyle = {
    width: '100%',
  };

  const headerStyle = {
    fontSize: '24px',
    fontWeight: 'bold',
    marginBottom: '20px',
  };
  
  
function Signup() {
    const [role, setRole] = useState('user'); // Initialize the role state with 'user'

    const handleRoleChange = (event) => {
        setRole(event.target.checked ? 'admin' : 'user');
    };

    return (
        <div>
            <Jumbotron fluid style={backgroundStyle}>
                <Container style={formContainerStyle}>
                    <h1 className="display-4" style={headerStyle}>Sign Up</h1>
                    <Form >
                        <FormGroup>
                            <Label for="username">Username</Label>
                            <Input
                                type="text"
                                name="username"
                                id="username"
                                placeholder="Enter your username..."
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="name">Name</Label>
                            <Input
                                type="text"
                                name="name"
                                id="name"
                                placeholder="Enter your name..."
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="password">Password</Label>
                            <Input
                                type="password"
                                name="password"
                                id="password"
                                placeholder="Enter your password..."
                            />
                        </FormGroup>
                        <FormGroup check>
                            <Label check>
                                <Input
                                    type="checkbox"
                                    checked={role === 'admin'}
                                    onChange={handleRoleChange}
                                />
                                Admin
                            </Label>
                        </FormGroup>
                        <Button color="primary" style={buttonStyle} onClick={postPerson}>SignIn</Button>
                    </Form>
                </Container>
            </Jumbotron>
        </div>
    );
}

export default Signup;
