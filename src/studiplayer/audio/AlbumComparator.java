package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	private String getAlbum(AudioFile file) {
		if (file instanceof TaggedFile) {
			return ((TaggedFile) file).getAlbum();
		}
		return null;
	}

	@Override
	public int compare(AudioFile o1, AudioFile o2) {

		if (o1 != null && o2 != null) {
			String album1 = getAlbum(o1);
			String album2 = getAlbum(o2);
			if (album1 == null && album2 == null)
				return 0;
			if (album1 == null)
				return -1;
			if (album2 == null)
				return 1;
			return album1.compareTo(album2);

		} else {
			throw new RuntimeException();
		}
	}

}
