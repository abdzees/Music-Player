package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PlayList implements Iterable<AudioFile> {

	private LinkedList<AudioFile> list;
	private String search;
	private SortCriterion sortCriterion;
	private ControllablePlayListIterator iterator;
	private int currentIndex;

	public PlayList() {
		list = new LinkedList<AudioFile>();
		sortCriterion = SortCriterion.DEFAULT;
		iterator = this.iterator();
		currentIndex = 0;
	}

	public PlayList(String m3uPathname) {
		this();
		try {
			loadFromM3U(m3uPathname);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load playlist from " + m3uPathname, e);
		}
	}

	public AudioFile currentAudioFile() {

		if (this.getList().size() == 0 || currentIndex > iterator().getList().size())
			return null;
		else {
			if (currentIndex == iterator().getList().size())
				return iterator().getList().get(0);
			else
				return iterator().getList().get(currentIndex);
		}
	}

	public void nextSong() {

		if (list.isEmpty()) {
			return;
		}
		currentIndex = (currentIndex + 1) % list.size();
	}

	public void saveAsM3U(String pathname) {
		try (FileWriter writer = new FileWriter(pathname)) {
			for (AudioFile file : list) {
				writer.write(file.getPathname() + System.lineSeparator());
			}
			System.out.println("File " + pathname + " written!");
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!", e);
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
		iterator = new ControllablePlayListIterator(list, search, sortCriterion);
	}

	public void jumpToAudioFile(AudioFile audiofile) {
		AudioFile afile = iterator().jumpToAudioFile(audiofile);
		currentIndex = list.indexOf(afile);
		iterator.next();
	}

	@Override
	public ControllablePlayListIterator iterator() {
		iterator = new ControllablePlayListIterator(getList(), getSearch(), getSortCriterion());
		return iterator;
	}

	public List<AudioFile> getList() {
		return list;
	}

	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(SortCriterion sort) {
		sortCriterion = sort;
		iterator();
		currentIndex = 0;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
		iterator();
		currentIndex = 0;
	}

	public void add(AudioFile file) {
		list.add(file);
	}

	public void remove(AudioFile file) {
		list.remove(file);
	}

	public int size() {
		return list.size();
	}

	@Override
	public String toString() {
		return list.toString();
	}
}
