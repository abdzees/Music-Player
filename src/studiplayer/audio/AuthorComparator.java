package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {

		if (o1 != null && o2 != null) {
			String author1 = o1.getAuthor();
			String author2 = o2.getAuthor();
			if (author1 == null && author2 == null)
				return 0;
			if (author1 == null)
				return -1;
			if (author2 == null)
				return 1;
			return author1.compareTo(author2);

		} else {
			throw new RuntimeException();
		}
	}

}
