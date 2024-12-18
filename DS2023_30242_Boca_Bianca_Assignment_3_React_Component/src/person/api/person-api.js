import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";


const endpoint = {
    person: '/person',
    device: '/device'
};

function getPersons(callback) {
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage
    localStorage.setItem("tokenAdmin", token);

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api +  endpoint.person +"/hasAdminPermision/getAll", {
        method: 'GET',
        headers:headers,
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonById(params, callback){
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api + endpoint.person +"/hasAdminPermision/"+ params.id, {
       method: 'GET',
       headers:headers,
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonByName(params, callback)
{
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api + endpoint.person +"/hasAdminPermision/", params.username,
        {
            method: 'GET',
            headers: headers,
        });
        RestApiClient.performRequest(request,callback);

    
}
function logInFunc(username, password, callback) {
    let request = new Request(HOST.backend_api + "/api/v1/auth/authenticate", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({username, password})
    });
    RestApiClient.performRequest(request, callback);
}
// function logInFunc(username, password, callback) {
//     let request = new Request(
//       HOST.backend_api + endpoint.person + "/" + username + "/" + password,
//       {
//         method: 'GET',
//       }
//     );
//     RestApiClient.performRequest(request,callback);

//     // RestApiClient.performRequest(request, (result, status, err) => {
//     //   try {
//     //     if (result) {
//     //       const jsonData = JSON.parse(result);
//     //       callback(jsonData, status, err);
//     //     } else {
//     //       // Handle the case where the response is empty or not valid JSON
//     //       console.log('Invalid JSON respons');
//     //       callback(null, status, 'Invalid JSON response');
//     //     }
//     //   } catch (error) {
//     //     // Handle JSON parsing error
//     //     console.log('JSON parsing error:');
//     //     callback(null, status, 'JSON parsing error');
//     //   }
//     // });
//   }
  
function deletePerson(ID, callback)
{
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api + endpoint.person + "/hasAdminPermision/"+ ID ,
        {
            method: 'DELETE',
            headers : headers
        });
        console.log("URL: " + request.url);
        RestApiClient.performRequest(request,callback);

    
}

function verifyingAdmin(id, callback)
{
    let request = new Request(HOST.backend_api  + endpoint.person + "/hasAllPermision/verifying/"+ id,
    {
        method: 'GET'
    });
    RestApiClient.performRequest(request,callback);
}

function postPerson(user, callback){
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage

    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api + endpoint.person +"/hasAdminPermision/" , {
        method: 'POST',
        headers : headers,
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function updatePerson(id,name, username, password, callback){
    const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage
    let headers = new Headers({
        'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
        'Accept': 'application/json',
        'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
    });
    let request = new Request(HOST.backend_api + endpoint.person + "/hasAdminPermision/" + id + "/" + name + "/" + username + "/"+password , {
        method: 'POST',
        headers: headers,
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function getAllDevicesOfUser(id, callback)
{  const token = sessionStorage.getItem('authToken'); // Retrieve the JWT token from session storage
    let headers = new Headers({
    'Authorization': `Bearer ${token}`, // Add the token to the Authorization header
    'Accept': 'application/json',
    'Content-Type': 'application/json', // Ensure the server knows you're sending JSON
});
    let request = new Request(HOST.backend_api_device+endpoint.device+"/hasAllPermision/getAll/"+id,{
        method: 'GET',
        headers:headers,
    })
    RestApiClient.performRequest(request,callback);
}

export {
    getPersons,
    getPersonById,
    postPerson,
    getPersonByName,
    logInFunc,
    verifyingAdmin,
    updatePerson,
    deletePerson,
    getAllDevicesOfUser
};
