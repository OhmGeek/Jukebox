import React from "react";
import Plyr from "react-plyr";
import "plyr/dist/plyr.css";
import "./VideoPlayer.css"
// ======== HANDLERS FOR VIDEO PLAYER ==================

// Set the player to play, on a given event.
let autoplayEvent = (player) => {
    player.play();
}

// Render the video player object if a song is present. Error handling case.
let getVideoPlayer = (song, onEndOfSong) => {
    if(typeof(song.info) !== 'undefined') {
        return <Plyr autoplay provider={song.backend} videoId={song.info.id} onEnd={onEndOfSong} onReady={autoplayEvent}/>
    } else {
        return <div></div>
    }
}

class VideoPlayer extends React.Component {

    render() {
        console.log(this.props.song);
        let songName = "";
        if(this.props.song && this.props.song.info && this.props.song.info.name) {
            songName = this.props.song.info.name;
        }


        console.log(this.props.song);
        return (
            <div>
                <div className="w3-container w3-black w3-center video-player">
                    {getVideoPlayer(this.props.song, this.props.nextSongHandler)}
                </div>
                <div className="w3-container">
                    <h2>Currently playing: {songName}</h2>
                </div>
            </div>
        )
    }
}

export default VideoPlayer;