import React from 'react';

import BackgroundImg from '../commons/images/gray.jpg';
import {Button, Container, Jumbotron} from 'reactstrap';
import ChatBox from './chatBox';

const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: '100%',
    height: '100vh',
    backgroundImage: `url(${BackgroundImg})`
};

const imageStyle = {
    display: 'block',        // This makes the image a block element
    margin: '0 auto',        // Center horizontally
    width: '30%',            // Adjust the width as needed
    height: '30%',           // Adjust the height as needed
  };
  

const textStyle = {color: 'black', fontWeight: 'bold', decoration: 'none', textAlign: 'center'};

class Home extends React.Component {


    render() {

        return (

            <div>
                 <Jumbotron fluid style={backgroundStyle}>
                <Container fluid>
                    <h1 className="display-3" style={textStyle}>MANAGEMENT OF ENERGY SYSTEM</h1>
                    <img src={require('../commons/images/energy.png')} style={imageStyle} alt="Management of Energy System" />
                </Container>
                <ChatBox></ChatBox>
            </Jumbotron>

            </div>
        )
    };
}

export default Home
