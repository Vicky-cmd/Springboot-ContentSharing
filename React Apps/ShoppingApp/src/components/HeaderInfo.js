import React, { Component } from 'react';
import {connect} from 'react-redux';
import {saveuserInfo, updateCartQty, updateCartData, addAddressInfo} from '../actions';
import {Link} from 'react-router-dom';

class HeaderInfo extends Component {


    saveUserData = (json) => {
        const {fullname, userId, username, isAdmin} = json;
        console.log(this.props);
        this.props.saveuserInfo(fullname, userId, username, isAdmin);
        this.fetchCartData();
    }

    fetchCartData = () => {
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

    saveCartData = (json) => {
        
        if(typeof json.cartId !== 'undefined') {
            const {cartId, totQuantity} = json;
            this.props.updateCartData(totQuantity, cartId, json);
            this.getAddressData();
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


    componentDidMount() {
        const reqOptions = {
            method: "post",
            headers: {
                 "Content-Type": "application/json"
            }
        }
        fetch("/api/v1/session/getSessionData", reqOptions)
            .then(response => response.json())
            .then(json => this.saveUserData(json))
            .catch(error => console.log(error));
    }

    render() {
        // console.log(this.props);
        if(typeof this.props.userInfo === 'undefined') {
            return (
                <div></div>
            )
        }
        let {fullname, userId, username, isAdmin} = this.props.userInfo;
        const profileId = "/userInfo?userId=" + userId;
        
        console.log("Carts ", this.props);
        return(
            <div>
                <div className="jumbotron jumbotron-fluid login" style={{padding: "5px"}}>
                    
                    <div className="d-flex justify-content-between">
                        <div className="" align="center" style={{padding: "10px", fontFamily: "fantasy"}}>
                            <a href="/" style={{textDecoration: "none", color: "white"}}><h1>InfoTrends.in</h1></a>
                        </div>
                        <div className="" align="center" style={{padding: "10px", right: 0, position: 'absolute'}}>
                            <a href="/authenticate/logout" className="linkbutton">LOGOUT</a>
                        </div>
                    </div>

                </div>

                <nav className="navbar navbar-expand-md navbar-transparent navbar-topcss">
                    <a href="/" className="navbar-brand"><img src="/assets/images/logo.jpg" style={{height: "40px", width: "40px"}} /></a>
                    <button type="button" className="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
                        <span className="navbar-toggler-icon"><i style={{fontSize:"24px"}} className="fa">&#xf0c9;</i></span>
                    </button>

                    <div className="collapse navbar-collapse justify-content-between" id="navbarCollapse">
                        <div className="navbar-nav">
                            <a href="/" className="nav-item nav-link active">Home</a>
                            <a href={profileId} className="nav-item nav-link">Profile</a>
                            <div className="nav-item dropdown">
                                <a href="#" className="nav-link dropdown-toggle" data-toggle="dropdown">Articles</a>
                                <div className="dropdown-menu">
                                    <a href="/membersdashboard/createNewArticle" className="dropdown-item">Create Article</a>
                                    <a href="#" className="dropdown-item">View Articles</a>
                                </div>
                            </div>
                                {
                                    (typeof isAdmin !== 'undefined' && isAdmin === 'Y')?
                                    (
                                        <div style={{width:"100%"}}>
                                            <a href="/membersdashboard" className="nav-item nav-link" style={{display: 'inline-block'}}>Members Home</a>
                                            <div className="nav-item dropdown" style={{display: 'inline-block'}}>
                                                <a href="#" className="nav-link dropdown-toggle" data-toggle="dropdown">Manage Users</a>
                                                <div className="dropdown-menu">
                                                    <a href="/adminsdashboard/addNewUser" className="dropdown-item">Add New User</a>
                                                    <a href="/adminsdashboard/displayUsers" className="dropdown-item">Display Users</a>
                                                </div>
                                            </div>
                                        </div>
                                    ):null
                                }
                            <Link to="/retailApp" style={{whiteSpace: "nowrap"}} className="nav-item nav-link">
                                retailApp Home
                            </Link> 
                            <Link to="/retailApp/items" style={{whiteSpace: "nowrap"}} className="nav-item nav-link">
                                Products
                            </Link>
                            <Link to="/retailApp/viewCart" style={{whiteSpace: "nowrap"}} className="nav-item nav-link">
                                Carts History
                            </Link>  
                            <Link to="/retailApp/cart" style={{whiteSpace: "nowrap"}} className="nav-item nav-link">
                                <i className="fa fa-shopping-cart"></i>&nbsp;&nbsp;
                                {
                                    this.props.cartInfo.qty?
                                    this.props.cartInfo.qty:0
                                } 
                            </Link> 
                        </div>
                        
                    </div>
                </nav>	

                <form className="form-inline" style={{right: 0, padding: 20, display: 'flex', justifyContent: 'flex-end' }} action="/search?pgNo=1">
                    <div className="input-group">                    
                        <input type="text" className="form-control search-bar" name="query" placeholder="Search" />
                        <div className="input-group-append">
                            <button type="submit" className="btn btn-warning"><i className="fa fa-search"></i></button>
                        </div>
                    </div>
                </form>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        userInfo: state.user,
        cartInfo: state.cart,
        addressInfo: state.address
    }
  }
  
export default connect(mapStateToProps, {saveuserInfo, updateCartQty, updateCartData, addAddressInfo})(HeaderInfo);