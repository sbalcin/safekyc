import * as types from '../actions/actionTypes';

const initialState = {
    user: null,
    signInError: null,
    signUpError: null,
    showSignInProgress: false,
    showSignUpProgress: false,
}

export default function authReducer(state = initialState, action) {
    switch (action.type) {
        case types.SHOW_SIGN_IN_PROGRESS:
            return {...state, showSignInProgress: true, signInError: null}
        case types.SHOW_SIGN_UP_PROGRESS:
            return {...state, showSignUpProgress: true, signUpError: null}
        case types.USER_SIGNED_IN:
            return {...state, user: action.payload, showSignInProgress: false, userAuthed: true};
        case types.USER_SIGNED_UP:
            return {...state, user: action.payload, showSignUpProgress: false, signUpSuccess: true};
        case types.USER_SIGN_IN_ERROR:
            return {...state, signInError: action.payload, signUpError: null, showSignInProgress: false, user: null};
        case types.USER_SIGN_UP_ERROR:
            return {...state, signInError: null, signUpError: action.payload, showSignUpProgress: false, user: null, signUpSuccess: false};
        default:
            return state;
    }
}
