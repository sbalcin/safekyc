import React from "react";
import "./Action.css";
import {
    retrieveQRCodeUri,
    shareQRCodeUri,
    verifyQRCodeUri,
    retrieveSourceActionsUri,
    retrieveTargetActionsUri,
} from "../../config/constants";
import Responsive from "react-responsive";
import NotificationSystem from "react-notification-system";
import axios from "axios";
import LaddaButton from "react-ladda";
import Popup from 'reactjs-popup'
import moment from "moment/moment";
import CSSTransitionGroup from "react-transition-group/CSSTransitionGroup";
import CountDown from 'reactjs-countdown';
import QrReader from 'react-qr-reader';
import adapter from 'webrtc-adapter';





const Mobile = props => <Responsive {...props} maxWidth={767}/>;
const Default = props => <Responsive {...props} minWidth={768}/>;

var token;
var taskResponse;
var taskItemResponse;

var notiStyle = {
    NotificationItem: {
        DefaultStyle: {
            margin: '2px 2px 2px 2px'
        }
    }
}

export default class Action extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            shareQRCodePopUpOpen: false,
            scanQRCodePopUpOpen: false,
            shareQRCodeSecret:'',
            shareQRCodeSecretExpire:'',
            scanQRCodeSecret:'',
            shareQRCodeData: [],
            scanQRCodeData: [],
            sourceActions: [],
            targetActions: [],
            currentTaskItems: [],
            taskItemDependencies: [],
            ctiPopUpOpen: false,
            dpPopUpOpen: false,
            selectedDependency: '',
            scanResult: 'No result',
            verifiedQRCodeName: '',
            verifiedQRCodeEmail: '',
            verifiedQRCodePhoneNumber:''
        };
        token = localStorage.getItem('token');

        this.retrieveQRCode();
        this.retrieveSourceActions();
        this.retrieveTargetActions();


        this.closeShareQRCodeModal = this.closeShareQRCodeModal .bind(this)
        this.closeScanQRCodeModal = this.closeScanQRCodeModal .bind(this)

    }

    addToNotifications(mess, level) {
        this._notificationSystem.addNotification({message: mess, level: level, position: 'tc'});
    }


    componentWillMount() {

    }

    componentDidMount() {
        this._notificationSystem = this.refs.notificationSystem;
    }

    componentDidUpdate(prevProps, prevState) {

    }

    addToNotifications(mess, level) {
        this._notificationSystem.addNotification({message: mess, level: level, position: 'tc'});
    }

    retrieveQRCode() {
        var data = {
            token: token
        };
        return axios.post(retrieveQRCodeUri, data).then(response => {
            if (response.data.result == 'success') {
                var base64Flag = 'data:image/png;base64,';
                this.setState({shareQRCodeData: base64Flag + response.data.data})
            }
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        })
    }

    retrieveSourceActions() {
        var data = {
            token: token
        };
        return axios.post(retrieveSourceActionsUri, data).then(response => {
            if (response.data.result == 'success') {
                this.setState({sourceActions: response.data.actionItemResponses})
            }
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        })
    }

    retrieveTargetActions() {
        var data = {
            token: token
        };
        return axios.post(retrieveTargetActionsUri, data).then(response => {
            if (response.data.result == 'success') {
                this.setState({targetActions: response.data.actionItemResponses})
            }
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        })
    }


    shareQRCodeClicked = (e) => {
        try {
            e.preventDefault();
            this.setState({shareQRCodeBtnLoading: true})
            this.setState({shareQRCodePopUpOpen: true})
        } catch (err) {
            console.log('err.. ' + err);
        }
    }

    scanQRCodeClicked = (e) => {
        try {
            e.preventDefault();
            this.setState({scanQRCodePopUpOpen: true})
            this.setState({verifiedQRCodeName: null})
        } catch (err) {
            console.log('err.. ' + err);
        }
    }

    closeShareQRCodeModal() {
        this.setState({shareQRCodeBtnLoading: false})
        this.setState({shareQRCodePopUpOpen: false})
    }

    closeScanQRCodeModal() {
        this.setState({scanQRCodePopUpOpen: false})
    }

    scanQRCodeSecretChange = (e) => {
        this.state.scanQRCodeSecret = e.target.value;
        window.fixMaterializeTextOverlap();
        this.forceUpdate();
    }

    shareQRCodeConfirmClicked = (e) => {
        e.preventDefault();
        this.setState({shareQRCodeConfirmBtnLoading: true})

        var data = {
            token: token
        };
        return axios.post(shareQRCodeUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Secret code created successfully', 'success');
                this.setState({shareQRCodeSecret: response.data.secret})
                let shareQRCodeSecretExpire = new Date(response.data.expire).toString();
                this.setState({shareQRCodeSecretExpire: shareQRCodeSecretExpire})
            }

            setTimeout(() => {
                this.setState({shareQRCodeConfirmBtnLoading: false})
            }, 100);
        }).catch(err => {
            throw err;
        });

    }

    scanQRCodeConfirmClicked = (e) => {
        e.preventDefault();
        this.addToNotifications('Show qr code to the camera', 'warning');
    }


    handleScan = data => {
        if (data) {
            this.setState({ scanResult: data });
            if(this.state.scanQRCodeSecret.length < 1){
                this.addToNotifications('Secret code can not be empty', 'error');
            } else {
                var data = {
                    token: token,
                    uuid: this.state.scanResult,
                    secret: this.state.scanQRCodeSecret
                };
                return axios.post(verifyQRCodeUri, data).then(response => {
                    if (response.data.result == 'success') {
                        this.addToNotifications('QR code identified successfully', 'success');
                        this.setState({verifiedQRCodeName: response.data.name});
                        this.setState({verifiedQRCodeEmail: response.data.email});
                        this.setState({verifiedQRCodePhoneNumber: response.data.phoneNumber});
                    }

                }).catch(err => {
                    this.addToNotifications(err.response.data, 'error');
                });
            }
        }
    }

    handleError = err => {
        console.error(err)
    }
    render() {


        return (
            <div className="task-main-container">
                <NotificationSystem ref="notificationSystem" style={notiStyle} allowHTML={true}/>

                <div>
                    <div className="card">
                        <div className="card-header list-group-item-success tabHeader">QR Code Details</div>
                        <div className="card-body cardBody">
                            <div style={{display: 'flex'}}>
                                <div>
                                    <img src={this.state.shareQRCodeData} />
                                </div>

                                <div className="actions">

                                    <div>
                                        <LaddaButton loading={this.state.shareQRCodeBtnLoading} onClick={(e) => {
                                            this.shareQRCodeClicked(e)
                                        }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out' style={{width: '20%'}}>
                                            Share QR Code
                                        </LaddaButton>

                                        <LaddaButton loading={this.state.shareQRCodeBtnLoading} onClick={(e) => {
                                            this.scanQRCodeClicked(e)
                                        }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out' style={{width: '20%'}}>
                                            Scan QR Code
                                        </LaddaButton>

                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <br/>
                <br/>

                <div>
                        <div className="flex-container">
                            <div className="card">
                                <div className="card-header list-group-item-success tabHeader">Your Transactions</div>
                                <div className="card-body cardBody">
                                    <form>
                                        <div className="md-form form-sm">
                                            <table className="table table-hover table-sm">
                                                <thead className="mTableHeader">
                                                <tr>
                                                    <td>Action Type</td>
                                                    <td>Creation Date</td>
                                                    <td>Target Username</td>
                                                </tr>
                                                </thead>
                                                <CSSTransitionGroup transitionName="fade"
                                                                    transitionEnterTimeout={200}
                                                                    transitionLeaveTimeout={200}
                                                                    component="tbody">


                                                    {this.state.sourceActions.map(action => (
                                                        <tr key={action.actionItemId}>
                                                            <td>{action.actionType}</td>
                                                            <td><span
                                                                className="date timeago"> {moment(action.creationDate).fromNow()}</span>
                                                            </td>
                                                            <td>{action.targetUserName}</td>
                                                        </tr>
                                                    ))}
                                                </CSSTransitionGroup>
                                            </table>
                                        </div>

                                    </form>
                                </div>
                            </div>
                        </div>
                </div>

                <br/>
                <br/>

                <div>
                    <div className="flex-container">
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Transactions Related With You</div>
                            <div className="card-body cardBody">
                                <form>
                                    <div className="md-form form-sm">
                                        <table className="table table-hover table-sm">
                                            <thead className="mTableHeader">
                                            <tr>
                                                <td>Action Type</td>
                                                <td>Creation Date</td>
                                                <td>Target Username</td>
                                            </tr>
                                            </thead>
                                            <CSSTransitionGroup transitionName="fade"
                                                                transitionEnterTimeout={200}
                                                                transitionLeaveTimeout={200}
                                                                component="tbody">


                                                {this.state.targetActions.map(action=> (
                                                    <tr key={action.actionItemId}>
                                                        <td>{action.actionType}</td>
                                                        <td><span
                                                            className="date timeago"> {moment(action.creationDate).fromNow()}</span>
                                                        </td>
                                                        <td>{action.targetUserName}</td>
                                                    </tr>
                                                ))}
                                            </CSSTransitionGroup>
                                        </table>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>
                </div>



                <Popup open={this.state.shareQRCodePopUpOpen} closeOnDocumentClick onClose={this.closeShareQRCodeModal}>

                    <div className="conversionDetails">
                        <a className="close" onClick={this.closeShareQRCodeModal}>
                            &times;
                        </a>
                        <br/>
                        <br/>
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Share Your QR Code</div>
                            <div className="card-body cardBody">
                                <form>
                                    <div className="md-form form-sm">
                                        <br/>
                                        <label>System will generate a code for you to share with your friend. Send the secret code and qr code to your friend through email, phone call or sms. Make sure that your friend will scan it before timeout (5 minute).</label>
                                        <br/>
                                    </div>

                                    {
                                        this.state.shareQRCodeSecret &&
                                        <div className="md-form form-sm">
                                            <label>Secret Code : {this.state.shareQRCodeSecret} </label>
                                            <br/>

                                        </div>
                                    }

                                    {
                                        this.state.shareQRCodeSecretExpire &&
                                        <div className="countDown">
                                            <CountDown deadline={this.state.shareQRCodeSecretExpire}
                                            />
                                        </div>
                                    }

                                </form>
                            </div>
                            <br/>
                            <br/>
                            <div className="actions">
                                <LaddaButton style={{fontSize: 'small'}}
                                             loading={this.state.shareQRCodeConfirmBtnLoading} onClick={(e) => {
                                    this.shareQRCodeConfirmClicked(e)
                                }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out'>
                                    Share</LaddaButton>
                                <LaddaButton style={{fontSize: 'small'}}    onClick={(e) => {
                                    this.setState({shareQRCodePopUpOpen: false})
                                }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out'>
                                    Cancel</LaddaButton>
                            </div>
                        </div>
                        <br/>
                        <br/>
                    </div>
                </Popup>

                <Popup open={this.state.scanQRCodePopUpOpen} closeOnDocumentClick onClose={this.closeScanQRCodeModal}>

                    <div className="conversionDetails">
                        <a className="close" onClick={this.closeScanQRCodeModal}>
                            &times;
                        </a>
                        <br/>
                        <br/>
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Scan QR Code</div>
                            <div className="card-body cardBody">
                                <form>

                                    {
                                        !this.state.verifiedQRCodeName &&
                                        <div>
                                            <div className="form-sm">
                                                <label>Make sure to get secret key from qr code owner.</label>
                                            </div>

                                            <div>
                                                <QrReader
                                                    delay={300}
                                                    onError={this.handleError}
                                                    onScan={this.handleScan}
                                                    style={{ width: '100%' }}
                                                />
                                            </div>
                                            <br/>
                                            <div className="md-form form-sm">
                                                <input type="text" className="form-control" id="taskName"
                                                       value={this.state.scanQRCodeSecret}
                                                       onChange={this.scanQRCodeSecretChange}/>
                                                <label>Secret Code</label>
                                            </div>
                                        </div>
                                    }

                                    {
                                        this.state.verifiedQRCodeName &&
                                        <div>
                                            <label>Here is the scan result</label>

                                            <div className="form-sm">
                                                <br/>
                                                <label>Name:</label>
                                                <label>{this.state.verifiedQRCodeName}</label>
                                                <br/>
                                            </div>
                                            <div className="form-sm">
                                                <br/>
                                                <label>Email:</label>
                                                <label>{this.state.verifiedQRCodeEmail}</label>
                                                <br/>
                                            </div>
                                            <div className="form-sm">
                                                <br/>
                                                <label>Phone Number:</label>
                                                <label>{this.state.verifiedQRCodePhoneNumber}</label>
                                                <br/>
                                            </div>

                                        </div>
                                    }

                                </form>
                            </div>
                            <br/>
                            {
                                !this.state.verifiedQRCodeName &&
                                <div className="actions">
                                    <LaddaButton style={{fontSize: 'small'}} onClick={(e) => {
                                        this.scanQRCodeConfirmClicked(e)
                                    }} className="btn btn-default waves-effect transaction-tl-button"
                                                 data-style='zoom-out'>
                                        Confirm</LaddaButton>
                                    <LaddaButton style={{fontSize: 'small'}} onClick={(e) => {
                                        this.setState({scanQRCodePopUpOpen: false})
                                    }} className="btn btn-default waves-effect transaction-tl-button"
                                                 data-style='zoom-out'>
                                        Cancel</LaddaButton>
                                </div>
                            }
                        </div>
                        <br/>
                        <br/>
                    </div>
                </Popup>

            </div>
        )
    }
}
