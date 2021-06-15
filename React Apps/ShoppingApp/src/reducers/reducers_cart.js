import {ADD_CART_QTY, UPDATE_CART_DATA, DELETE_CART_DATA} from '../constants';

const createCartData = (action) => {
    const {qty, cartId, cartData} = action;
    return {
        qty,
        cartId, 
        cartData,
        id: Math.random()
    }
}

const updateCartQty = (action, state) => {
    const {qty, cartId} = action;
    return {
        qty,
        cartId, 
        cartData: (typeof state.cartData !== 'undefined')?state.cartData:{},
        id: Math.random()
    }
}

const cartReducer = (state=[], action) => {
    let cartInfo={};
    switch(action.type) {
        case ADD_CART_QTY:
            cartInfo = updateCartQty(action, state);
            return cartInfo;
        case UPDATE_CART_DATA:
            cartInfo = createCartData(action);
            return cartInfo;
        case DELETE_CART_DATA:
            return cartInfo;
        default:
            return state;
    }
}

export default cartReducer;