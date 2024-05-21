package studiplayer.audio;
import java.io.File;

import studiplayer.basic.BasicPlayer;
public abstract class SampledFile extends AudioFile {

    protected long duration;
	protected float frameRate;
	protected long numberOfFrames;
	

    public SampledFile(String path) throws NotPlayableException{
    	super(path);
    	File file = new File(this.pathname);
    	if (!file.canRead()) {
            // If the file is not a readable file, throw a RuntimeException
            throw new NotPlayableException(pathname, "Error: Unable to read file at path: " );
    }
    }
    
    public SampledFile() {
    	super();
    }

    public void play() throws NotPlayableException{
    	try {
            BasicPlayer.play(pathname);
    	} catch (Exception e ) {
    		throw new NotPlayableException(pathname, "Error: Unable to play. " );
    	}
    }

  
    public void stop() {
            BasicPlayer.stop();
    }

    
    public void togglePause() {
            BasicPlayer.togglePause();
    }

    public String formatDuration() {
        return timeFormatter(duration);  // Format duration using timeFormatter
    }

    public String formatPosition() {
        long currentPosition = BasicPlayer.getPosition();
        return timeFormatter(currentPosition);
    }

    public static String timeFormatter(long timeInMicroSeconds) {
    	
    	if (timeInMicroSeconds < 0) {
    		throw new IllegalArgumentException("Time in microseconds cannot be negative");
    	}
    	
        if (timeInMicroSeconds > 5999999999L) {
            throw new IllegalArgumentException("Time value exceeds the maximum supported value for formatting");
        }
        
    	long seconds = timeInMicroSeconds / 1000000;
    	long minutes = seconds / 60;
    	seconds %= 60;
    	return String.format("%02d:%02d", minutes, seconds);
    }

    public long getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
    
    public static void main(String[] args) {
 	}
}
