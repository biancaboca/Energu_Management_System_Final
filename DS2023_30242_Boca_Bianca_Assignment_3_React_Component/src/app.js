import React from 'react'
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import NavigationBar from './navigation-bar'
import Home from './home/home';
import PersonContainer from './person/person-container'
import Login from './login/login'
import Signup from './commons/singnup/signup';
import UserPage from './userPage/UserPage'
import PrivateRoute from './commons/privateRoute';
import ChartPage from './commons/chartPage/chart';

import ErrorPage from './commons/errorhandling/error-page';
import styles from './commons/styles/project-style.css';

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthenticated: false, // Set this to true when the user is authenticated
            userRole: 'user',
            loginFailed: false, // Set the user's role ('admin' or 'user')
        };
    }


    render() {

        return (
            <div className={styles.back}>
                <Router>
                    <div>
                        <NavigationBar />
                        <Switch>
                            <Route
                                exact
                                path='/home'
                                render={() => <Home />}
                            />
                         <Route
                            exact
                            path='/userPage'
                            render={() => (
                                <UserPage
                                />
                            )}
/>
                            <Route
                                exact
                                path='/person'
                                render={() => <PersonContainer />}
                            />

                            <Route
                                exact
                                path='/login'
                                render={() => <Login />}
                              
                            />
                            <Route
                                exact
                                path='/signup'
                                render={() => <Signup />}
                            />
                              <Route
                                exact
                                path='/chart'
                                render={()=><ChartPage />}
                            />
                            <Route
                                exact
                                path='/error'
                                render={() => <ErrorPage />}
                            />
                          

                            <Route render={() => <ErrorPage />} />
                        </Switch>
                    </div>
                </Router>
            </div>
        )
    };
}

export default App
