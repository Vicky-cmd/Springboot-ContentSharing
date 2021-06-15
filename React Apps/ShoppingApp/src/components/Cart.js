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
import AddressDetails from './AddressDetails';

class CartItems extends Component {


    updateCartQty = (json) => {
        // console.log("Inside update");
        // console.log(typeof json.statusCode !== 'undefined' && (json.statusCode === 200 || json.statusCode === 0));
        if(typeof json.statusCode !== 'undefined' && (json.statusCode === 200 || json.statusCode === 0)) {
            console.log(this.props);
            this.props.updateCartQty(json.cartQuantity, json.cartId);
            const cartData = json.cartData;
            if(typeof cartData !== 'undefined') {
                this.saveCartData(cartData);
            } else {
                this.fetchCartData();
            }
            // (new CustomAlert()).success("Operation Success!")
        } 
    }

    saveCartData = (cartData) => {
        this.props.saveCartData(cartData);
    }

    fetchCartData = () => {
        // console.log("Calling parent fetch");
        this.props.fetchCartData();
    }

    removeFromCart = (pid, qty) => () => {
        console.log("[Item Id - ", pid, ", Qty - ", qty, "]");
        let req = {};
        req.pId = pid;
        req.quantity = qty;
        req.applyOffersInSync = true;
        console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/removeItemFromCart/" + this.props.userInfo.userId;
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
            .then(json => this.updateCartQty(json))
            .catch(error => console.log(error));
    }

    addToCart = (pid, qty) => () => {
        // let qty = this.refs.itemQty.value;
        console.log("[Item Id - ", pid, ", Qty - ", qty, "]");
        let req = {};
        req.pId = pid;
        req.quantity = qty;
        req.applyOffersInSync = true;
        // console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/addItemToCart/" + this.props.userInfo.userId;
        // console.log(url);
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
            .then(json => this.updateCartQty(json))
            .catch(error => console.log(error));
    }

    render() {
        const {imgUrl, name, desc, totalAmount, quantity, price, discount, pId} = this.props.cartItem;
        return (
            <div className="cartItemData" style={{}}>

                <div style={{float: 'left'}}>
                    {
                        (typeof imgUrl === 'undefined' || imgUrl === null)?
                        <img style={{height: 200, width: 200}} src={notAvailable} />
                        :
                        <img style={{height: 200, width: 200}} src={imgUrl} />
                    }
                </div>
                <div style={{padding: 10}}>
                    <h2>{name}</h2>
                    <h4>{desc}</h4>
                    <h6>Unit Price: Rs. {price}</h6>
                    {
                        (discount > 0.0)?
                        <h5>
                            Total Amount: Rs. <span style={{textDecoration: 'line-through'}}>{totalAmount}</span>
                            Rs. {totalAmount - discount}<br/>
                            <em>Total Discount: </em> Rs. {discount}
                        </h5>
                        :
                        <h5>Total Amount: Rs. <span>{totalAmount}</span></h5>
                    }
                </div>
                <div className="cartItemQtyCtrl" style={{}}>
                    <div className="input-group">
                        <div className="input-group-prepend">
                            <button className="btn btn-danger" type="button" onClick={this.removeFromCart(pId, 1)}><i className="fa fa-minus"></i></button>
                        </div>
                        <input className="form-control" style={{width: "70px", textAlign: 'center', alignContent: 'flex-end'}} type="number" ref="qty" value={quantity>=10?quantity: ("0" + quantity)} disabled/>
                        <div className="input-group-append">
                            <button className="btn btn-primary" type="button" onClick={this.addToCart(pId, 1)}><i className="fa fa-plus"></i></button>
                        </div>
                    </div>
                    <div className="removeCartItem"><button title="Remove Item From Cart" onClick={this.removeFromCart(pId, quantity)} className="btn btn-link" style={{}}><i style={{fonSize:"64px",color:"red"}} className="far fa-trash-alt"></i></button></div>
                </div>
            </div>
        )
    }
}

class Cart extends Component {


    state = {displayPayments: false, updateDelAddr: false}

    componentDidMount() {
        this.fetchCartData();
    }

    saveItemData = (json) => {
        this.props.saveItemData(json);
    }

