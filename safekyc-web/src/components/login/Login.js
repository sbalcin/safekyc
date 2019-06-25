import React from 'react';

import './Login.css';
import $ from 'jquery';
import {signIn, signUp} from '../../actions';
import {connect} from 'react-redux';
import Loader from '../loader/Loader';
import LaddaButton from "react-ladda";
import * as qs from 'query-string';
import {currentUser} from "../../config/constants";
const requestIp = require('request-ip');


class Login extends React.Component {

    constructor(props) {
        super(props);
        var state = {
            isShowingLogin: true
        }
        this.state = state;
        this.showSignInPanel = this.showSignInPanel.bind(this);
        this.showSignUpPanel = this.showSignUpPanel.bind(this);
    }

    componentDidMount() {

    }

    showSignInPanel() {
        if (!this.isFormLocked()) {
            this.setState({isShowingLogin: true});
            $(".form-sign-up").removeClass("active-sx");
            $(".form-sign-in").removeClass("inactive-dx");
            $(".form-sign-in").addClass("active-dx");
            $(".form-sign-up").addClass("inactive-sx");
            $(".form-sign-in").css("height", 300);
            $(".form-sign-up").css("height", 450);
        }
    }

    showSignUpPanel() {
        if (!this.isFormLocked()) {
            this.setState({isShowingLogin: false});
            $(".form-sign-in").removeClass("active-dx");
            $(".form-sign-up").removeClass("inactive-sx");
            $(".form-sign-up").addClass("active-sx");
            $(".form-sign-in").addClass("inactive-dx");
            $(".form-sign-up").css("height", 530);
        }
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.userAuthed && currentUser()) {
            window.location.reload();
        }
        if (nextProps.headerSignUpClicked.headerSignUpClicked && this.state.isShowingLogin) {
            this.showSignUpPanel();
        }
    }

    onSignInKeyPress = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            $('#signInBtn').click();
        }
    }

    onSignUpKeyPress = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            $('#signUpBtn').click();
        }
    }

    onSignInClicked = () => {
        if (!this.isFormLocked()) {
            this.props.signIn(this.signInEmail.value, this.signInPassword.value);
        }
    }

    onSignUpClicked = () => {

        fetch("https://api.ipdata.co/?api-key=test")
            .then(response => {
                return response.json();
            }, "jsonp")
            .then(res => {
                if (!this.isFormLocked()) {
                    this.props.signUp(this.signUpEmail.value, this.signUpPassword.value, this.signUpName.value, this.signUpBirthDate.value, this.signUpNationality.value, this.signUpPhone.value, res.ip);
                }
            })
            .catch(err => console.log(err));
    }

    isFormLocked = () => {
        return this.props.showSignInProgress || this.props.showSignUpProgress;
    }




    render() {

        return (
            <div className="login-container">

                {/* Sign Up */}
                <form className="form-sign-up">
                    {
                        this.props.showSignUpProgress &&
                        <Loader/>
                    }
                    <h3 className="form-h3">Sign Up</h3>
                    <div className="md-form form-sm">
                        <input id="sign-up-name" className="w100 form-text-area form-control" type="text"
                               ref={(signUpName) => {
                                   this.signUpName = signUpName
                               }} onKeyPress={this.onSignUpKeyPress}/>
                        <label htmlFor="sign-up-name">Name Surname</label>
                    </div>
                    <div className="md-form form-sm">
                        <input id="sign-up-email" className="w100 form-text-area form-control" type="email"
                               ref={(signUpEmail) => {
                                   this.signUpEmail = signUpEmail
                               }} onKeyPress={this.onSignUpKeyPress}/>
                        <label htmlFor="sign-up-email">EMail Address</label>
                    </div>
                    <div className="md-form form-md">
                        <input id="sign-up-birthDate" className="w100 form-text-area form-control" type="date"
                               ref={(signUpBirthDate) => {
                                   this.signUpBirthDate = signUpBirthDate
                               }} onKeyPress={this.onSignUpKeyPress}/>
                        <label htmlFor="sign-up-email"></label>
                    </div>
                    <div className="md-form form-sm">
                        <input id="sign-up-nationality" className="w100 form-text-area form-control" type="text"
                               ref={(signUpNationality) => {
                                   this.signUpNationality = signUpNationality
                               }} onKeyPress={this.onSignUpKeyPress}/>
                        <label htmlFor="sign-up-nationality">Nationality</label>
                    </div>
                    <div className="md-form form-sm">
                        <input id="sign-up-nationality" className="w100 form-text-area form-control" type="text"
                               ref={(signUpPhone) => {
                                   this.signUpPhone= signUpPhone
                               }} onKeyPress={this.onSignUpKeyPress}/>
                        <label htmlFor="sign-up-phone">Phone Number</label>
                    </div>
                    <div className="md-form form-sm">
                        <input id="ign-up-password" className="form-text-area" type="password"
                               ref={(signUpPassword) => {
                                   this.signUpPassword = signUpPassword
                               }} onKeyPress={this.onSignUpKeyPress}/>
                        <label htmlFor="sign-up-password">Password</label>
                    </div>

                    {
                        this.props.signUpError &&
                        <div className="form-error-container">
                            <label className="form-error-label">{this.props.signUpError}</label>
                        </div>
                    }
                    {
                        this.props.signUpSuccess &&
                        <div className="form-error-container">
                            <label className="form-info-label">Successfully signed up</label>
                        </div>
                    }

                    <LaddaButton type="button" loading={this.props.showSignUpProgress} id="signUpBtn"
                                 onClick={this.onSignUpClicked} className="form-btn dx log-in" data-style='zoom-out'>
                        Sign Up
                    </LaddaButton>
                    <button className="form-btn sx log-in" type="button" onClick={this.showSignInPanel}>Sign In</button>
                </form>

                {/* Sign In */}
                {
                    !this.state.showForgotPasswordPanel && !this.props.showSignInWithPhone &&
                    <form className="form-sign-in">
                        {
                            this.props.showSignInProgress &&
                            <Loader/>
                        }
                        <h3 className="form-h3">Sign In</h3>
                        <div className="md-form form-sm">
                            <input id="sign-in-email" className="w100 form-text-area form-control" type="email"
                                   ref={(signInEmail) => {
                                       this.signInEmail = signInEmail
                                   }} onKeyPress={this.onSignUpKeyPress}/>
                            <label htmlFor="sign-in-email">EMail Address</label>
                        </div>
                        <div className="md-form form-sm">
                            <input id="sign-in-password" className="form-text-area form-control" type="password"
                                   ref={(signInPassword) => {
                                       this.signInPassword = signInPassword
                                   }} onKeyPress={this.onSignInKeyPress}/>
                            <label htmlFor="sign-in-password">Password</label>
                        </div>


                        {
                            this.props.signInError &&
                            <div className="form-error-container" style={{flexDirection: 'column', display: 'flex'}}>
                                <label className="form-error-label">{this.props.signInError}</label>
                            </div>
                        }
                        <LaddaButton type="button" id="signInBtn" loading={this.props.showSignInProgress}
                                     onClick={this.onSignInClicked} className="form-btn dx log-in"
                                     data-style='zoom-out'>
                            Sign In
                        </LaddaButton>
                        <button className="form-btn sx back" type="button" onClick={this.showSignUpPanel}>Sign Up
                        </button>
                    </form>
                }

            </div>
        );
    }
}

const mapStateToProps = ({auth, header}) => {
    const {user, signInError, signUpError, signUpSuccess, showSignInProgress, showSignUpProgress, userAuthed} = auth;
    const headerSignUpClicked = header;
    return {user, signInError, signUpError, signUpSuccess, showSignInProgress, showSignUpProgress, headerSignUpClicked, userAuthed};
}

export default connect(mapStateToProps, {signIn, signUp})(Login);
