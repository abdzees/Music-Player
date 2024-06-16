# Music Player Application

## Overview
This JavaFX-based music player allows you to create and manage playlists, search and sort songs, and navigate through them using a custom iterator. It also features a graphical user interface (GUI) for easy interaction.

## Features
- **Play/Pause/Stop:** Basic controls to manage playback.
- **Next Song:** Skip to the next song in the playlist.
- **Sort by Criteria:** Sort songs by title, artist, or other metadata.
- **Search:** Filter songs based on keywords.
- **Load and Save Playlists:** Easily manage your playlists with M3U files.
- **Graphical User Interface:** User-friendly GUI for easy interaction.

## How to Use

1. **Create a Playlist**: Initialize a playlist and add your favorite audio files.
     - Create an M3U playlist file and add paths to your audio files line by line.
3. **Search and Sort**: Utilize the search feature to filter songs and sort them based on your preferred criteria.
4. **Navigate**: Use the custom iterator to navigate through your playlist seamlessly.
5. **Load and Save**: Load existing playlists from M3U files or save your current playlist to an M3U file for later use.
6. **Use the GUI**: Launch the GUI to interact with the music player visually.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- JavaFX SDK

## Running the Application

- Create an M3U Playlist:
  - Create a text file with the .m3u extension.
  - Add the paths to your audio files, one per line. For example:
```
audiofiles/Rock 812.mp3
audiofiles/Motiv 5. Symphonie von Beethoven.ogg
audiofiles/Eisbach Deep Snow.ogg
```
# Launch the Application:
- Compile and run the Player.java file.
- A GUI will pop up with the audio files loaded from your playlist.

## Example Preview
<p align="center">
  <img src="https://github.com/abdzees/Music-Player/assets/109596800/d3b2b267-aa4d-4e69-866c-517b9881d405" width="600" title="The Music Player">
</p>


## Setup
1. Clone the repository:
```
git clone https://github.com/abdzees/music-player.git
```
2. Open the project in your preferred Java IDE.
3. Add your audio files to the project directory.
4. Run the PlayList class to start the application.

## Running the GUI
1. Compile and run the GUI main class (Player.java).
2. Use the interface to navigate your playlist from your device.

## License
This project is licensed under the General Public License License. See the LICENSE file for more details.
