package studiplayer.audio;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

	private List<AudioFile> list;
	private int currentLine;

	public ControllablePlayListIterator(List<AudioFile> list) {
		this.list = new LinkedList<>(list);
		this.currentLine = 0;
	}

	public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sortCriterion) {
		this.list = new LinkedList<>();
		this.currentLine = 0;

		// Filter the list based on the search parameter if it's not null or empty
		if (search == null || search.isEmpty()) {
			this.list.addAll(list);
		} else {
			for (AudioFile file : list) {
				if (file.getAuthor() != null && file.getAuthor().contains(search)
					|| (file.getTitle() != null && file.getTitle().contains(search))
					|| (file instanceof TaggedFile && ((TaggedFile) file).getAlbum() != null
							&& ((TaggedFile) file).getAlbum().contains(search))) {
					this.list.add(file);
				}
			}
		}

		// Sort the list based on the sort parameter
		switch (sortCriterion) {
		case AUTHOR:
			Collections.sort(this.list, new AuthorComparator());
			break;
		case TITLE:
			Collections.sort(this.list, new TitleComparator());
			break;
		case ALBUM:
			Collections.sort(this.list, new AlbumComparator());
			break;
		case DURATION:
			Collections.sort(this.list, new DurationComparator());
			break;
		default:
			break; // No sorting needed for DEFAULT

		}
	}

	@Override
	public boolean hasNext() {
		return this.currentLine < list.size();

	}

	@Override
	public AudioFile next() {
		if (hasNext()) {
			return list.get(currentLine++);
		} else {
			throw new NoSuchElementException("No more elements in the playlist");
			
		}
	}

	public AudioFile jumpToAudioFile(AudioFile file) {
	    int index = list.indexOf(file); // Find the index of the AudioFile
	    if (index != -1) { // The AudioFile exists
	        currentLine = index + 1; // Update the currentIndex so that the next call to next() returns the song after the given AudioFile
	        return file;
	    } else {
	        return null; // AudioFile not found in the list
	    }
	}



}
