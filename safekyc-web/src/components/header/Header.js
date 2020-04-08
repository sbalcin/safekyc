import React from "react";

import "./Header.css";
import Responsive from "react-responsive";
import {connect} from "react-redux";
import {onHeaderSignUpClicked, signOut} from "../../actions";
import {Redirect} from 'react-router';
import $ from 'jquery';
import {currentUser} from "../../config/constants";

const Mobile = props => <Responsive {...props} maxWidth={767}/>;
const Default = props => <Responsive {...props} minWidth={768}/>;

var user;
var username;

class Header extends React.Component {

    constructor(props) {
        super(props);

        user = currentUser();
        if (user)
            username = user;

        this.state = {
            redirectToHome: false
        };
    }

    componentDidMount = () => {
    }

    componentWillUpdate = () => {
    }

    onSignUpClicked = () => {
        this.props.onHeaderSignUpClicked();
        $('html,body').animate({scrollTop: 0}, 0);
    }

    onSignUpClickedFromMobile = () => {
        this.props.onHeaderSignUpClicked();
        $('html,body').animate({scrollTop: 480}, 0);
    }

    onSignOutClicked = () => {
        this.props.signOut();
        this.setState({redirectToHome: true});
    }

    checkSessionIsActive() {
        try {
<<<<<<< HEAD
            /*if ((parseFloat(user.stsTokenManager.expirationTime) < parseFloat(new Date()))) {
                window.localStorage.setItem('message', 'Session expired');
                this.onSignOutClicked();
            }*/
            if (window.localStorage.getItem('token') && parseFloat(window.localStorage.getItem('tokenExpire')) < new Date().getTime()) {
                window.localStorage.setItem('message', 'Session expired');
                window.localStorage.removeItem('token');
                this.onSignOutClicked();
            }

=======
            if ((parseFloat(user.stsTokenManager.expirationTime) < parseFloat(new Date()))) {
                window.localStorage.setItem('message', 'Session expired');
                this.onSignOutClicked();
            }
>>>>>>> fdb9163e536ed3ca9a5322ce24c08445b802f623
        } catch (error) {
            console.log(error);
        }
    }

    render() {
<<<<<<< HEAD
        this.checkSessionIsActive();
=======

>>>>>>> fdb9163e536ed3ca9a5322ce24c08445b802f623
        if (this.state.redirectToHome) {
            this.setState({redirectToHome: false});
            window.location.reload();
            return (
                <Redirect to={'/'}/>
            );
        }


        return (
            <nav className="navbar fixed-top navbar-dark navbar-expand-lg navigation-container">

                <Default>
                    <a href="/" className="navbar-brand navigation-logo"><img src={require("../../assets/images/logo/logo.png")} style={{height: '24px', marginTop: '-4px', marginLeft: '80px'}}/></a>
                </Default>
                <Mobile>
                    <a href="/" className="navbar-brand"><img src={require("../../assets/images/logo/logo.png")} style={{height: '24px', marginTop: '-10px'}}/></a>
                </Mobile>

                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation"><span className="navbar-toggler-icon"></span></button>

                <div className="collapse navbar-collapse" id="navbarSupportedContent">

                    <ul className="navbar-nav mr-auto">
<<<<<<< HEAD
                        <li className="nav-item"><a className="nav-link" href="/action">Safe KYC</a></li>
=======
                        <li className="nav-item"><a className="nav-link" href="/">Safe KYC</a></li>
>>>>>>> fdb9163e536ed3ca9a5322ce24c08445b802f623
                        <li className="nav-item"><a className="nav-link" href="/about-us">About Us</a></li>
                        <li className="nav-item"><a className="nav-link" href="/about-us">Services</a></li>
                        <li className="nav-item"><a className="nav-link" href="/contact-us">Contact</a></li>

                        {
                            user &&
                            <Mobile>
                                {
                                    user &&
                                    <li className="nav-item dropdown">
                                        <button className="nav-link dropdown-toggle headerDropdownBtn" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                                                aria-haspopup="true" aria-expanded="false">
                                            {username}
                                        </button>
                                        <div className="dropdown-menu dropdown dropdown-primary userBalanceComboMobile" aria-labelledby="navbarDropdown">
                                            <a className="dropdown-item waves-light" href="#" onClick={this.onSignOutClicked}>Sign Out</a>
                                        </div>
                                    </li>
                                }
                            </Mobile>
                        }
                        {
                            !user &&
                            <Mobile>
                                <li className="nav-item"><a className="nav-link" href={window.location.origin.replace(/\//g, '') !== window.location.href.replace(/\//g, '') ? '/': '#'} onClick={this.onSignUpClickedFromMobile}>Sign Up</a></li>
                            </Mobile>
                        }
                    </ul>
                    <Default>

                        <ul className="navbar-nav justify-content-end navigation-item-right">
                            {
                                user &&
                                <li className="nav-item dropdown">
                                    <button className="nav-link dropdown-toggle headerDropdownBtn" href="#" id="navbarDropdownC" role="button" data-toggle="dropdown"
                                            aria-haspopup="true" aria-expanded="false">
                                        {username}
                                    </button>
                                    <div className="dropdown-menu dropdown dropdown-primary userBalanceCombo" aria-labelledby="navbarDropdown">
                                        <a className="dropdown-item" href="#" onClick={this.onSignOutClicked}>Sign Out</a>
                                    </div>
                                </li>
                            }

                            {
                                !user &&
                                <li className="nav-item"><a className="nav-link" href={window.location.origin.replace(/\//g, '') !== window.location.href.replace(/\//g, '') ? '/': '#'} onClick={this.onSignUpClicked}>Sign Up</a></li>
                            }
                        </ul>
                    </Default>
                </div>

            </nav>

        )
    }
}

const mapStateToProps = ({header, auth}) => {
    const headerSignUpClicked = header;
    const user = auth;
    return {headerSignUpClicked, user};
}

export default connect(mapStateToProps, {onHeaderSignUpClicked, signOut})(Header);


