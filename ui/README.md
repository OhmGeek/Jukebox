# Jukebox :: UI
The React-based user interface for the picker.

## How does this work
This provides a React UI for users to view the queue, select songs, and control the overall system.

Technologies:
- Socket.io and JSON is used for communication between client and server
- React.js is used for application management
- We use W3.css for generating UI components, as this is a simple but clean UI framework.

## A note about packaging
We use Maven to trigger the build. 

1. npm is used for javascript dependency management
2. webpack is used for building the UI (JS and HTML)
3. We then package this as a JAR, so we can integrate this into the wider application.

The goal here is to make the integration with the wider server as smooth as possible. 