package studiplayer.audio;
import java.io.File;
import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile{

	private String album;

    public TaggedFile() {
        super();
    }

    public TaggedFile(String path) throws NotPlayableException {
        super(path);
        File file = new File(path);
        if (!file.canRead()) {
            // If the file is not a readable file, throw a NotPlayableException
            throw new NotPlayableException("Error: Unable to read file at path: " + path, path);
        }
        readAndStoreTags();
    }

	public String getAlbum() {
		return album;
	}
	
	public long getDuration() {
		return duration;
	}

	public void readAndStoreTags() throws NotPlayableException {
		
		try {
	    // Read tags using TagReader
        Map<String, Object> tagMap = TagReader.readTags(pathname);
        
        // Store tags if available
        if (tagMap.containsKey("title")) {
            title = tagMap.get("title").toString().trim();
        }
        if (tagMap.containsKey("author")) {
            author = tagMap.get("author").toString().trim();
        }
        if (tagMap.containsKey("album")) {
            album = tagMap.get("album").toString().trim();
        }
        if (tagMap.containsKey("duration")) {
        	duration = (long) tagMap.get("duration");
        }
		} catch (Exception e) {
	        // Catch any other exceptions and throw as NotPlayableException
	        throw new NotPlayableException(pathname, "Error: Unable to read tags due to unexpected error", e);
		}
	}
		
	@Override
	public String toString() {
	    String fileRepresentation1 = super.toString();
	    if (album != null && !album.isEmpty()) {
	        return fileRepresentation1.trim() + " - " + album.trim() + " - " + timeFormatter(duration);
	    } else {
	        return super.toString() + " - " + timeFormatter(duration);
	    }
	}

	
}
