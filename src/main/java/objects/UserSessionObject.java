package objects;

import java.util.ArrayList;

public class UserSessionObject {
    private String duration;
    private ArrayList<String> pages;
    private String startTime;

    public UserSessionObject(String duration, ArrayList<String> pages, String startTime) {
        this.duration = duration;
        this.pages = pages;
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<String> getPages() {
        return pages;
    }

    public void setPages(ArrayList<String> pages) {
        this.pages = pages;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setAll(String duration, ArrayList<String> pages, String startTime) {
        this.duration = duration;
        this.pages = pages;
        this.startTime = startTime;
    }
}
