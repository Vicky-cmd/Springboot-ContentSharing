import React, { Component } from 'react';
import {connect} from 'react-redux';
import {saveuserInfo} from '../actions';

class SignIn extends Component {


    render() {
        return(
            <div>
                
                <h1>Login Screen</h1>

                <div class="container .bg-transparent justify-content-center">
                    <div class="row">
                        <div class="col-sm"></div>
                        <div className="col-sm login" style={{padding: "10px"}}>
                            <div id="loginForm">
                            <div className="container">
                                    <div className="row">
                                        <div className="col" align="left" style={{padding: "10px"}}>Sign In</div>
                                    </div>
                                </div>
                                <div style={{margin: "10px"}}>
                                    <form action="/submit" method="post" className="form">
                                        <input path="fullname" type="hidden" name="fullname" id="fullname" value="" />
                                        <div className="form-group">
                                            <label className="font-weight-bold">Username</label><br/>
                                            <div className="input-group">
                                                <div className="input-group-prepend">
                                                    <span className="input-group-text" id="username-text-login">@</span>
                                                </div>
                                                <input type="email" id="username" name="username" className="form-control" required="required" placeholder="Email ID" />
                                            </div>
                                        </div>
                                        <div className="form-group">
                                            <label className="font-weight-bold">Password</label><br/>
                                            <div className="input-group">
                                                <input type="password" id="password" name="password" className="form-control" required="required" placeholder="Password" />
                                                <div className="input-group-append">
                                                        <span className="input-group-text " id="pwd-text-login"> <span id="pwd-log-toggle"/*  onClick='changepwd("password", "pwd-log-toggle")' */ className="fa fa-fw fa-eye field-icon toggle-password"></span></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="form-group text-center">
                                            <button type="submit" className="btn btn-success">Submit</button>
                                            <button type="reset" className="btn ">Cancel</button>
                                        </div>
                                        <div className="form-group text-center">
                                            <p>Don't have an Account? <a href="${showSubmitPageUrl}">Sign Up</a>.</p>
                                        </div>
                                        <div className="form-group text-center">
                                            <p><a href="/authenticate/forgotPassword">Forgot Password?</a></p>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm"></div>
                    </div>
                </div>
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
      reminders: state
    }
  }
  // export default connect(mapStateToProps, mapDispatchToProps)(App);
  
  export default connect(mapStateToProps, {saveuserInfo})(SignIn);