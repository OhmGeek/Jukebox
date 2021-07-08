import React from "react";
import io from "socket.io-client";
class QueueList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            queue: []
        }
    }
    componentDidMount() {
        let socket = io();

        // Update queue handler
        socket.on('queue-updated', (data) => {
            this.setState({queue: data});
        });

        // Call the get queue thing.
        socket.emit('get-queue');
    }

    render() {
        let shortQueue = this.state.queue;

        if(this.props.maxItemsToDisplay && shortQueue.length > this.props.maxItemsToDisplay) {
            shortQueue.slice(0, this.props.maxItemsToDisplay); // Only display the max number of items allowed.
        }
        const imgStyle = {
            width: "85px"
        }
        console.log(shortQueue)
        return (
            <div class="w3-container">
                <h2>Up next:</h2>
                <ul class="w3-ul w3-border">
                    {shortQueue.map((elem) => {
                        return (
                            <li className="w3-bar">
                                <img src={elem.info.thumbnail_img} className="w3-bar-item w3-circle" style={imgStyle} />
                                <div className="w3-bar-item">
                                    <span className="w3-large">{elem.info.name}</span>
                                </div>
                            </li>
                        )
                    })}
                </ul>
            </div>
        )
    }

}

export default QueueList;
