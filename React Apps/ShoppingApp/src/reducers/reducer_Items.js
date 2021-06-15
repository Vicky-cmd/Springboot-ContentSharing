import {ITEM_DATA} from '../constants';
import {saveuserInfo} from '../actions';

const items = (action) => {
    const {itemsLst} = action;
    return {
        itemsLst,
        id: Math.random()
    }
}

const itemsReducer = (state=[], action) => {
    let itemInfo={};
    switch(action.type) {
        case ITEM_DATA:
            itemInfo = items(action);
            return itemInfo;
        default:
            return state;
    }
}

export default itemsReducer;