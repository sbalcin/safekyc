import React from "react";
import ReactDOM from "react-dom";
import configureStore from "./store/configureStore";
import {Provider} from "react-redux";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import Header from "./components/header/Header";
import Home from "./screens/home/Home";
import Action from "./screens/action/Action";
import ContactUs from './screens/contactUs/ContactUs';
import AboutUs from './screens/aboutUs/AboutUs';
import UserPolicy from './screens/userPolicy/UserPolicy';
import Faq from './screens/faq/Faq';
import NoMatch from './screens/noMatch/NoMatch';
import Footer from "./components/footer/Footer";
import {currentUser} from "./config/constants";

const store = configureStore();
const authed = currentUser();

ReactDOM.render(
    <Provider store={store}>
        <Router>
            <div>
                <Header/>
                <Switch>
                    <Route exact path="/" component={Home}/>
                    <Route path="/action" render={auth('action')}/>
                    <Route path="/user-policy" component={UserPolicy}/>
                    <Route path="/faq" component={Faq}/>
                    <Route path="/contact-us" component={ContactUs}/>
                    <Route path="/about-us" component={AboutUs}/>
                    <Route component={NoMatch}/>
                </Switch>
                <Footer/>
            </div>
        </Router>
    </Provider>,
    document.getElementById('root')
);

function auth(name) {

    if (!authed) {
        checkMessageRequire();
        return (props) => (<Home {...props} />)
    }

    switch (name) {
        case 'action':
            return (props) => <Action {...props} />
        default:
            return (props) => (<Home {...props} />)
    }
}

function checkMessageRequire() {
    try {
        if (window.location.href.indexOf('action') >= 0)
            window.localStorage.setItem('message', 'Please log in or sign up to continue');
    } catch (err) {
        console.error(err);
    }
}

