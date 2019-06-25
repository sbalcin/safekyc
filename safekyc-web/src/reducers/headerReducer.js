import * as types from '../actions/actionTypes';

const initialState = {
    headerSignUpClicked: false
}

export default function headerReducer(state = initialState, action) {
    switch (action.type) {
        case types.HEADER_SIGN_UP_CLICKED:
            return {...state, headerSignUpClicked: true};
        default:
            return {...state, headerSignUpClicked: false};
    }
}
