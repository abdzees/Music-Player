# Music Player Project

## Overview
This Music Player project is a Java-based application designed to manage and play audio files. It includes robust playlist management features, allowing users to create, modify, and navigate through playlists efficiently. The player supports various audio formats and integrates search and sorting functionalities for an enhanced user experience. Additionally, the application comes with a graphical user interface (GUI), making it easy for users to interact with the music player.

## Features
- **Playlist Management**: Add, remove, and manage audio files in your playlist.
- **Search Functionality**: Filter songs by author, title, or album with a customizable search feature.
- **Sorting Options**: Sort your playlist by author, title, album, or duration using different criteria.
- **M3U Support**: Load and save playlists in the M3U format for compatibility with other media players.
- **Custom Iterator**: Navigate through your playlist with a custom iterator that supports controlled navigation.
- **Graphical User Interface**: User-friendly GUI to easily interact with the music player.

## How to Use
1. **Create a Playlist**: Initialize a playlist and add your favorite audio files.
2. **Search and Sort**: Utilize the search feature to filter songs and sort them based on your preferred criteria.
3. **Navigate**: Use the custom iterator to navigate through your playlist seamlessly.
4. **Load and Save**: Load existing playlists from M3U files or save your current playlist to an M3U file for later use.
5. **Use the GUI**: Launch the GUI to interact with the music player visually.

## Example
```java
AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

PlayList list = new PlayList();
list.add(tf1);
list.add(tf2);
list.add(tf3);

list.setSearch("Beethoven");
list.setSortCriterion(SortCriterion.TITLE);

for (AudioFile file : list) {
    System.out.println(file);
}
```

## Requirements
Java 8 or higher

## Setup
1. Clone the repository:
```
git clone https://github.com/abdzees/music-player.git
```
2. Open the project in your preferred Java IDE.
3. Add your audio files to the project directory.
4. Run the PlayList class to start the application.

## Running the GUI
1. Compile and run the GUI main class (e.g., MusicPlayerGUI.java).
2. Use the interface to add, remove, search, sort, and play audio files from your playlist.

## License
This project is licensed under the General Public License License. See the LICENSE file for more details.
