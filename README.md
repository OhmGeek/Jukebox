# Jukebox
This is an evolution of the UraDJ system, fixing notable design issues and extending it for modern APIs

## Problems with UraDJ:
### Concurrency issues:
Users could bypass validation due to issues with concurrency (e.g. adding a song that's already in the queue).

### Separation of Concerns:
The old system was very tightly coupled to the YouTube backend. The goal here is to make this slightly more platform invariant (allowing the user to configure which backend to use, and allow overriding of the backend as required).

Backends:
- Spotify
- YouTube
- Custom

### More configurable parameters:
UraDJ had lots of hard coded values. Here, we want to be able to configure many different parameters of the app, including:
- Time limit on songs
- Which backend to use (as above)
- Default song queue
