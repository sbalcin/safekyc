import * as types from './actionTypes';

export function onHeaderSignUpClicked() {
  return(dispatch, getState) => {
    return dispatch({
      type: types.HEADER_SIGN_UP_CLICKED
    });
  }
}
