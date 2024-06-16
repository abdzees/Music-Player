package studiplayer.ui;

import java.io.File;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

public class Player extends Application {

    // Public constant
    public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";

    // Private constants
    private static final String PLAYLIST_DIRECTORY = "playlists";
    private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
    private static final String NO_CURRENT_SONG = "-";

    // Attributes
    private PlayList playList;
//    private String pathname = "playlists/playlist.cert.m3u";
    private boolean useCertPlayList;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button nextButton;
    private Label playListLabel;
    private Label playTimeLabel;
    private Label currentSongLabel;
    private ChoiceBox<SortCriterion> sortChoiceBox;
    private TextField searchTextField;
    private Button filterButton;
    private SongTable songTable;
    private PlayerThread playerThread;
    private TimerThread timerThread;
    private boolean paused = false;

    // Constructor
    public Player() {
        this.useCertPlayList = false;
        this.playButton = createButton("play.jpg");
        this.pauseButton = createButton("pause.jpg");
        this.stopButton = createButton("stop.jpg");
        this.nextButton = createButton("next.jpg");
        this.playListLabel = new Label(PLAYLIST_DIRECTORY);
        this.playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
        this.currentSongLabel = new Label(NO_CURRENT_SONG);
        this.sortChoiceBox = new ChoiceBox<>();
        this.searchTextField = new TextField();
        this.filterButton = new Button("display");

        // Initialize SongTable with a default playlist
        this.playList = new PlayList();
        this.songTable = new SongTable(playList);
    }

    // Methods
    public void setUseCertPlayList(boolean value) {
        this.useCertPlayList = value;
    }

    public void setPlayList(String pathname) {
        this.playList = new PlayList(pathname);
        songTable.refreshSongs(getPlayList());
    }

    public PlayList getPlayList() {
        return this.playList;
    }

    public boolean isUsingCertPlayList() {
        return this.useCertPlayList;
    }

    public void loadPlayList(String pathname) {
        if (pathname == null || pathname.isEmpty()) {
            this.playList = new PlayList(DEFAULT_PLAYLIST);
            Platform.runLater(() -> playListLabel.setText(DEFAULT_PLAYLIST));
        } else {
            this.playList = new PlayList(pathname);
            Platform.runLater(() -> playListLabel.setText(pathname));
        }
        songTable.refreshSongs(getPlayList());
    }

    public Button createButton(String iconfile) {
        Button button = new Button();
        try {
            // Load icon from the icons package
            Image icon = new Image(getClass().getResourceAsStream("/icons/" + iconfile));
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button.setGraphic(imageView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setStyle("-fx-background-color: transparent;"); // Use transparent background
        } catch (Exception e) {
            System.out.println("Image 'icons/" + iconfile + "' not found!");
            e.printStackTrace(); // Print stack trace for debugging purposes
            // Handle exception gracefully instead of exiting the application
        }
        return button;
    }

    private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState,
                                 boolean nextButtonState) {
        playButton.setDisable(playButtonState);
        pauseButton.setDisable(pauseButtonState);
        nextButton.setDisable(nextButtonState);
        stopButton.setDisable(stopButtonState);
    }

