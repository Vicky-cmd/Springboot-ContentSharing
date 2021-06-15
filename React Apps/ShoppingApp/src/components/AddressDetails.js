import React, { Component } from 'react';
import {connect} from 'react-redux';
import { Link } from 'react-router-dom';
import {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData, addAddressInfo, addNewAddress, deleteAddress, updateAddress} from '../actions';
import notAvailable from '../images/not-available.png';
import Razorpay from 'razorpay';
import CustomAlert from './Alert'
import { useHistory } from "react-router-dom";
import Payments from './Payments';
import SavedAddresses from './SavedAddresses';

class AddressDetails extends Component {

    state = {displayAddressForm: false, updateForm: false, updateAddress: {}, updateDelAddr: false}
    
    componentDidUpdate() {

        if(this.state.displayAddressForm && this.state.updateForm) {
            const { addressId, houseNo, addressLine1, addressLine2, landmark, city, state, pincode,  } = this.state.updateAddress;
            this.refs.hno.value = houseNo;
            this.refs.addr1.value = addressLine1;
            this.refs.addr2.value = addressLine2;
            this.refs.landmark.value = landmark;
            this.refs.city.value = city;
            this.refs.state.value = state;
            this.refs.pincode.value = pincode;
        }
    }


    setupdateDelAddrState = (flag) => {
        this.props.setupdateDelAddrState(flag)
    }
 
