package com.WHXeO46.github.stepcounter.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Date implements Serializable {
    private final long timestamp;

    public Date() {
        timestamp = System.currentTimeMillis();
    }

    public Date(long timestamp) {
        this.timestamp = timestamp;
    }

    private long getTimestamp() {
        return timestamp;
    }

    @NotNull
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date(getTimestamp()));
    }

    public int compareTo(Date other) {
        return this.getTimestamp()-other.getTimestamp()<=0?-1:1 ;
    }
}
