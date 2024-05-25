package studiplayer.audio;

import java.util.*;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

    private List<AudioFile> list;
    private int currentLine;

    public ControllablePlayListIterator(List<AudioFile> list) {
        this.list = new LinkedList<>(list);
        currentLine = 0;
    }

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sortCriterion) {
        this.list = new LinkedList<>();
        currentLine = 0;

        // Filter the list based on the search parameter if it's not null or empty
        if (search == null || search.isEmpty()) {
            this.list.addAll(list);
        } else {
            for (AudioFile file : list) {
                if ((file.getAuthor() != null && file.getAuthor().toLowerCase().contains(search.toLowerCase()))
                        || (file.getTitle() != null && file.getTitle().toLowerCase().contains(search.toLowerCase()))
                        || (file instanceof TaggedFile && ((TaggedFile) file).getAlbum() != null
                                && ((TaggedFile) file).getAlbum().toLowerCase().contains(search.toLowerCase()))) {
                    this.list.add(file);
                }
            }
        }

        // Sort the list according to the sort criterion
        switch (sortCriterion) {
            case AUTHOR:
                this.list.sort(new AuthorComparator());
                break;
            case ALBUM:
                this.list.sort(new AlbumComparator());
                break;
            case TITLE:
                this.list.sort(new TitleComparator());
                break;
            case DURATION:
                this.list.sort(new DurationComparator());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean hasNext() {
        return currentLine < list.size();
    }

    @Override
    public AudioFile next() {
        if (!hasNext()) {
        	currentLine = 0;
        	return list.get(currentLine++);
        }
        return list.get(currentLine++);
    }

    public AudioFile jumpToAudioFile(AudioFile file) {
        if (list.contains(file)) {
            currentLine = list.indexOf(file);
            return list.get(currentLine++);
        } else {
            return null;
        }
    }


    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int index) {
        this.currentLine = index;
    }

    public List<AudioFile> getList() {
        return this.list;
    }
    
}
