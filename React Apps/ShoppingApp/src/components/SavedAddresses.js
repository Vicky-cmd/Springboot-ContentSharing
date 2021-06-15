import React, { Component } from 'react';
import {connect} from 'react-redux';
import { Link } from 'react-router-dom';
import {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData, addAddressInfo, deleteAddress} from '../actions';
import notAvailable from '../images/not-available.png';
import CustomAlert from './Alert'
import Razorpay from 'razorpay';
import { useHistory } from "react-router-dom";
import Payments from './Payments';

class Address extends Component {
    

    displayValue = (addr) => () => {
        this.props.displayAddress(addr);
    }
    deleteEntry = (addressId) => () => {
        let req = {};
        console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/address/deleteAddress?addrId=" + addressId;
        console.log(url);
        const reqOptions = {
            method: "GET",
            headers: {
                 "Content-Type": "application/json",
                 "Accept": "application/json"
            }
        }
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.deleteAddress(json, addressId))
            .catch(error => console.log(error));
    }

    deleteAddress = (json, addressId) => {
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 200) {
            this.props.deleteAddress(addressId);
        }
    }

    updateAddress = (address) => () => {
        console.log("CALLING PARENT FUNCTION ", this.props);
        this.props.updateAddress(address)
    }

    render() {
        const { addressId, city, state, pincode,  } = this.props.addr;
        return(
            <div className="col-xs-12 col-sm-12 col-md-6 col-lg-4" >
                    <div className="savedAddressItem" style={{boxShadow: "5px 5px 5px"}}>
                        <div style={{display: 'inline-flex', verticalAlign: 'middle'}}>
                            <input type="radio" name="savedAddress" value={addressId} onClick={this.displayValue(this.props.addr)}/>
                        </div>
                        <div style={{padding: 20}}>
                            <h3>{pincode}</h3>
                            <h6>{city}, {state}.</h6>
                            
                        <div style={{paddingTop: 10}}>    
                            <i className="fa fa-edit" onClick={this.updateAddress(this.props.addr)} style={{padding: 5, cursor: 'pointer', float: 'left'}}></i>
                            <i className="fa" style={{fontSize: "24px", float: 'right', cursor: 'pointer'}} onClick={this.deleteEntry(addressId)}>&times;</i>
                        </div>
                    </div>
                        
                    </div>
            </div>
        )
    }
}


class SavedAddresses extends Component {


    state = {selectedAddressId: 0};

    displayAddress = (addr) => {
       this.props.displayAddress(addr);
    }

    handleOnChange = (event) => {
        console.log(event.target.value);
        this.state.selectedAddressId= event.target.value;
    }

    updateDeliveryAddress = () => {
        this.props.updateDelAddress(this.state.selectedAddressId);
    }

    render() {
        const addressArr = this.props.addressInfo.address;
        console.log(this.props.addressInfo);
        if(typeof addressArr === 'undefined') {
            return(
                <div></div>
            )
        }
        return (
            <div className="container" style={{width: "100%", margin: "auto", padding: 20}}>
                <div onChange={this.handleOnChange} className="row">
                    {
                        addressArr.map(addr => (
                            <Address key={addr.addressId} updateAddress={this.props.updateAddress} addr={addr} displayAddress={this.displayAddress} deleteAddress={this.props.deleteAddress}/>
                        ))
                    }
                </div>

                <div style={{width: 'fit-content', margin: 'auto', marginTop: 20}}>
                    <button className="btn btn-success" onClick={this.updateDeliveryAddress}>Deliver Here</button>
                </div>

            </div>
        )
    }

}

function mapStateToProps(state) {
    return {
      userInfo: state.user,
      itemInfo: state.items,
      cartInfo: state.cart,
      addressInfo: state.address
    }
  }
  
export default connect(mapStateToProps, {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData, addAddressInfo, deleteAddress})(SavedAddresses);