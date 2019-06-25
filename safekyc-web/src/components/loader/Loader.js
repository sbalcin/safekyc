import React, {Component} from 'react';
import './Loader.css';

class Loader extends Component {

  render() {
    return(
      <div className="load-bar">
        <div className="bar"></div>
        <div className="bar"></div>
        <div className="bar"></div>
      </div>
    );
  }
}

export default Loader;
