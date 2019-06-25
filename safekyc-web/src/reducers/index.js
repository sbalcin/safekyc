import {combineReducers} from 'redux';
import authReducer from './authReducer';
import headerReducer from './headerReducer';

const rootReducers = combineReducers({
    auth: authReducer,
    header: headerReducer
})

export default rootReducers;
