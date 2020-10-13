package org.moparforia.shared.tracks.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.moparforia.shared.tracks.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTrackManagerTest {
    @RegisterExtension
    final FileSystemExtension extension = new FileSystemExtension();

    TrackManager manager;

    @BeforeEach
    void beforeEach() {
        FileSystem fileSystem = this.extension.getFileSystem();
        manager = new FileSystemTrackManager(fileSystem);
    }

    @Test
    void testSimpleTrackLoad() throws TrackLoadException, IOException, URISyntaxException {
        extension.copyDir("tracks/modern/");

        manager.load();
        assertEquals(6, manager.getTracks().size());
        assertEquals(0, manager.getTrackSets().size());

        assertEquals(6, manager.findAllByCategory(TrackCategory.MODERN).size());
        assertEquals(6, manager.findAllByCategory(TrackCategory.ALL).size());
        assertEquals(0, manager.findAllByCategory(TrackCategory.BASIC).size());

        assert manager.isLoaded();

        Track track = manager.findTrackByName("4 da Crew");
        assertNotNull(track);
        assertEquals("Data", track.getMap());
        assertEquals("Aither", track.getAuthor());
        assertEquals("4 da Crew", track.getName());
        assertEquals("Aither", track.getAuthor());
    }

    /**
     * Loads modern tracks
     * Loads Tracksets
     *
     * oakpark.trackset should be ignored because it didnt contain any loaded tracks
     * birchwood.trackset should be have only 2 songs
     */
   @Test
   void testSimpleSetLoad() throws IOException, URISyntaxException, TrackLoadException {
       extension.copyDir("tracks/modern/");
       extension.copyDir("tracks/sets/");

       manager.load();
       assertEquals(1, manager.getTrackSets().size());
       TrackSet birchwood = manager.getTrackSet("Birchwood");

       assertEquals(2, birchwood.getTracks().size());
       assertEquals("Birchwood", birchwood.getName());
       assertEquals(TrackSetDifficulty.EASY, birchwood.getDifficulty());

   }

   @Test
   void testLoad() throws IOException, URISyntaxException, TrackLoadException {
       extension.copyAll();

       manager.load();
       assertEquals(18, manager.getTracks().size());
       assertEquals(2, manager.getTrackSets().size());

       assertEquals(6, manager.findAllByCategory(TrackCategory.MODERN).size());
       assertEquals(18, manager.findAllByCategory(TrackCategory.ALL).size());
       assertEquals(2, manager.findAllByCategory(TrackCategory.SHORT).size());
       assertEquals(3, manager.findAllByCategory(TrackCategory.TRADITIONAL).size());
       assertEquals(2 ,manager.findAllByCategory(TrackCategory.HIO).size());
       assertEquals(3, manager.findAllByCategory(TrackCategory.BASIC).size());

       assert manager.isLoaded();
   }

    @Test
    void testRandomTracksIncorrectLimit() {
       assertThrows(IllegalArgumentException.class, () -> manager.getRandomTracks(0, TrackCategory.ALL));
       assertThrows(IllegalArgumentException.class, () -> manager.getRandomTracks(-1, TrackCategory.ALL));
    }

    @Test
    void testRandomTracks() throws IOException, URISyntaxException, TrackLoadException {
        extension.copyAll();

        manager.load();
        assertEquals(3, manager.getRandomTracks(3, TrackCategory.MODERN).size());
        assertEquals(6, manager.getRandomTracks(50, TrackCategory.MODERN).size());
   }

    /**
     * This means that if randomTracks is called on a category that doesn't have any tracks it will return empty list
     */
   @Test
   void testRandomTracksEmpty() throws IOException, URISyntaxException, TrackLoadException {
       extension.copyDir("tracks/modern");

       manager.load();
       assertEquals(0, manager.getRandomTracks(50, TrackCategory.BASIC).size());
   }
}