    fetchCartData = () => {
        console.log("Inside fetchCartData");
        if(typeof this.props.userInfo.userId === 'undefined') {
            return;
        }
        const reqOptions = {
            method: "get",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        };
        console.log("Fetching data => ",this.props);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/getActiveCart/" + this.props.userInfo.userId;
        console.log(url);
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.saveCartData(json))
            .catch(error => console.log(error));
    }

    abandonCart = (cartId) => () => {
        console.log("Inside abandonCart");
        const reqOptions = {
            method: "get",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        };
        console.log("Fetching data => ",this.props);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/abandonCart/" + cartId;
        console.log(url);
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.processResponseabandonCart(json))
            .catch(error => console.log(error));
    }

    
    processResponseabandonCart = (json) => {
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 200) {
            this.props.deleteCartData();
            this.fetchCartData();
        }
    }

    saveCartData = (json) => {
        
        console.log("Save Cart Data");
        if(typeof json.cartId !== 'undefined') {
            const {cartId, totQuantity} = json;
            console.log(json);
            console.log("Calling Get Address");
            this.getAddressData();
            this.props.updateCartData(totQuantity, cartId, json);
        }
    }

    getAddressData = () => {
        console.log("Inside Get Address");
        if(typeof this.props.userInfo.userId === 'undefined') {
            return;
        }
        console.log("Calling API");
        const reqOptions = {
            method: "get",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        };
        console.log("Fetching data => ",this.props);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/address/findStoredAddr?custId=" + this.props.userInfo.userId;
        console.log(url);
        fetch(url, reqOptions)
            .then(response => response.json())
            .then(json => this.saveAddressData(json))
            .catch(error => console.log(error));
    }

    saveAddressData = (json) => {
        if(typeof json !== 'undefined' && json.length>0) {
            this.props.addAddressInfo(json);
        } 
    }

    render() {
        console.log("Cart", this.props);
        let itemInfo = [];
        if(typeof this.props.itemInfo.itemsLst !== 'undefined') {
            itemInfo = this.props.itemInfo.itemsLst;
        }

        console.log("Cart Data =>=>=>=>", this.props.cartInfo.cartData);

        if(typeof this.props.cartInfo.cartData === 'undefined') {
            return(
                <div>
                    <div style={{width: 'fit-content', margin: 'auto', textAlign: 'center'}}>
                        <h2>No Active Cart Found!</h2>
                        <Link to="/retailApp/Items"><h4>Items</h4></Link>
                    </div>
                </div>
            );
        }
        const {cartId, totalAmount, discAmt, offersDesc, totQuantity, offersApplied, cartItems, deliveryAddress} = this.props.cartInfo.cartData;

        // if(typeof cartItems === 'undefined') {
        //     return(<div></div>);
        // }

        return(
            <div className="login" style={{padding: 10, margin: 10, marginTop: 30, borderRadius: "10px"}}>
                <div>
                    <h2>Cart Id: #{cartId}</h2>
                    {
                        offersApplied==="Y"?
                        (
                            <div style={{textAlign: 'end'}}>
                                <h5> Total Quantity: {totQuantity}</h5>
                                <h4> Cart Amount: Rs. <span style={{textDecoration: 'line-through'}}>
                                {totalAmount}</span></h4><h5>Rs. {totalAmount - discAmt}</h5>
                                <h6>{offersDesc}</h6>
                            </div>
                        )
                        :
                        <h4> Cart Amount: {totalAmount}</h4>
                    }

                    <div style={{padding: 10, marginTop: 20}}>

                        {
                            cartItems.map(item => (
                                <CartItems key={item.pId} cartItem={item} userInfo={this.props.userInfo} updateCartQty={this.props.updateCartQty} fetchCartData={this.fetchCartData} saveCartData={this.saveCartData}/>
                            ))
                        }   
                    </div>
                    <div style={{width: "fit-content", margin: 'auto'}}>
                        <button onClick={this.abandonCart(cartId)} type="button" className="btn btn-danger" style={{width: 200, minWidth: "100px", textAlign: 'center'}}>Abandon Cart</button>    
                    </div>
                </div>
                {
                    !((typeof deliveryAddress !== 'undefined' && deliveryAddress !== null && deliveryAddress.addressId > 0) && !this.state.updateDelAddr)?
                        <AddressDetails setupdateDelAddrState={this.setupdateDelAddrState} setupdateDelAddrStateWithoutUpdate={this.setupdateDelAddrStateWithoutUpdate} updateDelAddr={this.state.updateDelAddr} fetchCartData={this.fetchCartData} showCancelOpt={(typeof deliveryAddress !== 'undefined' && deliveryAddress !== null && deliveryAddress.addressId > 0)}/>
                        :
                        <div style={{margin: 'auto', width: '60%', textAlign: 'center', verticalAlign: 'middle' , boxShadow: '5px 5px 5px', padding: 10, marginTop: 25}}>
                            <h2>Delivery Address</h2>
                            <i className="fa fa-edit" onClick={this.editDelAddress} style={{padding: 5, cursor: 'pointer', float: 'right'}}></i>
                            {this.formAdddress(deliveryAddress, 'formatted')}
                        </div>
                }
                {
                    ((typeof deliveryAddress !== 'undefined' && deliveryAddress !== null && deliveryAddress.addressId > 0) && !this.state.updateDelAddr)?
                    <Payments />
                    :
                    null
                }

            </div>
        )
    }

    setupdateDelAddrState = (flag) => {
       this.setState({updateDelAddr: flag}); 
    }

    setupdateDelAddrStateWithoutUpdate = (flag) => {
        this.state.updateDelAddr = flag;
    }


    editDelAddress = () => {
        this.setState({updateDelAddr: true})
    }


    formAdddress = (addr, format) => {
        const { addressId, houseNo, addressLine1, addressLine2, landmark, city, state, pincode,  } = addr;
        if(format === 'formatted') {
            return  (<div>{houseNo + ", "} <br /> {addressLine1 + ", " + addressLine2 + ", "} <br />  {landmark + ", "} <br /> {city + ", "} <br />  {state + " - " + pincode}</div>);
        } else {
            return houseNo + ", " + addressLine1 + ", " + addressLine2 + ", " + landmark + ", " + city + ", " + state + " - " + pincode;
        }
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
  
export default connect(mapStateToProps, {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData, addAddressInfo, addNewAddress, deleteAddress, updateAddress})(Cart);