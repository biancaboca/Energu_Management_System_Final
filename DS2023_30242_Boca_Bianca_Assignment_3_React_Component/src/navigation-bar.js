import React from 'react'
import logo from './commons/images/icon.png';
import { Link } from 'react-router-dom'; // Import the Link component

import {
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
    Nav,
    Navbar,
    NavbarBrand,
    NavLink,
    UncontrolledDropdown
} from 'reactstrap';

const textStyle = {
    color: 'white',
    textDecoration: 'none',
    fontWeight: 'bold', 
};


const background ={
    color:'black'
};

const NavigationBar = () => (
    <div>
        {/* <Navbar color="dark" light expand="md">
            <NavbarBrand href="/">
                <img src={logo} width={"50"}
                     height={"35"} />
            </NavbarBrand>
            <Nav className="mr-auto" navbar>

                <UncontrolledDropdown nav inNavbar>
                    <DropdownToggle style={textStyle} nav caret>
                       Menu
                    </DropdownToggle>
                    <DropdownMenu right >

                        <DropdownItem>
                            <NavLink href="/person">Persons</NavLink>
                        </DropdownItem>


                    </DropdownMenu>
                </UncontrolledDropdown>

            </Nav>
        </Navbar> */}
        <nav class="navbar navbar-expand-lg navbar-light bg-dark" style={background}>
  <a class="navbar-brand" href="#" style={textStyle}>
    <Link to = '/home'>
  </Link>
  WELCOME!</a>
  <button
    class="navbar-toggler"
    type="button"
    data-toggle="collapse"
    data-target="#navbarNavDropdown"
    aria-controls="navbarNavDropdown"
    aria-expanded="false"
    aria-label="Toggle navigation"
  >
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarNavDropdown">
    <ul class="navbar-nav" style={{ justifyContent: 'flex-start' }}>
    <li class="nav-item" className="nav-link" style={textStyle}>
        <a class="nav-link" href="#" style={textStyle}>
        <Link to="/home">
          HOME
          </Link>
        </a>
        </li>
      <li class="nav-item" className="nav-link" style={textStyle}>
        <a class="nav-link" href="#" style={textStyle}>
        <Link to="/login">
          LOGIN
          </Link>
        </a>
        </li>
        <li class="nav-item" className="nav-link" style={textStyle}>
        <a class="nav-link" href="#" style={textStyle}>
        <Link to="/signup">
          SIGN UP
          </Link>
        </a>
      </li>
    </ul>
    <form class="form-inline">
      <input
        class="form-control mr-sm-2"
        type="search"
        placeholder="Search"
        aria-label="Search"
      ></input>
      <button
        class="btn btn-outline-success my-2 my-sm-0"
        type="submit"
        style={textStyle}
      >
        Search
      </button>
    </form>
  </div>
</nav>

    </div>
);

export default NavigationBar
