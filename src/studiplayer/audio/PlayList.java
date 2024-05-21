package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PlayList implements Iterable<AudioFile> {

	private List<AudioFile> list;
	private String search;
	private SortCriterion sortCriterion;
	private ControllablePlayListIterator iterator; // Maintain a reference to the iterator
	private AudioFile currentAudioFile;

	public PlayList() {
		list = new LinkedList<>(); // Initialize the LinkedList
		this.search = "";
		this.sortCriterion = SortCriterion.DEFAULT;
		this.iterator = new ControllablePlayListIterator(list, search, sortCriterion);
		this.currentAudioFile = null; // Initialize current audio file as null
	}

	public PlayList(String m3uPathname) {
		this();
		try {
			loadFromM3U(m3uPathname);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load playlist from " + m3uPathname, e);
		}
	}

	// Getters and Setters
	public List<AudioFile> getList() {
		return list; // Return the LinkedList<AudioFile> object
	}

	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(SortCriterion sort) {
		sortCriterion = sort;
		resetIterator();
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String s) {
		search = s;
		resetIterator();
	}

	// Add an AudioFile
	public void add(AudioFile file) {
		getList().add(file);
		resetIterator();
	}

	// Remove an AudioFile
	public void remove(AudioFile file) {
		getList().remove(file);
		resetIterator();
	}

	// Return size of the PlayList
	public int size() {
		return list.size();
	}

	@Override
	public Iterator<AudioFile> iterator() {
		return iterator;
	}

	// Method to get the current audio file being played
	public AudioFile currentAudioFile() {
		return currentAudioFile;
	}

	// Method to get the next song in the playlist
	public void nextSong() {
		if (!iterator.hasNext()) { // If there is no next song
			iterator = new ControllablePlayListIterator(list, search, sortCriterion); // Reset the iterator to the
																						// beginning
		}
		if (currentAudioFile == null) { // If current audio file is not set
			if (iterator.hasNext()) { // Check if there is a next song
				currentAudioFile = iterator.next(); // Get the first song from the iterator
			}
		} else {
			if (iterator.hasNext()) { // Check if there is a next song
				currentAudioFile = iterator.next(); // Get the next song from the iterator
			}
		}
	}

	public AudioFile jumpToAudioFile(AudioFile file) {
		currentAudioFile = file; // Update the currentAudioFile variable
		return iterator.jumpToAudioFile(file);
	}

	public void saveAsM3U(String pathname) {
		try (FileWriter writer = new FileWriter(pathname)) {
			for (AudioFile file : list) {
				writer.write(file.getPathname() + System.lineSeparator());
			}
			System.out.println("File " + pathname + " written!");
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
		}
	}

	public void loadFromM3U(String path) throws NotPlayableException {
		list.clear();

		try (Scanner scanner = new Scanner(new File(path))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.isEmpty() && !line.startsWith("#")) {
					try {
						AudioFile audiofile = AudioFileFactory.createAudioFile(line);
						list.add(audiofile);
					} catch (NotPlayableException e) {
						System.err.println("Not playable: " + line);
					}
				}
			}
			System.out.println("File " + path + " read!");
		} catch (IOException e) {
			throw new NotPlayableException("Unable to read file " + path + "!", path);
		}
	}

	// Method to set the search criteria and reset the iterator
	private void resetIterator() {
	    // Create a new ControllablePlayListIterator using the existing constructor
	    iterator = new ControllablePlayListIterator(list, search, sortCriterion);
	    if (iterator.hasNext()) {
	        currentAudioFile = iterator.next();
	    } else {
	        currentAudioFile = null;
	    }
	}

	@Override
	public String toString() {
		return list.toString();
	}

	/**
	 * Just testing out some code below
	 */
//	public static void main(String[] args) throws NotPlayableException {
//	PlayList pl = new PlayList();
//	pl.add(new TaggedFile("audiofiles/Rock 812.mp3"));
//	pl.add(new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"));
//	pl.add(new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
//	// set different values for search and sortCriterion below...
//	pl.setSearch("");
//	pl.setSortCriterion(SortCriterion.TITLE);
//	// example iteration with for-each
//	for(AudioFile file: pl) {
//	 System.out.println(file);
//	}
//		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
//		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
//		AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
//		PlayList list = new PlayList();
//		list.add(tf1);
//		list.add(tf2);
//		list.add(tf3);
//		list.jumpToAudioFile(tf2);
//		System.out.println(list.currentAudioFile());
//		list.nextSong();
//		System.out.println(list.currentAudioFile());
//		list.nextSong();
//		System.out.println(list.currentAudioFile());
//	}

}
