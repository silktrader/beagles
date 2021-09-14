package eu.silktrader.beagles;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class Archiver {

    //private final String rootPath;
    private final PasswordFeeder passwordFeeder = new PasswordFeeder();         // ideally injected as interface
    private final int maxThreads = Runtime.getRuntime().availableProcessors();            // -1 due to main thread

    private final ZipParameters parameters = new ZipParameters() {{
        setEncryptFiles(true);
        setEncryptionMethod(EncryptionMethod.AES);
    }};

    public Archiver() {
        //this.rootPath = Paths.get(rootPath) + "/";

    }

    public void archive(Path filePath) {

        final var filename = filePath.toAbsolutePath().toString();

        // get file names without extension, provided they have one
        var baseName = filename.contains(".") ? filename.substring(0, filename.lastIndexOf('.')) : filename;

        // create and encrypt new archive
        try {
            new ZipFile(baseName + ".zip", passwordFeeder.feedRandomPassword()).addFile(filename, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

//    public void decryptAll(String baseDir) {
//
//        // determine the ideal number of threads to start
//        var maxThreads = Runtime.getRuntime().availableProcessors();            // -1 due to main thread
//
//        var possiblePasswords = new PasswordFeeder().getPasswords();
//
//        // create and start decrypting threads
//        IntStream.range(0, maxThreads).forEach(i -> new DecryptingThread(possiblePasswords).start());
//
//    }

    public void unarchive(Path filePath) {

        // create shared queue from immutable collection of passwords
        var passwords = new ArrayBlockingQueue<String>(passwordFeeder.getPasswords().size());
        passwords.addAll(passwordFeeder.getPasswords());

        // create and start decrypting threads
        IntStream.range(0, maxThreads).forEach(i -> new DecryptingThread(passwords, filePath).start());

    }

}

