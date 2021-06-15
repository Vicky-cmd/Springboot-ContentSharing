import React from 'react';
import {Link} from 'react-router-dom';
import HeaderInfo from './HeaderInfo'

const Headers = ({children}) => {
    const style = {
        display: 'inline-block',
        margin: 10,
        'textDecoration': 'none'
    } 
    console.log("<=<=<=<=<=<=<=Inside Headers Render!=>=>=>=>=>=>=>");
    return (
        <div>
            
            <div style={{width: "100%"}}>
                <HeaderInfo />
            </div>
            {children}
        </div>
    )
}

export default Headers;