import React from "react";

function Title(props) {
    let style;
    if(props.type==="1"){
        style = {
            fontSize: '18px',
            textAlign: 'left',
            marginTop: '4rem',
            fontWeight: 'bold'
        }
    }else if(props.type==="2"){
        style = {
            fontSize: '14px',
            textAlign: 'left',
            marginTop: '3rem',
            fontWeight: 'bold',
            color: '#6CBACB'
        }
    }
    return (
         <p style={style}>{props.text}</p>
    );
}
export default Title;