    private void updateSongInfo(AudioFile selectedSong) {
        Platform.runLater(() -> {
            if (selectedSong == null) {
                // set currentSongLabel and playTimeLabel
                currentSongLabel.setText(NO_CURRENT_SONG);
                playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
            } else {
                // set currentSongLabel and playTimeLabel
                currentSongLabel.setText(selectedSong.getTitle());
                playTimeLabel.setText(selectedSong.formatPosition());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        if (isUsingCertPlayList()) {
            setPlayList(Player.DEFAULT_PLAYLIST);
            playListLabel.setText(Player.DEFAULT_PLAYLIST);
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Playlist File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("M3U Files", "*.m3u"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                loadPlayList(selectedFile.getPath());
                setPlayList(selectedFile.getPath());
                playListLabel.setText(selectedFile.getPath());
            } else {
                // Handle the case where no file was selected
                loadPlayList(Player.DEFAULT_PLAYLIST);
                setPlayList(Player.DEFAULT_PLAYLIST);
                playListLabel.setText(Player.DEFAULT_PLAYLIST);
            }
        }

        // Initialize SongTable
        songTable = new SongTable(getPlayList());

        songTable.setRowSelectionHandler(e -> {
            stopCurrentSong();
            playList.jumpToAudioFile(songTable.getSelectionModel().getSelectedItem().getAudioFile());
            songTable.selectSong(songTable.getSelectionModel().getSelectedItem().getAudioFile());
        });

        // UI Elements
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane, 600, 400);

        // Create GridPane for search and sort controls
        GridPane searchSortGrid = new GridPane();
        searchSortGrid.setHgap(5);
        searchSortGrid.setVgap(5);
        searchSortGrid.setPadding(new Insets(10));

        // Search controls
        Label searchLabel = new Label("Search text:");
        searchTextField.setPrefWidth(200); // Adjust width as needed
        searchSortGrid.add(searchLabel, 0, 0);
        searchSortGrid.add(searchTextField, 1, 0);

        // Sort controls
        Label sortLabel = new Label("Sort by:");
        sortChoiceBox.getItems().addAll(SortCriterion.values());
        sortChoiceBox.setValue(SortCriterion.DEFAULT); // Default selection
        sortChoiceBox.setPrefWidth(200);
        searchSortGrid.add(sortLabel, 0, 1);
        searchSortGrid.add(sortChoiceBox, 1, 1);
        searchSortGrid.add(filterButton, 2, 1);

        // Create TitledPane for filter and sort controls
        TitledPane filterSortTitledPane = new TitledPane("Filter", searchSortGrid);

        // Create GridPane for bottom area
        VBox bottomBox = new VBox();
        bottomBox.setPadding(new Insets(10));
        bottomBox.setSpacing(5);

        // Current Song data in gridPane
        GridPane songInfo = new GridPane();
        songInfo.setHgap(20);
        songInfo.setVgap(5);
        songInfo.add(new Label("Playlist"), 0, 1);
        songInfo.add(playListLabel, 1, 1);
        songInfo.add(new Label("Current Song"), 0, 2);
        songInfo.add(currentSongLabel, 1, 2);
        songInfo.add(new Label("Playtime"), 0, 3);
        songInfo.add(playTimeLabel, 1, 3);

        // Button Box
        HBox controlButtonsBox = new HBox(10); // HBox with 10px spacing
        controlButtonsBox.setPadding(new Insets(0, 10, 15, 10));
        controlButtonsBox.setAlignment(Pos.CENTER); // Center align buttons horizontally
        controlButtonsBox.setSpacing(10);

        // Create buttons and add them to controlButtonsBox
        controlButtonsBox.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
        setButtonStates(false, true, true, false);

        // Create VBox to hold bottomGrid and controlButtonsBox
        VBox bottomContent = new VBox(10);
        bottomContent.getChildren().addAll(songInfo, controlButtonsBox);

        // Add components to BorderPane
        mainPane.setTop(filterSortTitledPane); // Add Filter and Sort TitledPane to the top
        mainPane.setCenter(songTable); // Add SongTable to the center
        mainPane.setBottom(bottomContent); // Add bottom VBox to the bottom

        // Add filter button event handler
        filterButton.setOnAction(event -> {
            // Apply filtering and sorting
            playList.setSearch(searchTextField.getText());
            playList.setSortCriterion(sortChoiceBox.getValue());
            songTable.refreshSongs(getPlayList()); // Refresh the SongTable with updated list
        });

        // Inside your start() method, set up button event handlers
        playButton.setOnAction(e -> playCurrentSong());
        pauseButton.setOnAction(e -> pauseCurrentSong());
        stopButton.setOnAction(e -> stopCurrentSong());
        nextButton.setOnAction(e -> {
            playNextSong();
            songTable.selectSong(playList.currentAudioFile());
        });

        stage.setTitle("APA Player");
        stage.setScene(scene);
        stage.show();
    }

    // Define local methods for button actions
    private void playCurrentSong() {
        // Update UI and start playing current song
        if (paused) {
            paused = false;
            threadStart(true);
        } else {
            threadStart(false);
            paused = false;
        }
        songTable.selectSong(playList.currentAudioFile());
        setButtonStates(true, false, false, false);
    }

    private void pauseCurrentSong() {
        // Update UI and pause current song
        if (paused) {
            paused = false;
            threadStart(true);
        } else {
            paused = true;
            threadTerminate(true);
        }
        playList.currentAudioFile().togglePause();
        setButtonStates(true, false, false, false);
    }

    private void stopCurrentSong() {
        // Update UI and stop current song
        threadTerminate(false);
        playList.currentAudioFile().stop();
        setButtonStates(false, true, true, false);
        updateSongInfo(null);
    }

    private void playNextSong() {
        stopCurrentSong();
        playList.nextSong();
        setButtonStates(true, false, false, false);
        updateSongInfo(playList.currentAudioFile());
        playCurrentSong();
    }

    // Threads
    private class PlayerThread extends Thread {
        private boolean stopped = false;

        public void terminate() {
            stopped = true;
        }

        @Override
        public void run() {
            while (!stopped) {
                AudioFile song = playList.currentAudioFile();
                if (song != null) {
                    try {
                        playList.currentAudioFile().play();
                        if (!stopped) {
                            playList.nextSong();
                            updateSongInfo(playList.currentAudioFile());
                        }
                    } catch (NotPlayableException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class TimerThread extends Thread {
        private boolean stopped = false;

        public void terminate() {
            stopped = true;
        }

        @Override
        public void run() {
            while (!stopped) {
                AudioFile currentSong = playList.currentAudioFile();
                if (currentSong != null) {
                    updateSongInfo(playList.currentAudioFile());
                }
                try {
                    // Update playback time every second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Thread Methods
    private void threadStart(boolean Timer) {
        if (timerThread == null || !timerThread.isAlive()) {
            timerThread = new TimerThread();
            timerThread.start();
        }

        if (!Timer) {
            if (playerThread == null || !playerThread.isAlive()) {
                playerThread = new PlayerThread();
                playerThread.start();
            }
        }
    }

    private void threadTerminate(boolean Timer) {
        if (!Timer) {
            if (playerThread != null) {
                playerThread.terminate();
                playerThread = null;
            }
        }
        if (timerThread != null) {
            timerThread.terminate();
            timerThread = null;
        }
    }
}

