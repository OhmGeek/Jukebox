import React from "react";
import Title from "./Title.jsx";
import w3css from "w3-css";
import io from "socket.io-client";
import VideoPlayer from "./VideoPlayer.jsx";
import QueueList from "./QueueList.jsx";

class App extends React.Component {
    constructor(props) {
        super(props);
        this.nextSongHandler = this.nextSongHandler.bind(this);
        this.state = {
            currentSong: {},
            nextSongs: [],
        }
    }

    componentDidMount() {
        this.socket = io();

        this.socket.on("queue-updated", (songs) => {
            console.log("Queue Updated");
            this.setState({nextSongs: songs});
        });

        this.socket.on("play-next-song", (song) => {
            console.log("Next song to be played");
            console.log("song");
            this.setState({currentSong: song})
        })

        // Get the ball rolling.
        this.socket.emit('get-next-song');
    }
    nextSongHandler() {
        // Emit event to change song.
        this.socket.emit('get-next-song');
    }
    render() {
        return (
            <div>
                <Title text={this.props.appName || "UraDJ Jukebox Player"} />
                <VideoPlayer song={this.state.currentSong} nextSongHandler={this.nextSongHandler} />
                <QueueList queue={this.state.nextSongs} />
            </div>
        )
    }


}

export default App;