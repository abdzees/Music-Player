package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

	public WavFile(String path) throws NotPlayableException {
		super(path);
		try {
			readAndSetDurationFromFile();
		} catch (Exception e) {
			throw new NotPlayableException(pathname, "Error: Unable to read file at path: ", e);
		}
	}

	public WavFile() {
		super();
	}

	public void readAndSetDurationFromFile() throws NotPlayableException {

		try {
			WavParamReader.readParams(pathname);

			frameRate = WavParamReader.getFrameRate();
			numberOfFrames = WavParamReader.getNumberOfFrames();

			duration = computeDuration(numberOfFrames, frameRate);
		} catch (Exception e) {
			// Catch any other exceptions and throw as NotPlayableException
			throw new NotPlayableException(pathname, "Error: Unable to read the Parameters", e);
		}
	}

	public static long computeDuration(long numberOfFrames, float frameRate) {
		return (long) ((numberOfFrames / frameRate) * 1000000);
	}

	@Override
	public String toString() {
		String fileRepresentation = super.toString();
		return fileRepresentation + " - " + timeFormatter(duration);
	}
}
