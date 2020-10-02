package com.Tregaki.designooq;

public class Messages {
    private boolean seen;
    private long time;

    public String getKey() {
        return key;
    }

    public void setId(String key) {
        this.key = key;
    }

    public String key;
    public Messages(boolean seen, long time, String from, String message, String type) {
        this.seen = seen;
        this.time = time;
        this.from = from;
        this.message = message;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;

    public Messages(boolean seen, long time, String message, String type) {
        this.seen = seen;
        this.time = time;
        this.message = message;
        this.type = type;
    }

    private String message;
    private String type;

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public Messages() {
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
