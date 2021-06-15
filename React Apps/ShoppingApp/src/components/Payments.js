import React, { Component } from 'react';
import {connect} from 'react-redux';
import { Link } from 'react-router-dom';
import {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData} from '../actions';
import notAvailable from '../images/not-available.png';
import Razorpay from 'razorpay';
import CustomAlert from './Alert'
import { useHistory } from "react-router-dom";

class Payments extends Component {

    state = {displayCod: false, displayOnline: false, displayPayments: false}

    createOrder = () => {
        const cod = this.refs.codPaymentType.checked;
        const online = this.refs.onlinePaymentType.checked;

        if(cod) {
            const markForCodUrl = "https://infotrends-carts.herokuapp.com/api/v1/cart/markActiveCartForCoD?custId=" + this.props.userInfo.userId;
            console.log(markForCodUrl);
            const reqOptions = {
                method: "PUT",
                headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                }
            }
            fetch(markForCodUrl, reqOptions)
                .then(response => response.json())
                .then(json => {
                    if(typeof json.statusCode !== 'undefined' && json.statusCode === 200) {
                        console.log(this.state);
                        
                        let alertMsg = new CustomAlert();
                        alertMsg.paymentMsg("Items booked with Cash on Delivery.")
                    } else {
                        if(typeof json.errorMsg !== 'undefined') {
                            alert(json.errorMsg);
                        } else {
                            alert("Oops! Something Happened!");
                        }
                    }
                })
                .catch(error => console.log(error));
        } else if(online) {
            const createOrderUrl = "https://infotrends-payments.herokuapp.com/api/v1/payment/createOrder";
            let req = {};
            const {cartId, totalAmount, discAmt, offersApplied, userId} = this.props.cartInfo.cartData;
            req.amount = (offersApplied === 'Y')?(totalAmount - discAmt): totalAmount;
            req.userId = userId;
            req.cartId = cartId;
            console.log(req);
            console.log(createOrderUrl);
            const reqOptions = {
                method: "POST",
                headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                },
                body: JSON.stringify(req)
            }
            fetch(createOrderUrl, reqOptions)
                .then(response => response.json())
                .then(json => this.completePayment(json, cartId))
                .catch(error => console.log(error));
        }
    }

    completePayment = (json, cartId) => {
        console.log("Inside Complete Payment",json);
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 201) {
            const options = {
                "key_id": "rzp_test_1OnHyQf62f3JNR", // Enter the Key ID generated from the Dashboard
                "key_secret": "iYXMef9vScvWtLemApt8sjRs",
                "amount": json.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
                "currency": "INR",
                "name": "InfoTrends Corp",
                "description": "Test Transaction",
                "image": "https://infotrends-media.herokuapp.com/assets/images/logo.jpg",
                "order_id": json.orderId, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
                handler: async function (response) {
                    const data = {
                        orderId: json.orderId,
                        razorpay_payment_id: response.razorpay_payment_id,
                        razorpay_order_id: response.razorpay_order_id,
                        razorpay_signature: response.razorpay_signature,
                    };

                    const reqOptions = {
                        method: "POST",
                        headers: {
                                "Content-Type": "application/json",
                                "Accept": "application/json"
                        },
                        body: JSON.stringify(data)
                    }
                    fetch("https://infotrends-payments.herokuapp.com/api/v1/payment/confirmPayment", reqOptions)
                        .then(response => response.json())
                        .then(json => {
                            console.log("RESPONSE RECEIVED!-----> ",json);
                            if(typeof json.statusCode !== 'undefined' && json.statusCode === 200) {
                                const redUrl = "/retailApp/viewCarts?cartId=" + cartId;
                                console.log(this.state);
                                
                                let alertMsg = new CustomAlert();
                                alertMsg.paymentMsg("Completed Payment for the Cart #" + cartId, '/retailApp/viewCart');
                            } else {
                                if(typeof json.errorMsg !== 'undefined') {
                                    alert(json.errorMsg);
                                } else {
                                    alert("Oops! Something Happened!");
                                }
                            }
                            // return(json);
                        })

                },
                // "callback_url": "http://localhost:8081/payments/redirectURL",
                // "prefill": {
                //     "name": "Gaurav Kumar",
                //     "email": "gaurav.kumar@example.com",
                //     "contact": "9999999999"
                // },
                "notes": {
                    "address": "Razorpay Corporate Office"
                },
                "theme": {
                    "color": "#3399cc"
                }
            };

            console.log(options);
            var rzp1 = new window.Razorpay(options);
                rzp1.open();
                // ele.preventDefault();

        }
    }

    displayMsg = () => {
        console.log("Display Message!");
    }

    paymentAction = () => {
        const cod = this.refs.codPaymentType.checked;
        const online = this.refs.onlinePaymentType.checked;
        console.log(cod);
        console.log(online);
        if(cod) {
            this.setState({displayCod: true, displayOnline: false})
        } else if(online) {
            this.setState({displayCod: false, displayOnline: true})
        }
    }

    render() {
        return (
            <div style={{width: "98%", borderRadius: 20, boxShadow: '5px 5px 5px', margin: 'auto', padding: 30, marginTop: 30, marginBottom: 20}}>
                    <h1 style={{width: "100%", textAlign: 'center', fontVariant: 'small-caps'}}>Payment Methods</h1>
                    <div style={{width: "98%", margin: "auto", marginTop: 20}}>
                        <div><label style={{cursor: 'pointer'}}><input type="radio" onChange={this.paymentAction} aria-label="CoD" ref="codPaymentType" name="paymentType" value="CoD" /> Cash On Delivery</label></div>
                        {
                            this.state.displayCod?
                            <div><h3 style={{width: 'fit-content', margin: 'auto', fontWeight: 'bolder', fontVariant: 'small-caps'}}>Cod</h3></div>
                            :
                            null
                        }
                        <div><label style={{cursor: 'pointer'}}><input type="radio" onChange={this.paymentAction} aria-label="online" ref="onlinePaymentType" name="paymentType" value="OnlineType" /> Online Transactions</label></div>
                        {
                            this.state.displayOnline?
                            <div style={{width: "100%", alignContent: 'center', justifyItems: 'center'}}>
                                
                            </div>
                            :
                            null
                        }
                        
                        {
                            (this.state.displayCod || this.state.displayOnline)?
                            <div style={{width: 'fit-content', margin: 'auto'}}>
                                <button onClick={this.createOrder} className="btn btn-success">Complete Payment</button>
                            </div>
                            :
                            null
                        }
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
    }
  }
  
export default connect(mapStateToProps, {saveItemData, updateCartQty, saveuserInfo, updateCartData, deleteCartData})(Payments);