package com.Tregaki.designooq;

import java.sql.Timestamp;

public class Chat {
    private boolean seen;
    private Long timestamp;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
