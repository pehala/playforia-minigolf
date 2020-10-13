package org.moparforia.shared.tracks.filesystem;

import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TrackCategory;
import org.moparforia.shared.tracks.stats.TrackStats;
import org.moparforia.shared.tracks.stats.StatsManager;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSystemStatsManager implements StatsManager {
    protected final FileSystem fileSystem;

    private static FileSystemStatsManager instance;

    private Map<Track, TrackStats> stats;

    public FileSystemStatsManager(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public static FileSystemStatsManager getInstance() {
        if (instance == null) {
            instance = new FileSystemStatsManager(FileSystems.getDefault());
        }
        return instance;
    }

    public void load() throws IOException {
        stats = loadStats();
    }

    public Map<Track, TrackStats> loadStats() throws IOException {
        List<TrackStats> tracks = new ArrayList<>();
        for (TrackCategory type : TrackCategory.values()) {
            if (type == TrackCategory.ALL || type == TrackCategory.UNKNOWN) {
                continue;
            }
            Path tracksPath = fileSystem.getPath("tracks", type.getDir());
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tracksPath,
                    entry -> entry.toString().endsWith(".track"));
            for (Path filePath : directoryStream) {
                tracks.add(TrackFileParser.parseStats(filePath, type));
            }
        }
        return tracks.stream()
                .collect(Collectors.toMap(TrackStats::getTrack, stats -> stats));
    }

    @Override
    public TrackStats getStats(Track track) {
        if (!stats.containsKey(track)) {
            throw new IllegalArgumentException("Couldn't find stats for track " + track.getName() + " on filesystem");
        }
        return stats.get(track);
    }

    @Override
    public void rate(Track track, int rating) {
        // No-op, FileSystemStats are readonly
    }

    @Override
    public void addScore(Track track, String player, int par) {
        // No-op, FileSystemStats are readonly
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
