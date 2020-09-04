package objects;

public class VisitObject {
    private String urlVisited;
    private String timestamp;

    public VisitObject(String urlVisited, String timestamp) {
        this.urlVisited = urlVisited;
        this.timestamp = timestamp;
    }

    public String getUrlVisited() {
        return urlVisited;
    }

    public void setUrlVisited(String urlVisited) {
        this.urlVisited = urlVisited;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
