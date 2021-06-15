import React, { Component } from 'react';
import {connect} from 'react-redux';
import { Link } from 'react-router-dom';
import {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData} from '../actions';
import notAvailable from '../images/not-available.png';
import { useLocation } from "react-router-dom";

class CartData extends Component {

    formAdddress = (addr, format) => {
        const { addressId, houseNo, addressLine1, addressLine2, landmark, city, state, pincode,  } = addr;
        if(format === 'formatted') {
            return  (<div>{houseNo + ", "} <br /> {addressLine1 + ", " + addressLine2 + ", "} <br />  {landmark + ", "} <br /> {city + ", "} <br />  {state + " - " + pincode}</div>);
        } else {
            return houseNo + ", " + addressLine1 + ", " + addressLine2 + ", " + landmark + ", " + city + ", " + state + " - " + pincode;
        }
    }

    formpaymentDetails = (paymentInfo) => {
        const {paymentType, paymentStatus, paymentMode, currencyType, amountTendered, amountReturned, razorPay_paymentAmount, razorPay_orderId, razorPay_receiptId} = paymentInfo;
        if(paymentMode !== 'undefined' && paymentMode === 'CoD') {
            return (
                <div>
                    <h5>Payment Mode - Cash On Delivery</h5>
                    {
                        paymentStatus === 'P'?
                           <h6>Payment Pending - Items Yet to be Delivered</h6>
                           :
                           <h6>Payment Completed</h6> 
                    }
                    {
                        paymentStatus === 'C'?
                            <div>
                                Currency Type: {currencyType} <br />
                                Amount Tendered: {amountTendered} <br />
                                Amount Returned: {amountReturned}
                            </div>
                            :
                            null
                    }
                </div>
            )
        } else {
            return (
                <div>
                    <h5>Payment Mode - Online Payment</h5>
                    {
                        paymentType === 'RP'?
                            <div>
                                <h6>Payment Type: Razorpay</h6>
                                Order Id: {razorPay_orderId} <br />
                                Receipt Id: {razorPay_receiptId} <br />
                                Payment Amount: {razorPay_paymentAmount}
                            </div>
                        :
                        null
                    }
                </div>
            )
        }
    }


    render() {
        const {cartId, status, totQuantity, offersApplied, offersDesc, discAmt, totalAmount, deliveryAddress, paymentInfo} = this.props.cart;

        let statusCol = "blue";
        if(status === 'C') {
            statusCol = "green";
        } else if(status === 'I') {
            statusCol = "red";
        }

        return(
            <div style={{margin: 'auto', width: '98%', textAlign: 'center', verticalAlign: 'middle' , boxShadow: '5px 5px 5px', padding: 10, marginTop: 25}}>
                <div style={{backgroundColor: statusCol,  float: 'left', width: 20, height: 20, borderRadius: 10}}></div> 
                <div style={{width: '80%', margin: 'auto'}}>
                    <h2>#{cartId}</h2>
                </div>
                <div style={{width: '80%', margin: 'auto', textAlign: 'right', display:'inline-flex', flexDirection: 'column'}}>

                    {
                        offersApplied==="Y"?
                        (
                            <div style={{textAlign: 'end'}}>
                                <h5> Total Quantity: {totQuantity}</h5>
                                <h4> Cart Amount: Rs. <span style={{textDecoration: 'line-through'}}>
                                {totalAmount}</span></h4><h5>Rs. {totalAmount - discAmt}</h5>
                                <h6>{offersDesc}</h6>
                                <em>Total discount Amount: </em> Rs.  <span style={{fontStyle: 'italic'}}>{discAmt}</span>
                            </div>
                        )
                        :
                        <h4> Cart Amount: {totalAmount}</h4>
                    }
                </div>
                    {
                        (typeof deliveryAddress !== 'undefined' && deliveryAddress !== null)?
                            <div style={{width: '80%', margin: 'auto', textAlign: 'center', display:'inline-flex', flexDirection: 'column'}}>
                                <h4>Delivery Address</h4>
                                {this.formAdddress(deliveryAddress, 'formatted')}
                            </div>
                            :
                            null
                    }
                    {
                        (typeof paymentInfo !== 'undefined' && paymentInfo !== null) ?
                        <div style={{width: '80%', margin: 'auto', textAlign: 'center', display:'inline-flex', flexDirection: 'column'}}>
                            <h4>Payment Details</h4>
                                {this.formpaymentDetails(paymentInfo)}
                        </div>
                        :
                        null
                    }
            </div>
        )
    }
}

class ViewCart extends Component {

    state = {cartsLst: []}

    componentDidMount() {
        this.fetchCartsData();
    }

    fetchCartsData = () => {
        console.log("this.props  =>> ", this.state);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/getAllCarts?custId=" + this.props.userInfo.userId;
        console.log(url);
        const reqOptions = {
            method: "GET",
            headers: {
                 "Content-Type": "application/json",
                 "Accept": "application/json"
            }
        }
        fetch(url, reqOptions)
            .then(response => {
                console.log("Process Fetch Response =>=>");
                console.log(response.status);
                console.log(response.status === 200);
                let data = [];
                if(response.status === 200) {
                    data = response.json();
                }
                return Promise.all([response.status, data]);
            })
            .then(res => this.processFetchResponse(res))
            .catch(error => console.log(error));
    }


    processFetchResponse = (res) => {
        console.log("Process Fetch JSON =>=>");
        const status = res[0];
        console.log(status);
        console.log(status === 200);
        if(status === 200) {
            const cartsLst = res[1];
            console.log(cartsLst);
            console.log(cartsLst.length);
            this.setState({cartsLst})
        }
    }

    render() {
        const carts = this.state.cartsLst;
        if(this.props.userInfo.userId !== 'undefined' && carts.length <= 0) {
            this.fetchCartsData();
        }
        console.log("carts ", carts);
        return(
            <div style={{width: '80%', margin: 'auto'}}>
                {
                    carts.map(cart => (
                        <CartData key={cart.cartId} cart={cart} />
                    ))
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
    }
  }
  
export default connect(mapStateToProps, {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData})(ViewCart);