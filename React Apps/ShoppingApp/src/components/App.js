import {connect} from 'react-redux';
import { Link } from 'react-router-dom';
import {saveuserInfo} from '../actions';

function App() {
  return (
    <div style={{width: '80%', margin: 'auto'}}>
      <h2>Hi!</h2>
      <h3>Welcome to InfoTrends.in</h3>
      <p>This is a shopping site that was created using React js and Springboot for backend support.</p>
      <p>The various Links Available for this site are:</p>
      <ul>
        <li><Link to="/retailApp/">Home</Link></li>
        <li><Link to="/retailApp/items">View Items</Link></li>
        <li><Link to="/retailApp/cart">Cart</Link></li>
        <li><Link to="/retailApp/viewCart">View Carts</Link></li>
      </ul>
    </div>
  );
}



function mapStateToProps(state) {
  return {
    state: state
  }
}
// export default connect(mapStateToProps, mapDispatchToProps)(App);

export default connect(mapStateToProps, {saveuserInfo})(App);
