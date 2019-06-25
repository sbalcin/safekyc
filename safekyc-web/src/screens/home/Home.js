import React, {Component} from "react";

import "./Home.css";
import Login from "../../components/login/Login";
import Responsive from "react-responsive";
import NotificationSystem from "react-notification-system";
import {currentUser} from "../../config/constants";


const Mobile = props => <Responsive {...props} maxWidth={767}/>;
const Default = props => <Responsive {...props} minWidth={768}/>;


var notiStyle = {
    NotificationItem: {
        DefaultStyle: {
            margin: '2px 2px 2px 2px'
        }
    }
}


export default class Home extends Component {

    componentDidMount() {

        if (currentUser()) {
            this.props.history.push('/action');
        }

        this._notificationSystem = this.refs.notificationSystem;

        if (window.localStorage.getItem('message')) {
            this._notificationSystem.addNotification({
                message: window.localStorage.getItem('message'),
                level: 'warning'
            });
            window.localStorage.removeItem('message');
        }
    }


    render() {
        return (
            <div className="home-main-container">
                <NotificationSystem ref="notificationSystem" style={notiStyle} allowHTML={true}/>


                <Default>
                    <div className="home-motto">
                        <h1 className="home-motto-text">Manage your customers easily</h1>
                        <div style={{display: 'flex'}}>
                            <img src={require("../../assets/images/logo/logoBlue.png")} />
                        </div>
                    </div>
                    <div className="login-container-default">
                        <Login history={this.props.history}/>
                    </div>

                </Default>
                <Mobile>
                    <div className="home-motto-mobile">
                        <h2 className="home-motto-text-mobile">Manage Customers</h2>
                        <div style={{display: 'flex'}}>
                            <img src={require("../../assets/images/logo/logoBlue.png")} />
                        </div>
                        <div className="login-container-mobile">
                            <Login history={this.props.history}/>
                        </div>
                    </div>

                </Mobile>

            </div>
        );
    }
}
