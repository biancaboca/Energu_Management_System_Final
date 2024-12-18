import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";


const endpoint = {
    device: '/device'
};


function deleteDevice(ID, callback) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api_device + endpoint.device + "/hasAdminPermision/" + ID, {
        method: 'DELETE',
        headers : headers
        });

    RestApiClient.performRequest(request, (result, status, error) => {
        if (status === 200) {
            callback(result, status, error);
        } else {
            // Handle non-200 status codes or invalid JSON responses
            callback(null, status, error);
        }
    });
}

function postDevice(id,device, callback){
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage
    console.log(token);
    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api_device + endpoint.device+"/hasAdminPermision/"+id , {
        method: 'POST',
        headers : headers,
        body: JSON.stringify(device)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function updateDevice(id,address, description, maxHours, callback){
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api_device + endpoint.device + "/hasAdminPermision/" + id + "/" + address + "/" + description + "/"+maxHours , {
        method: 'POST',
        headers : headers,
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

export {
    postDevice,
    updateDevice,
    deleteDevice,
};
