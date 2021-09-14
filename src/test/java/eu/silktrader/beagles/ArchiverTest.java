package eu.silktrader.beagles;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ArchiverTest {

    private final Path rootPath = Paths.get("data");

    @Test
    public void clearArchives() throws IOException {
        // clear all zip files in the directory
        Files.list(rootPath).filter(p -> p.toString().endsWith(".zip")).forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void createArchives() throws IOException {
        var archiver = new Archiver();
        Files.list(rootPath)
                .filter(file -> !Files.isDirectory(file))
                .forEach(archiver::archive);
    }

    @Test
    public void extractArchives() throws IOException {
        var archiver = new Archiver();
        Files.list(rootPath).filter(file -> file.toString().endsWith(".zip")).forEach(archiver::unarchive);
    }
}