    updateDelAddress = (addressId) => {
        let req = {};
        console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/tagAddressToCustomer?addrId=" + addressId + "&custId=" + this.props.userInfo.userId;
        console.log(url);
        const reqOptions = {
            method: "PUT",
            headers: {
                 "Content-Type": "application/json",
                 "Accept": "application/json"
            }
        }
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.checkResponseForAddrDtls(json))
            .catch(error => console.log(error));
    }

    checkResponseForAddrDtls = (json) => {
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 200) {
            this.props.setupdateDelAddrStateWithoutUpdate(false);
            this.state.displayAddressForm= false; 
            this.state.updateForm= false;
            this.state.updateAddress= {};
            this.props.fetchCartData();
        }
    }

    updateFormData = () => {
        const hno = this.refs.hno.value;
        const addr1 = this.refs.addr1.value;
        const addr2 = this.refs.addr2.value;
        const landmark = this.refs.landmark.value;
        const city = this.refs.city.value;
        const state = this.refs.state.value;
        const pincode = this.refs.pincode.value;

        let req = {};
        req.houseNo=hno;
        req.addressLine1=addr1;
        req.addressLine2=addr2;
        req.landmark=landmark;
        req.city=city;
        req.state=state;
        req.pincode=pincode;
        req.custId=this.props.userInfo.userId;
        console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/address/updateDeliveryAddress?addrId=" + this.state.updateAddress.addressId;
        console.log(url);
        const reqOptions = {
            method: "POST",
            headers: {
                 "Content-Type": "application/json",
                 "Accept": "application/json"
            },
            body: JSON.stringify(req)
        }
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.updateAddressData(json, req))
            .catch(error => console.log(error));

    }

    updateAddressData = (json, req) => {
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 200) {
            req.addressId = json.addressId;
            this.props.updateAddress(req);
            this.setState({displayAddressForm: false, updateForm: false, updateAddress: {}})
        }
    }

    editDelAddress = () => {
        this.setState({displayAddressForm: false, updateForm: false, updateAddress: {}, updateDelAddr: true})
        this.setupdateDelAddrState(true);
    }


    updateAddress = (address) => {
        console.log("UPDATE Address Function Called!");
        this.setState({updateForm: true, updateAddress: address, displayAddressForm: true});
    }

    formAdddress = (addr, format) => {
        const { addressId, houseNo, addressLine1, addressLine2, landmark, city, state, pincode,  } = addr;
        if(format === 'formatted') {
            return  (<div>{houseNo + ", "} <br /> {addressLine1 + ", " + addressLine2 + ", "} <br />  {landmark + ", "} <br /> {city + ", "} <br />  {state + " - " + pincode}</div>);
        } else {
            return houseNo + ", " + addressLine1 + ", " + addressLine2 + ", " + landmark + ", " + city + ", " + state + " - " + pincode;
        }
    }

    displayAddress = (addr) => {
        
        this.refs.prevAddr.value = this.formAdddress(addr, 'unformatted')
    }
    

    saveFormData = () => {
        const hno = this.refs.hno.value;
        const addr1 = this.refs.addr1.value;
        const addr2 = this.refs.addr2.value;
        const landmark = this.refs.landmark.value;
        const city = this.refs.city.value;
        const state = this.refs.state.value;
        const pincode = this.refs.pincode.value;

        let req = {};
        req.houseNo=hno;
        req.addressLine1=addr1;
        req.addressLine2=addr2;
        req.landmark=landmark;
        req.city=city;
        req.state=state;
        req.pincode=pincode;
        req.custId=this.props.userInfo.userId;
        console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/address/addDeliveryAddress";
        console.log(url);
        const reqOptions = {
            method: "POST",
            headers: {
                 "Content-Type": "application/json",
                 "Accept": "application/json"
            },
            body: JSON.stringify(req)
        }
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.addNewAddress(json, req))
            .catch(error => console.log(error));

    }

    addNewAddress = (json, req) => {
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 201) {
            req.addressId = json.addressId;
            this.props.addNewAddress(req);
            this.setState({displayAddressForm: false});
        }
    }

    
    render() {
        return(
            <div style={{width: "98%", borderRadius: 20, boxShadow: '5px 5px 5px', margin: 'auto', padding: 30, marginTop: 30, marginBottom: 20}}>
                <h1 style={{width: "100%", textAlign: 'center', fontVariant: 'small-caps'}}>Address Details</h1>
                
                <div>
                    <div style={{width:"45%", margin: 'auto', minWidth: "200px", marginTop: 20}}>
                        <textarea value="" ref="prevAddr" rows="3" style={{width: "100%", resize: 'none', overflowY: 'scroll'}} disabled />
                    </div>
                    <div className="adrressMenuItems" style={{}}>
                        {
                            !this.state.displayAddressForm?
                                <div className="addressFieldEdit"><i className="fa fa-plus-square-o" onClick={() => this.setState({displayAddressForm: true, updateForm: false})} style={{}}></i></div>
                                :
                                <div className="addressFieldEdit"><i className="fa fa fa-minus-square-o" onClick={() => this.setState({displayAddressForm: false, updateForm: false, updateAddress: {}})} style={{}}></i></div>
                        }
                        {
                            this.props.showCancelOpt?
                            <div className="addressFieldCancelOption"><i className="far fa-times-circle" onClick={() => {this.setupdateDelAddrState(false)}} style={{}}></i></div>
                            :
                            null
                        }
                    </div>
                </div>
                <SavedAddresses displayAddress={this.displayAddress} updateAddress={this.updateAddress} updateDelAddress={this.updateDelAddress}/>
                {
                    this.state.displayAddressForm?(
                        <div style={{width: '30%', minWidth: 100, margin: 'auto', marginTop: 25}}>
                            <form>
                                <div className="form-group">
                                    <label className="font-weight-bold">House Number</label>
                                    <input ref="hno" className="form-control form-control-sm" required="required" placeholder="House No." />
                                </div>
                                <div className="form-group">
                                    <label className="font-weight-bold">Address Line 1</label>
                                    <input ref="addr1" className="form-control form-control-sm" required="required" placeholder="Address Line 1" />
                                </div>
                                <div className="form-group">
                                    <label className="font-weight-bold">Address Line 2</label>
                                    <input ref="addr2" className="form-control form-control-sm" required="required" placeholder="Address Line 2" />
                                </div>
                                <div className="form-group">
                                    <label className="font-weight-bold">Landmark</label>
                                    <input ref="landmark" className="form-control form-control-sm" required="required" placeholder="Landmark" />
                                </div>
                                <div className="form-group">
                                    <label className="font-weight-bold">City</label>
                                    <input ref="city" className="form-control form-control-sm" required="required" placeholder="City" />
                                </div>
                                <div className="form-group">
                                    <label className="font-weight-bold">State</label>
                                    <input ref="state" className="form-control form-control-sm" required="required" placeholder="State" />
                                </div>
                                <div className="form-group">
                                    <label className="font-weight-bold">Pin Code</label>
                                    <input ref="pincode" type="number" min="99999" max="1000000" className="form-control form-control-sm" required="required" placeholder="Pin Code" />
                                </div>
                                <div className="form-group" style={{width: 'fit-content', margin: 'auto', padding: 5}}>
                                    {
                                        !this.state.updateForm?
                                        <button type="button" onClick={this.saveFormData} className="btn btn-warning" style={{margin: 5}}>Save</button>
                                        :
                                        <button type="button" onClick={this.updateFormData} className="btn btn-warning" style={{margin: 5}}>Update</button>
                                    }
                                    <button type="button" onClick={() => {this.setState({displayAddressForm: false, updateForm: false, updateAddress: {}})}} className="btn" style={{margin: 5}}>Cancel</button>
                                </div>

                            </form>
                        </div>
                    ):null
                }
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
  
export default connect(mapStateToProps, {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData, addAddressInfo, addNewAddress, deleteAddress, updateAddress})(AddressDetails);