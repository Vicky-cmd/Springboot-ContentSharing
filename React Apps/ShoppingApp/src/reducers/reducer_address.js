import {ADD_ADDRESS_INFO, ADD_NEW_ADDRESS, DELETE_ADDRESS, UPDATE_ADDRESS} from '../constants';
import {addAddressInfo, addNewAddress} from '../actions';

const createAddressDtls = (action) => {
    const {address} = action;
    return {
        address
    }
}

const addNewAddressDts = (action, state) => {
    const {address} = action;
    let addrInfo = state.address;
    if(typeof addrInfo === 'undefined') {
        addrInfo = [];
    }
    addrInfo.push(address);
    return {
        address:addrInfo
    }
}

const deleteAddress = (action, state) => {
    const {addressId} = action;
    let addrInfo = state.address;
    if(typeof addrInfo === 'undefined') {
        addrInfo = [];
    } else {
        addrInfo = addrInfo.filter(addr => (addressId !== addr.addressId));
    }

    return {
        address:addrInfo
    }
}

const updateAddress = (action, state) => {
    const {address} = action;
    let addrInfo = state.address;
    if(typeof addrInfo === 'undefined') {
        addrInfo = [];
    } else {
        addrInfo = addrInfo.filter(addr => (address.addressId !== addr.addressId));
    }
    addrInfo.push(address);

    return {
        address:addrInfo
    }
}

const addressReducer = (state=[], action) => {
    let addressInfo={};
    switch(action.type) {
        case ADD_ADDRESS_INFO:
            addressInfo = createAddressDtls(action);
            return addressInfo;
        case ADD_NEW_ADDRESS:
            addressInfo = addNewAddressDts(action, state);
            return addressInfo;
        case DELETE_ADDRESS:
            addressInfo = deleteAddress(action, state);
            return addressInfo;
        case UPDATE_ADDRESS:
            addressInfo = updateAddress(action, state);
            return addressInfo;
        default:
            return state;
    }
}

export default addressReducer;