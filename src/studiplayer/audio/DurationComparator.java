package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

	private long getDuration(AudioFile file) {
		if (file instanceof SampledFile) {
			return ((SampledFile) file).getDuration();
		}
		return 0;
	}

	@Override
	public int compare(AudioFile o1, AudioFile o2) {

		if (o1 != null && o2 != null) {
			long duration1 = getDuration(o1);
			long duration2 = getDuration(o2);
			if (duration1 == 0 && duration2 == 0)
				return 0;
			if (duration1 == 0)
				return -1;
			if (duration2 == 0)
				return 1;
			return (int) (duration1 - duration2);

		} else {
			throw new RuntimeException();
		}
	}
}
