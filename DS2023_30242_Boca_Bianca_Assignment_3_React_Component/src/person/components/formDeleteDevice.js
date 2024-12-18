import React from 'react';
import validate from "./validators/person-validators";
import Button from "react-bootstrap/Button";
import { deleteDevice } from '../../commons/device/device-api';
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';

class DeviceFormDelete extends React.Component {
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

deletePerrson(id) {
    return deleteDevice(id, (result, status, error) => {
        if (status === 200) {
            // Handle successful deletion
            console.log("Device deleted successfully");
        } else {
            // Handle the error
            console.error(`Error deleting device (Status: ${status}): ${error}`);
            this.setState({
                errorStatus: status,
                error: error,
            });
        }
    });
}

    

    handleSubmit = () => {
        const id = this.state.formControls.id.value;
       
        this.deletePerrson(id);
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

export default DeviceFormDelete;
