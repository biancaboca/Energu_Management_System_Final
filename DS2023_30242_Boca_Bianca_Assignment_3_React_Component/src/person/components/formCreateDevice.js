import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import * as API_DEVICE from "../../commons/device/device-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class CreateFormDevice extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            errorStatus: 0,
            error: null,

            formIsValid: false,


            formControls: {
                id: {
                    value: '',
                    placeholder: 'Enter the new ID...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true,
                    },
                },
                description: {
                    value: '',
                    placeholder: 'Enter the new description...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true,
                    },
                },
                address: {
                    value: '',
                    placeholder: 'Enter the new address...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
                        isRequired: true,
                    },
                },
                maxHours: {
                    value: '',
                    placeholder: 'Enter the new maxHours...',
                    valid: false,
                    touched: false,
                    validationRules: {
                        minLength: 3,
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

    createDevice(id,device) {
        return API_DEVICE.postDevice(id,device, (result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("Successfully created device with id: " + result);
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

        let device = {
            name: this.state.formControls.description.value,
            address: this.state.formControls.address.value,
            maxHours: this.state.formControls.maxHours.value,
        };
        this.createDevice(id, device);
    };

    render() {
        return (
            <div>
                <FormGroup id='id'>
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

                <FormGroup id='description'>
                    <Label for='descriptionField'> Description: </Label>
                    <Input
                        name='description'
                        id='descriptionField'
                        placeholder={this.state.formControls.description.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.description.value}
                        valid={this.state.formControls.description.valid}
                        required
                    />
                    {this.state.formControls.description.touched &&
                        !this.state.formControls.description.valid && (
                            <div className={"error-message row"}>
                                * Description must have at least 3 characters and is required.
                            </div>
                        )}
                </FormGroup>
                <FormGroup id='address'>
                    <Label for='addressField'> Address: </Label>
                    <Input
                        name='address'
                        id='addressField'
                        placeholder={this.state.formControls.address.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.address.value}
                        valid={this.state.formControls.address.valid}
                        required
                    />
                    {this.state.formControls.address.touched &&
                        !this.state.formControls.address.valid && (
                            <div className={"error-message"}>
                                * Address must have at least 3 characters and is required.
                            </div>
                        )}
                </FormGroup>
                <FormGroup id='maxHours'>
                    <Label for='maxHoursField'> Max Hours: </Label>
                    <Input
                        name='maxHours'
                        id='maxHoursField'
                        placeholder={this.state.formControls.maxHours.placeholder}
                        onChange={this.handleChange}
                        value={this.state.formControls.maxHours.value}
                        valid={this.state.formControls.maxHours.valid}
                        required
                    />
                    {this.state.formControls.maxHours.touched &&
                        !this.state.formControls.maxHours.valid && (
                            <div className={"error-message"}>
                                * Max Hours must have at least 3 characters and is required.
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

export default CreateFormDevice;
