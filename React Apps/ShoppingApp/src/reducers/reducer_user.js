import {USER_INFO} from '../constants';
import {saveuserInfo} from '../actions';

const users = (action) => {
    const {fullname, userId, username, isAdmin} = action;
    return {
        fullname, 
        userId, 
        username,
        isAdmin,
        id: Math.random()
    }
}


const usersReducer = (state=[], action) => {
    let userInfo={};
    switch(action.type) {
        case USER_INFO:
            userInfo = users(action);
            return userInfo;
        default:
            return state;
    }
}

export default usersReducer;