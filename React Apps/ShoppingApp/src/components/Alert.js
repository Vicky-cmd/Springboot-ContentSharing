import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class AlertMsg extends Component {

    state = {render: true}

    componentDidUpdate() {
        this.state.render = this.props.render;
    }

    removeMessage = () => {
        
        this.setState({render:!this.state.render});
    }

    render() {
        const message = this.props.message;
        console.log("RENDERING Component <=>==<=> ");
        return (
            <div>
                {
                    this.state.render?
                    (
                        <div className="alert alert-success alert-dismissible fade show" style={{margin: 10}}>
                            <button type="button" className="close" onClick={this.removeMessage} aria-label="Close">
                                <span aria-hidden="true" style={{fontSize:"20px"}}>×</span>
                            </button>
                            {message}
                        </div>
                    )
                    :
                    null

                }
            </div>
        )
    }

}

class PaymentMsg extends Component {

    state = {render: true}

    componentDidUpdate() {
        this.state.render = this.props.render;
    }

    removeMessage = () => {
        
        this.setState({render:!this.state.render});
    }

    reloadPage = () => {
        window.location.reload(false)
    }

    render() {
        const { message, redirectUrl } = this.props.message;
        console.log("RENDERING Component <=>==<=> ");
        return (
            <div>
                {
                    this.state.render?
                    (
                        <div className="alert alert-success alert-dismissible fade show" style={{margin: 10}}>
                            <button type="button" className="close" onClick={this.removeMessage} aria-label="Close">
                                <span aria-hidden="true" style={{fontSize:"20px"}}>×</span>
                            </button>
                            <button className="btn btn-link" onClick={this.reloadPage}><a href={redirectUrl} style={{textDecoration: 'none'}}>Show Cart Details</a></button>
                            {message}
                        </div>
                    )
                    :
                    null

                }
            </div>
        )
    }

}

export default class CustomAlert {


    success = (message) => {

        ReactDOM.render(
            <AlertMsg message={message} render={true} />,
            document.getElementById('alerts')
        )
    }

    paymentMsg = (message, redirectUrl) => {

        ReactDOM.render(
            <PaymentMsg message={message} redirectUrl={redirectUrl} render={true} />,
            document.getElementById('alerts')
        )

    }
}