package org.moparforia.shared.tracks.filesystem.lineparser;

import org.moparforia.shared.tracks.filesystem.TrackFileParser;

import java.util.Collections;
import java.util.Map;


/**
 * Line parser that returns entire contents of the line
 */
public class SimpleLineParser implements TrackFileParser.LineParser {

    private final String parameter_name;

    public SimpleLineParser(String parameter_name) {
        this.parameter_name = parameter_name;
    }

    @Override
    public Map<String, Object> apply(String line) {
        return Collections.singletonMap(parameter_name, line);
    }
}
