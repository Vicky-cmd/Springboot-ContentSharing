import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './components/App';
// import reportWebVitals from './reportWebVitals';
import {Switch, Route, BrowserRouter} from "react-router-dom";
import {Provider} from 'react-redux';
import {createStore} from 'redux';
import { createBrowserHistory } from 'history';
import reducer from './reducers';
import SignIn from './components/signin';
import Header from './components/Header';
import Items from './components/Items';
import Cart from './components/Cart';
import ViewCart from './components/ViewCart';

const store = createStore(reducer);

const history = createBrowserHistory();

console.log("Rendering!");

ReactDOM.render(
  <Provider store={store}>
  <BrowserRouter history={history}>
    <Switch>
      <Route exact path="/retailApp/" render={() => <Header><App/></Header>} />
      <Route path="/retailApp/signIn" render={() => <Header><SignIn/></Header>} />
      <Route path="/retailApp/Items" render={() => <Header><Items/></Header>} />
      <Route path="/retailApp/cart" render={() => <Header><Cart/></Header>} />
      <Route path="/retailApp/viewCart" render={() => <Header><ViewCart/></Header>} />
    </Switch>
  </BrowserRouter></Provider>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint-. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals();
