import React from "react";

class Title extends React.Component {
    render() {
        return (
            <div className="w3-container w3-red w3-center">
                <h1>{this.props.text}</h1>
            </div>
        )
    }
}

export default Title;