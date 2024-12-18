import React, { Component } from 'react';
import BackgroundImg from '../commons/images/gray.jpg';
import { Button, Container, Jumbotron, Form, FormGroup, Label, Input } from 'reactstrap';
import * as API_USERS from '../person/api/person-api';
import { withRouter } from 'react-router-dom';

const backgroundStyle = {
  backgroundPosition: 'center',
  backgroundSize: 'cover',
  backgroundRepeat: 'no-repeat',
  width: '100%',
  height: '100vh',
  backgroundImage: `url(${BackgroundImg})`,
};

const formContainerStyle = {
  background: 'rgba(255, 255, 255, 0.8)',
  padding: '20px',
  borderRadius: '8px',
  width: '500px',
  margin: '0 auto',
  marginTop: '50px',
  textAlign: 'center',
  boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

const headerStyle = {
  fontSize: '24px',
  fontWeight: 'bold',
  marginBottom: '20px',
};

const buttonStyle = {
  width: '100%',
};

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      redirectToUserPage: false,
      redirectToAdminPage: false,
      userRole: '',
      id: null,
      loginFailed: false,
      isAuthenticated: false,
    };
  }

  handleUsernameChange = (event) => {
    this.setState({ username: event.target.value });
  };

  handlePasswordChange = (event) => {
    this.setState({ password: event.target.value });
  };

  handleLogin = () => {
    const { username, password } = this.state;

    // Check for empty username or password
    if (!username || !password) {
      this.setState({
        redirectToUserPage: false,
        redirectToAdminPage: false,
        loginFailed: true,
        isAuthenticated: false,
      });
      return;
    }

    API_USERS.logInFunc(username, password, (result, status, err) => {
      if (result == null) {
        this.setState({
          redirectToUserPage: false,
          redirectToAdminPage: false,
          loginFailed: true,
          isAuthenticated: false,
        });
        return;
      }

      if (status === 200) {
        const { personID, authToken } = result;
        sessionStorage.setItem('userId', personID);
        sessionStorage.setItem('authToken', authToken);
        console.log(sessionStorage.getItem('authToken'));

        API_USERS.verifyingAdmin(result.personID, (result1) => {
          if (result1 === true) {
            // Set authToken in sessionStorage
       //     console.log(result1 + " is null");
            this.setState({
              redirectToUserPage: true,
              personID,
              userRole: 'user',
              isAuthenticated: true,
              loginFailed: false,
            });
            this.props.history.push('/userPage');
          } else if (result1 === false) {
            this.setState({
              redirectToAdminPage: true,
              userRole: 'admin',
              personID,
              isAuthenticated: false,
              loginFailed: false,
            });
            this.props.history.push('/person');

          }
        });
      } else {
        this.setState({ loginFailed: true, isAuthenticated: false });
        console.log('Login failed. Try again.');
        // Redirect to login on login failure
        this.props.history.push('/login');
      }
    });
  };

  render() {
    return (
      <div>
        <Jumbotron fluid style={backgroundStyle}>
          <Container>
            <div style={formContainerStyle}>
              <h1 className="display-4" style={headerStyle}>
                Login
              </h1>
              <Form>
                <FormGroup>
                  <Label for="username">Username</Label>
                  <Input
                    type="text"
                    name="id"
                    id="id"
                    value={this.state.username}
                    onChange={this.handleUsernameChange}
                    placeholder="Enter your username..."
                  />
                </FormGroup>
                <FormGroup>
                  <Label for="password">Password</Label>
                  <Input
                    type="password"
                    name="password"
                    id="password"
                    value={this.state.password}
                    onChange={this.handlePasswordChange}
                    placeholder="Enter your password..."
                  />
                </FormGroup>
                <Button color="primary" style={buttonStyle} onClick={this.handleLogin}>
                  Login
                </Button>
                <p>
                  Don't have an account? <a href="/signup">Sign up</a>
                </p>
              </Form>
            </div>
          </Container>
        </Jumbotron>
      </div>
    );
  }
}

export default withRouter(Login);
