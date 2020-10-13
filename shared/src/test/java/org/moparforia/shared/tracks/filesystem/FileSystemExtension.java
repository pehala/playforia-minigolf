package org.moparforia.shared.tracks.filesystem;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.softsmithy.lib.nio.file.CopyFileVisitor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemExtension implements BeforeEachCallback, AfterEachCallback {
    private static final String[] DIRS = {
            "/tracks/modern",
            "/tracks/traditional",
            "/tracks/short",
            "/tracks/long",
            "/tracks/basic",
            "/tracks/hio",
            "/tracks/sets",
    };

    private FileSystem fileSystem;

    FileSystem getFileSystem() {
        return this.fileSystem;
    }

    void copyFile(String src) throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(src);
        Path srcPath = Paths.get(resource.toURI());
        Path targetPath = fileSystem.getPath(src);
        CopyFileVisitor.copy(srcPath, targetPath);
    }

    /**
     * Copies directory from resource folder into InMemory file system
     * @param dir Path to the dir
     */
    void copyDir(String dir) throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(dir);
        Path base = Paths.get(getClass().getClassLoader().getResource("").toURI());
        List<Path> files = Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        for (Path file : files) {
            Path relative_path = fileSystem.getPath(base.relativize(file).toString());
            CopyFileVisitor.copy(base.resolve(file), relative_path);
        }
    }

    void copyAll() throws IOException, URISyntaxException {
        copyDir("tracks/");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        if (this.fileSystem != null) {
            this.fileSystem.close();
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        this.fileSystem = MemoryFileSystemBuilder.newEmpty().build("test");
        for (String DIR : DIRS) {
            Path path = fileSystem.getPath(DIR);
            Files.createDirectories(path);
        }
    }
}
