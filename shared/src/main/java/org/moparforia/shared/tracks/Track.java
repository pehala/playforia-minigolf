package org.moparforia.shared.tracks;

import java.util.Objects;

/**
 * track shit
 */
public class Track {

    private final String track;

    private final String author;

    private String map;

    private final TrackCategory category;

    /**
     * man thats an ugly constructor
     *
     * @param name        name of this track
     * @param author      the author homie that wrote this track
     * @param map         the track data/map string
     */
    public Track(String name, String author, String map, TrackCategory category) {
        this.track = name;
        this.author = author;
        this.map = map;
        this.category = category;
    }

    /**
     * new Track(1,"Boats and hoes","fc","B3A48DE48DE48DE48DE48DE48DEBAQQ46D3EG13DEG14DEG13D5E13DEE14DEE13D5E13DEE14DEE13D5E13DEE14DEE13D5E13DEE6DBMAQE6DEE13D5ECAAE11DEE6DBAQQE6DEE11DCBA6E13DBOAQE6DEE6DBOAQE13D5E21DEE21D5E21DEE21D5E21DEE21D5E21DEE21D4E46DEE48DE48DE48DE48DE48DE48D",
     * new int[]{2629492,7166639,2,1191141},new String[] {"fc","Tiikoni"}, new int[]{1034197200000,1370170660930},
     * new int[]{1630,567,647,835,1148,3945,3755,3346,2924,2672,21566});
     */

    public String getName() {
        return track;
    }

    public String getAuthor() {
        return author;
    }

    public TrackCategory getCategory() {
        return category;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String data) {
        this.map = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track)) return false;
        Track track = (Track) o;
        return getName().equals(track.getName()) &&
                getAuthor().equals(track.getAuthor()) &&
                getMap().equals(track.getMap()) &&
                getCategory() == track.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(track, getAuthor(), map, getCategory());
    }
}