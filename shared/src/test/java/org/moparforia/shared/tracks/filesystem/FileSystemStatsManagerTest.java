package org.moparforia.shared.tracks.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TrackCategory;
import org.moparforia.shared.tracks.stats.TrackStats;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;

import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileSystemStatsManagerTest {
    private final double PRECISION = 0.001d;
    @RegisterExtension
    final FileSystemExtension extension = new FileSystemExtension();

    FileSystemStatsManager statsManager;

    Track single = new Track("4 da Crew", "Aither", "Data", TrackCategory.MODERN);
    Track empty_stats = new Track("SprtTrack", "Sprt", "Data", TrackCategory.MODERN);

    @BeforeEach
    void beforeEach() {
        FileSystem fileSystem = this.extension.getFileSystem();
        statsManager = new FileSystemStatsManager(fileSystem);
    }

    @Test
    void testSimpleLoad() throws IOException, URISyntaxException {
        extension.copyDir("tracks/modern");

        statsManager.load();

        TrackStats stats = statsManager.getStats(single);
        assertEquals("Sprt", stats.getBestPlayer());
        assertEquals(537, stats.getTotalAttempts());
        assertEquals(11734, stats.getTotalStrokes());
        assertEquals(4, stats.getBestPar());
        assertEquals(0.039 , stats.getPercentageOfBestPar(), PRECISION);
        assertEquals(7.752, stats.getAverageRating(), PRECISION);
    }

    @Test
    void testEmptyStats() throws IOException, URISyntaxException {
        extension.copyDir("tracks/modern");

        statsManager.load();
        TrackStats stats = statsManager.getStats(empty_stats);
        assertEquals("", stats.getBestPlayer());
        assertEquals(0, stats.getTotalAttempts());
        assertEquals(0, stats.getTotalStrokes());
        assertEquals(-1, stats.getBestPar());
        assertEquals(NaN , stats.getPercentageOfBestPar(), PRECISION);
        assertEquals(NaN, stats.getAverageRating(), PRECISION);

    }
}