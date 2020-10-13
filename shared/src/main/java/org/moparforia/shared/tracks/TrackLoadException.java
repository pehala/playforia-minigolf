package org.moparforia.shared.tracks;

public class TrackLoadException extends Exception {
    public TrackLoadException(String s) {
        super(s);
    }

    public TrackLoadException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
