import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../api/person-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class PersonFormUpdate extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            errorStatus: 0,
            error: null,

            formIsValid: false,

            formControls: {
                id: {
                    value: '',
                    placeholder: 'Enter the id of the person...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true,
                    },
                },
                name: {
                    value: '',
                    placeholder: 'Enter the new name...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true,
                    },
                },
                username: {
                    value: '',
                    placeholder: 'Enter the new username...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,  // You can add specific validation rules for usernames
                        isRequired: true,
                    },
                },
                password: {
                    value: '',
                    placeholder: 'Enter the new password...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 6,  // Adjust the minimum length for passwords
                        isRequired: true,
                    },
                },
            },
        };
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

    updatePerson(id, name, username, password) {
        return API_USERS.updatePerson(id, name, username, password, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully updated person with id: " + result);
              //  this.reloadHandler();
            } else {
                this.setState({
                    errorStatus: status,
                    error: error,
                });
            }
        });
    }

    handleSubmit = () => {
        const id = this.state.formControls.id.value;
        const name = this.state.formControls.name.value;
        const username = this.state.formControls.username.value;
        const password = this.state.formControls.password.value;
        console.log(password);
        this.updatePerson(id, name, username, password);
    };
    
    render() {
        return (
            <div>
                <FormGroup id='ID'>
                    <Label for='idField'> ID: </Label>
                    <Input
                        name='id'
                        id='idField'
                        placeholder={this.state.formControls.id.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.id.value}
                        valid={this.state.formControls.id.valid}
                        required
                    />
                    {this.state.formControls.id.touched &&
                        !this.state.formControls.id.valid && (
                            <div className={"error-message row"}>
                                * ID must have at least 3 characters and is required.
                            </div>
                        )}
                </FormGroup>
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
                                * Name must have at least 3 characters and is required.
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
                                * Username must have at least 3 characters and is required.
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
                                * Password must have at least 6 characters and is required.
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

export default PersonFormUpdate;
