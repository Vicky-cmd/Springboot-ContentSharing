import {USER_INFO, ITEM_DATA, ADD_CART_QTY, UPDATE_CART_DATA, DELETE_CART_DATA, ADD_ADDRESS_INFO, ADD_NEW_ADDRESS, DELETE_ADDRESS, UPDATE_ADDRESS} from '../constants';

export const saveuserInfo = (fullname, userId, username, isAdmin) => {
    console.log(fullname, userId, username, isAdmin);
    return {
        type: USER_INFO,
        fullname,
        userId, 
        username,
        isAdmin
    }
}

export const saveItemData = (itemsLst) => {
    // console.log(itemsLst);
    return {
        type: ITEM_DATA,
        itemsLst
    }
}

export const updateCartQty = (qty, cartId) => {
    // console.log(qty, cartId);
    return {
        type: ADD_CART_QTY,
        qty,
        cartId
    }
}

export const updateCartData = (qty, cartId, cartData) => {
    // console.log(qty, cartId);
    return {
        type: UPDATE_CART_DATA,
        qty,
        cartData
    }
}

export const deleteCartData = () => {
    return {
        type: DELETE_CART_DATA
    }
}

export const addAddressInfo = (address) => {
    return {
        type: ADD_ADDRESS_INFO,
        address

    }
}

export const addNewAddress = (address) => {
    return {
        type: ADD_NEW_ADDRESS,
        address

    }
}

export const deleteAddress = (addressId) => {
    return {
        type: DELETE_ADDRESS,
        addressId

    }
}

export const updateAddress = (address) => {
    return {
        type: UPDATE_ADDRESS,
        address
    }
}
