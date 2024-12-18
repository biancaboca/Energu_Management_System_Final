import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";


const endpoint = {
    monotoring: '/monotoring/hasAllPermision'
};

function getListMeasured(callback, id) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_monotoring + endpoint.monotoring + "/getAll/"+id, {
        method: 'GET',
        headers: headers,
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function getAllDevicesMeasured(callback) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_monotoring + endpoint.monotoring + "/getAllDevicesMeasured", {
        method: 'GET',
        headers: headers,
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function getOwner(id, callback) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_monotoring + endpoint.monotoring + "/getOwner/"+id, {
        method: 'GET',
        headers: headers,
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function getDate(id, callback) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_monotoring + endpoint.monotoring + "/getDate/"+id, {
        method: 'GET',
        headers,
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}
function getTimestamp(callback, idOwner, idDevice) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_monotoring + endpoint.monotoring + "/getTimestamp/"+idOwner+"/"+idDevice, {
        method: 'GET',
        headers: headers,
    });
    console.log(request.url);
    RestApiClient.performRequest(request, (response, status) => {
        if (status === 200) {
            try {
                const result = JSON.parse(response);
                callback(result, status, null);
            } catch (e) {
                callback(null, status, 'Invalid JSON response: ' + e.toString());
            }
        } else {
            callback(null, status, 'Error fetching data');
        }
    });


}

export {
    getListMeasured,
    getAllDevicesMeasured,
    getTimestamp,
    getOwner,
    getDate
}
