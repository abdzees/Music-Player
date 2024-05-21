package studiplayer.audio;

public class AudioFileFactory {

	public static AudioFile createAudioFile(String path) throws NotPlayableException{
		// Extracting extension
		String extension = path.substring(path.lastIndexOf(".") + 1).toLowerCase();

		// Categorizing extensions
		if (extension.equals("wav")) {
			return new WavFile(path);
		} else if (extension.equals("ogg") || extension.equals("mp3")) {
			return new TaggedFile(path);
		} else {
			throw new NotPlayableException(path, "Unknown suffix for AudioFile");
		}
	}

}
