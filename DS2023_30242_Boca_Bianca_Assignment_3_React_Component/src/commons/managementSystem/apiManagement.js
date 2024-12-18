import {HOST} from '../../commons/hosts';
import RestApiClient from "../../commons/api/rest-client";


const endpoint = {
    person: '/person',
    device: '/device'
};

function getPersons(callback) {
    let request = new Request(HOST.backend_api + endpoint.person, {
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonById(params, callback){
    let request = new Request(HOST.backend_api + endpoint.person + params.id, {
       method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getPersonByName(params, callback)
{
    let request = new Request(HOST.backend_api + endpoint.person, params.username,
        {
            method: 'GET'
        });
        RestApiClient.performRequest(request,callback);

    
}
function logInFunc(username, password, callback) {
    let request = new Request(
      HOST.backend_api + endpoint.person + "/" + username + "/" + password,
      {
        method: 'GET',
      }
    );
    RestApiClient.performRequest(request,callback);

    // RestApiClient.performRequest(request, (result, status, err) => {
    //   try {
    //     if (result) {
    //       const jsonData = JSON.parse(result);
    //       callback(jsonData, status, err);
    //     } else {
    //       // Handle the case where the response is empty or not valid JSON
    //       console.log('Invalid JSON respons');
    //       callback(null, status, 'Invalid JSON response');
    //     }
    //   } catch (error) {
    //     // Handle JSON parsing error
    //     console.log('JSON parsing error:');
    //     callback(null, status, 'JSON parsing error');
    //   }
    // });
  }
  
function deletePerson(ID, callback)
{
    let request = new Request(HOST.backend_api + endpoint.person + "/"+ ID ,
        {
            method: 'DELETE'
        });
        console.log("URL: " + request.url);
        RestApiClient.performRequest(request,callback);

    
}

function verifyingAdmin(id, callback)
{
    let request = new Request(HOST.backend_api  + endpoint.person + "/verifying/"+ id,
    {
        method: 'GET'
    });
    RestApiClient.performRequest(request,callback);
}

function postPerson(user, callback){
    let request = new Request(HOST.backend_api + endpoint.person , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function updatePerson(id,name, username, password, callback){
    let request = new Request(HOST.backend_api + endpoint.person + "/" + id + "/" + name + "/" + username + "/"+password , {
        method: 'POST',
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function getAllDevicesOfUser(id, callback)
{
    let request = new Request(HOST.backend_api_device+endpoint.device+"/getAll/"+id,{
        method: 'GET',
    })
    RestApiClient.performRequest(request,callback);
}

export {
    
};
