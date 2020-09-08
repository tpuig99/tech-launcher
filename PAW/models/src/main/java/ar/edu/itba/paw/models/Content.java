package ar.edu.itba.paw.models;

import java.net.URL;
import java.sql.Timestamp;

public class Content {
    private long contentId;
    private long frameworkId;
    private long userId;
    private String title;
    private long votesUp;
    private long votesDown;
    private Timestamp timestamp;
    private URL link;
    private ContentTypes type;

    public Content(long contentId, long frameworkId, long userId, String title, long votesUp, long votesDown, Timestamp timestamp, URL link, ContentTypes type) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.link = link;
        this.type = type;
    }

    public long getContentId() {
        return contentId;
    }

    public long getFrameworkId() {
        return frameworkId;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public long getVotesUp() {
        return votesUp;
    }

    public long getVotesDown() {
        return votesDown;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public URL getLink() {
        return link;
    }

    public ContentTypes getType() {
        return type;
    }
}
