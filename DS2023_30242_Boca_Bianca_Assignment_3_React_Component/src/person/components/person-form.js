import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/person-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class PersonForm extends React.Component {
    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reloadHandler = this.props.reloadHandler;

        this.state = {
            errorStatus: 0,
            error: null,

            formIsValid: false,

            formControls: {
                name: {
                    value: '',
                    placeholder: 'Enter your name...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true,
                    },
                },
                role: {
                    value: '',
                    placeholder: 'Enter your role...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        usernameValidator: true,
                    },
                },
                username: {
                    value: '',
                    placeholder: 'Enter your username...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        usernameValidator: true,
                    },
                },
                password: {
                    value: '',
                    placeholder: 'Enter a password...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        passwordValidatori: true,
                    },
                },
            },
        };
    }

    toggleForm() {
        this.setState({ collapseForm: !this.state.collapseForm });
    }

    handleChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;

        const updatedControls = { ...this.state.formControls };
        const updatedFormElement = updatedControls[name];

        updatedFormElement.value = value;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(value, updatedFormElement.validationRules);
        updatedControls[name] = updatedFormElement;

        let formIsValid = true;
        for (let formElementName in updatedControls) {
            formIsValid = updatedControls[formElementName].valid && formIsValid;
        }

        this.setState({
            formControls: updatedControls,
            formIsValid: formIsValid,
        });
    };

    registerPerson(person) {
        return API_USERS.postPerson(person, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully inserted person with id: " + result);
                this.reloadHandler();
            } else {
                this.setState({
                    errorStatus: status,
                    error: error,
                });
            }
        });
    }

    handleSubmit = () => {
        let person = {
            name: this.state.formControls.name.value,
            role: this.state.formControls.role.value,
            password: this.state.formControls.password.value,
            username: this.state.formControls.username.value,
        };
    
        console.log(person);
        this.registerPerson(person);
    };
    
    render() {
        return (
            <div>
                <FormGroup id='Name'>
                    <Label for='nameField'> Name: </Label>
                    <Input
                        name='name'
                        id='nameField'
                        placeholder={this.state.formControls.name.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.name.value}
                        valid={this.state.formControls.name.valid}
                        required
                    />
                    {this.state.formControls.name.touched &&
                        !this.state.formControls.name.valid && (
                            <div className={"error-message row"}>
                                * Name must have at least 3 characters
                            </div>
                        )}
                </FormGroup>

                <FormGroup id='Role'>
                    <Label for='RoleField'> Role: </Label>
                    <Input
                        name='role'
                        id='RoleField'
                        placeholder={this.state.formControls.role.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.role.value}
                        valid={this.state.formControls.role.valid}
                        required
                    />
                    {this.state.formControls.role.touched &&
                        !this.state.formControls.role.valid && (
                            <div className={"error-message"}>
                                * Role need to be user
                            </div>
                        )}
                </FormGroup>

                <FormGroup id='Password'>
                    <Label for='passwordField'> Password: </Label>
                    <Input
                        type='password'
                        name='password'
                        id='passwordField'
                        placeholder={this.state.formControls.password.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.password.value}
                        valid={this.state.formControls.password.valid}
                        required
                    />
                    {this.state.formControls.password.touched &&
                        !this.state.formControls.password.valid && (
                            <div className={"error-message"}>
                                * Password must meet validation rules
                            </div>
                        )}
                </FormGroup>

                <FormGroup id='Username'>
                    <Label for='usernameField'> Username: </Label>
                    <Input
                        name='username'
                        id='usernameField'
                        placeholder={this.state.formControls.username.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.username.value}
                        valid={this.state.formControls.username.valid}
                        required
                    />
                    {this.state.formControls.username.touched &&
                        !this.state.formControls.username.valid && (
                            <div className={"error-message"}>
                                * Username must meet validation rules
                            </div>
                        )}
                </FormGroup>

                <Row>
                    <Col sm={{ size: '4', offset: 8 }}>
                        <Button
                            type={"submit"}
                            disabled={!this.state.formIsValid}
                            onClick={this.handleSubmit}
                        >
                            Submit
                        </Button>
                    </Col>
                </Row>

                {this.state.errorStatus > 0 && (
                    <APIResponseErrorMessage
                        errorStatus={this.state.errorStatus}
                        error={this.state.error}
                    />
                )}
            </div>
        );
    }
}

export default PersonForm;
