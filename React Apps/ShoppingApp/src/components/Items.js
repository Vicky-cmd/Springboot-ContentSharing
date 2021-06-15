import React, { Component } from 'react';
import {connect} from 'react-redux';
import {saveItemData, updateCartQty} from '../actions';
import notAvailable from '../images/not-available.png';
import CustomAlert from './Alert'

class Item extends Component {

    state={totPrice: this.props.itemData.price}
    addToCart = (pid) => () => {
        let qty = this.refs.itemQty.value;
        console.log("[Item Id - ", pid, ", Qty - ", qty, "]");
        let req = {};
        req.pId = pid;
        req.quantity = qty;
        console.log(req);
        const url = "https://infotrends-carts.herokuapp.com/api/v1/cart/addItemToCart/" + this.props.userInfo.userId;
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

    updateCartQty = (json) => {
        console.log(typeof json.statusCode !== 'undefined');
        console.log(json.statusCode === 0);
        if(typeof json.statusCode !== 'undefined' && json.statusCode === 0) {
            console.log(this.props);
            let alertMsg = new CustomAlert();
            alertMsg.success("Successfully Added the Items to Cart - Qty: " + json.cartQuantity)
            this.props.updateCartQty(json.cartQuantity, json.cartId);
        } 
    }

    onQtyChange = () => {
        console.log("Qty Change");
        let qty = this.refs.itemQty.value;
        let unitPrice = this.refs.unitPrice.textContent;
        let totPrice = this.refs.totPrice;
        console.log(this.refs.unitPrice.textContent)
        console.log(this.refs.itemQty.value)
        this.setState({totPrice:(unitPrice * qty)})
    }

    render() {
        const { id, name, desc, category, subcategory, brand, price, pid, imgUrl, quantity } = this.props.itemData;
        let qtyArr = [];
        for(let i=1; i<=quantity; i++) {
            qtyArr.push(i);
        }
        console.log("Item ",name);
        return(
            <div className="productItem" style={{}}>

                <div className="productItemImage" style={{}}>
                    {
                        (typeof imgUrl === 'undefined' || imgUrl === null)?
                        <img className="alignImageForScreen" style={{}} src={notAvailable} />
                        :
                        <img className="alignImageForScreen" style={{}} src={imgUrl} />
                    }
                </div>
                <div className="displayProductDesc" style={{padding: 10}}>
                    <h2>{name}</h2>
                    <h4>{desc}</h4>
                    <h3 style={{fontVariant: 'all-petite-caps', textTransform: 'capitalize', fontWeight: 'bolder', fontSize: 'xx-large'}}>{brand}</h3>
                    <h5>{category} &gt; {subcategory}</h5>
                </div>
                <div className="itemPricingData" style={{}}>
                    <h6><em>Per unit Price: </em><span ref="unitPrice">{price}</span></h6>
                    <h3>Rs. {this.state.totPrice}</h3>
                    <div className="form-group">
                        <select ref="itemQty" className="form-control" onChange={this.onQtyChange} >
                            {
                                qtyArr.map(qty => (
                                    <option key={qty} value={qty}>{qty}</option>
                                ))
                            }
                        </select>
                    </div>
                    <button className="btn btn-danger" onClick={this.addToCart(pid)}>Buy Now</button>
                </div>
            </div>
        )
    }
}


class Items extends Component {

    componentDidMount() {
        const reqOptions = {
            method: "get",
            headers: {
                 "Content-Type": "application/json",
                 "Accept": "application/json"
            }
        }
        fetch("https://infotrends-products.herokuapp.com/api/v1/products/findAllData", reqOptions)
            .then(response => response.json())
            .then(json => this.saveItemData(json))
            .catch(error => console.log(error));
    }

    saveItemData = (json) => {
        this.props.saveItemData(json);
    }

    render() {
        let itemInfo = [];
        if(typeof this.props.itemInfo.itemsLst !== 'undefined') {
            itemInfo = this.props.itemInfo.itemsLst;
        }
        console.log("itemInfo ", this.props);

        return(
            <div style={{padding: 10, marginTop: 20}}>
                {
                    itemInfo.map(item => (
                        <Item key={item.id} itemData={item} userInfo={this.props.userInfo} updateCartQty={this.props.updateCartQty}/>
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
    }
  }
  
export default connect(mapStateToProps, {saveItemData, updateCartQty})(Items);