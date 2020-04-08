import * as types from './actionTypes';

import {authSignIn, authSignOut, authSignUp} from '../api/AuthApi';

export function signIn(email, password) {
    return (dispatch, getState) => {
        if (!email || !password) {
            return dispatch({
                type: types.USER_SIGN_IN_ERROR,
                payload: "Check email and password"
            });
        }
        dispatch({
            type: types.SHOW_SIGN_IN_PROGRESS
        });

        authSignIn(email, password).then(function (response) {
            if (response.error) {
                dispatch({
                    type: types.USER_SIGN_IN_ERROR,
                    payload: response.error,
                });
            } else{
                window.localStorage.setItem('userKey', email);
                window.localStorage.setItem('token', response.user);
<<<<<<< HEAD
                window.localStorage.setItem('tokenExpire', new Date().getTime() + (1000 * 60 * 60));
=======
>>>>>>> fdb9163e536ed3ca9a5322ce24c08445b802f623
                return dispatch({
                    type: types.USER_SIGNED_IN,
                    payload: response.user
                });
            }
        });
    }
}


export function signUp(email, password, name, birth, nat, phone, clientIp) {
    return (dispatch, getState) => {

        if (!email || !password || !name) {
            return dispatch({
                type: types.USER_SIGN_UP_ERROR,
                payload: "email, password or name can not be null"
            });
        }
        dispatch({
            type: types.SHOW_SIGN_UP_PROGRESS
        });
        authSignUp(email, password, name, birth, nat, phone, clientIp).then(function (response) {
            if (response.error) {
                var errorMessage = response.error;
                dispatch({
                    type: types.USER_SIGN_UP_ERROR,
                    payload: errorMessage
                });
            } else{
                return dispatch({
                    type: types.USER_SIGNED_UP,
                    payload: response.user
                });
            }
        });
    }
}


export function signOut() {
    window.localStorage.removeItem('userKey');
    window.localStorage.removeItem('token');
    return (dispatch, getState) => {
        authSignOut();
    }
}
