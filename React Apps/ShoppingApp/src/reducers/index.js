import { combineReducers } from "redux";
import user from './reducer_user';
import items from './reducer_Items';
import cart from './reducers_cart';
import address from './reducer_address';

export default combineReducers({
    user,
    items,
    cart,
    address
})

