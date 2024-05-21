package studiplayer.audio;

public class NotPlayableException extends Exception {

    private String pathname;

    public NotPlayableException(String pathname, String msg) {
        super(msg);
        this.pathname = pathname;
    }

    public NotPlayableException(String pathname, Throwable t) {
        super(t);
        this.pathname = pathname;
    }

    public NotPlayableException(String pathname, String msg, Throwable t) {
        super(msg, t);
        this.pathname = pathname;
    }

    // Getter method for pathname
    public String getPathname() {
        return pathname;
    }

    // Override toString() to include pathname information
    @Override
    public String toString() {
        return "studiplayer.audio.NotPlayableException: " + pathname + ": " + getMessage() ;
    }
}
