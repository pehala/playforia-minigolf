package org.moparforia.shared.tracks.filesystem;

import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TrackCategory;
import org.moparforia.shared.tracks.filesystem.lineparser.BestTimeLineParser;
import org.moparforia.shared.tracks.filesystem.lineparser.RatingsLineParser;
import org.moparforia.shared.tracks.filesystem.lineparser.ScoreInfoLineParser;
import org.moparforia.shared.tracks.filesystem.lineparser.SimpleLineParser;
import org.moparforia.shared.tracks.stats.TrackStats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class for parsing Track file in format like this.
 * Allows for parsing of similar files or adding custom properties.
 *
 * V 1
 * A {AUTHOR OF TRACK}
 * N {NAME OF TRACK}
 * T data
 * I {NUMBER OF PLAYERS TO COMPLETE},{NUMBER OF STROKES},{BEST NUMBER OF STROKES},{NUMBER OF PEOPLE THAT GOT BEST STROKE}
 * B {FIRST BEST PAR PLAYER},{UNIX TIMESTAMP OF FIRST BEST PAR}000
 * L {LAST BEST PAR PLAYER},{UNIX TIMESTAMP OF LAST BEST PAR}000
 * R {RATING: 0},{RATING: 1},{RATING: 2},{RATING: 3},{RATING: 4},{RATING: 5},{RATING: 6},{RATING: 7},{RATING: 8},{RATING: 9},{RATING: 10}
 */
public class TrackFileParser {
    private static final Map<Character, LineParser> BASE_PARSERS;
    private static final Map<Character, LineParser> STATS_PARSERS;

    // Initialize parser with all LineParsers
    static
    {
        HashMap<Character, LineParser> tmp_map = new HashMap<>();
        tmp_map.put('A', new SimpleLineParser("author"));
        tmp_map.put('N', new SimpleLineParser("name"));
        tmp_map.put('T', new SimpleLineParser("data"));
        BASE_PARSERS = Collections.unmodifiableMap(tmp_map);
        tmp_map.put('R', new RatingsLineParser());
        tmp_map.put('I', new ScoreInfoLineParser());
        tmp_map.put('B', new BestTimeLineParser("bestTime", "bestPlayer"));
//      Uncomment if you want to also parse lastTime and lastPlayer
//      tmp_map.put("L", new BestTimeLineParser("lastTime", "lastPlayer"));
        STATS_PARSERS = Collections.unmodifiableMap(tmp_map);
    }

    public static Map<String, Object> parse(Map<Character, LineParser> parser, Path path) throws IOException {
        return Files.lines(path)
                .map(line -> parseLine(parser, line))
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)
                );
    }

    private static Map<String, Object> parseLine(Map<Character, LineParser> parsers, String line) {
        Character character = line.charAt(0);
        if (parsers.containsKey(character)) {
            return parsers.get(character).apply(line.substring(2));
        }
        return Collections.emptyMap();
    }

    public static Track parseTrack(Path path, TrackCategory category) throws IOException {
        Map<String, Object> parsed = parse(BASE_PARSERS, path);
        return constructTrack(category, parsed);
    }

    private static Track constructTrack(TrackCategory category, Map<String, Object> parsed) {
        String name = (String) parsed.get("name");
        String author = (String) parsed.get("author");
        String data = (String) parsed.get("data");
        return new Track(name, author, data, category);
    }

    public static TrackStats parseStats(Path path, TrackCategory category) throws IOException {
        Map<String, Object> parsed = parse(STATS_PARSERS, path);
        Track track = constructTrack(category, parsed);
        int[] ratings = (int[]) parsed.getOrDefault("ratings", new int[10]);
        int attempts = (int) parsed.getOrDefault("attempts", 0);
        int strokes = (int) parsed.getOrDefault("strokes", 0);
        int bestPar = (int) parsed.getOrDefault("bestPar", -1);

        // Widen int primitive type, to support integer division resulting in double
        int numberOfBestPar = (Integer) parsed.getOrDefault("numberOfBestPar", 0);
        LocalDate bestTime = (LocalDate) parsed.getOrDefault("bestTime", LocalDate.now());
        String bestPlayer = (String) parsed.getOrDefault("bestPlayer", "");

        double bestParPercentage = (double) numberOfBestPar / attempts;

        return new FileSystemTrackStats(
                attempts, strokes, bestPar, bestParPercentage, numberOfBestPar, bestPlayer, bestTime, ratings, track
        );
    }

    /**
     * Class that takes line and returns map of parsed parameters
     */
    public interface LineParser extends Function<String, Map<String, Object>> {}
}
