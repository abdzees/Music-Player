package studiplayer.audio;
import java.io.File;

public abstract class AudioFile {

	// Attributes
	protected String pathname;
	private String filename;
	protected String author;
	protected String title;
	private static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}

	// Parameterized constructor to analyze the provided path
	public AudioFile(String path) throws NotPlayableException {
		parsePathname(path);
		parseFilename(filename);
		
        // Check if the normalized file path refers to a readable file
      File file = new File(this.pathname);
      if (!file.exists() || !file.isFile() || !file.canRead()) {
          // If the file does not exist or is not a readable file, throw a RuntimeException
          throw new NotPlayableException(pathname,"Error: Unable to read file at path: ");
      }
	}

	// Default constructor (available for creating empty instances)
	public AudioFile() {
		// Initialize with default or empty values
		this.pathname = "";
		this.filename = "";
		this.author = "";
		this.title = "";
	}

	// Getters
	public String getPathname() {
		return this.pathname;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getAuthor() {
		return this.author;
	};
	

	public String getTitle() {
		return this.title;
	}

	// Parsing
	public void parsePathname(String path) {

		if (isWindows() == true) {

			/// Windows environment => reduce \
			String newPathname = path.replace('/', '\\');
			newPathname = newPathname.trim();
			while (newPathname.indexOf("\\\\") != -1) {
				newPathname = newPathname.replace("\\\\", "\\");
			}

			// Separate Filename
			filename = newPathname.substring(newPathname.lastIndexOf('\\') + 1);
			this.filename = filename.trim();
//
//			//Separate Pathname
			this.pathname = newPathname;

		} else {

			/// UNIX environment => swap \ with /
			String newPathname = path.replace('\\', '/');
			while (newPathname.indexOf("//") != -1) {
				newPathname = newPathname.replace("//", "/");
			}

			// Separate Filename
			filename = newPathname.substring(newPathname.lastIndexOf('/') + 1);
			this.filename = filename.trim();

			// Separate Pathname
			this.pathname = newPathname;

		}

	}

	public void parseFilename(String filename) {

		int dashPos = filename.indexOf(" - ");
		int lastDot = filename.lastIndexOf(".");

		if (dashPos != -1 && lastDot != -1) {
			author = filename.substring(0, dashPos);
			title = filename.substring(dashPos + 3, lastDot);
			this.author = author.trim();
			this.title = title.trim();

		} else if (dashPos != -1 && lastDot == -1) {
			author = filename.substring(0, dashPos);
			this.author = author.trim();
			this.title = "";

		} else if (dashPos == -1 && lastDot != -1) {
			this.author = "";
			title = filename.substring(0, lastDot);
			this.title = title.trim();

		} else if (dashPos == -1 && lastDot == -1) {
			this.author = "";
			this.title = filename.trim();
		}
	}

	// Functions
    public abstract void play() throws NotPlayableException;
    public abstract void togglePause();
    public abstract void stop();

    public String toString() {
    	
    	if (getAuthor().isEmpty()) {
    		return getTitle();
    	}else {
    		return getAuthor() + " - " + getTitle();
    	}
    	}
    
	
    // Abstract Methods
    public abstract String formatDuration();
    public abstract String formatPosition();
    
	// Main
	public static void main(String[] args) {
 	}

}
