package eu.silktrader.beagles;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;

public class DecryptingThread extends Thread {

    private final ArrayBlockingQueue<String> sharedPasswords;
    private final Path filePath;

    public DecryptingThread(ArrayBlockingQueue<String> sharedPasswords, Path filePath) {

        this.sharedPasswords = sharedPasswords;
        this.filePath = filePath;
        this.setName(this.getClass().getSimpleName());
        this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void start() {
        System.out.println("Starting " + this.getName());
        super.start();
    }

  @Override
  public void run() {

    final var fileName = filePath.toAbsolutePath().toString();
    final var extractPath =
        filePath.getParent().toString() + "/" + filePath.getFileName().toString().substring(0, 5);

    // fetch next password
    var keepGuessing = true;
    char[] password = new char[0];
    while (keepGuessing) {
      try {
        password = sharedPasswords.take().toCharArray();
      } catch (InterruptedException e) {
        keepGuessing = false;
        // tk kill
      }
      try {
        new ZipFile(fileName, password).extractAll(extractPath);

        // no exception thrown means success
        keepGuessing = false;
      } catch (ZipException e) {
        System.out.println("Failed password");
          keepGuessing = true; // unn
      }
    }
        }

}
