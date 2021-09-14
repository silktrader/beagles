package eu.silktrader.beagles;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PasswordFeeder {

    private final List<String> passwords;
    private final Random seed = new Random();

    public PasswordFeeder() {
        // load passwords from file into a final field within a try block
        passwords = loadPasswords();
    }

    private List<String> loadPasswords() {
        try {
            return Files.lines(Paths.get("data", "passwords.txt"), Charset.defaultCharset()).toList();
    } catch (IOException e) {
      e.printStackTrace();
      return List.of();
            }
    }

    public char[] feedRandomPassword() {
        return passwords.get(seed.nextInt(passwords.size())).toCharArray();
    }

    public List<String> getPasswords() {
        // can't expose as iterable; require length
        return Collections.unmodifiableList(passwords);
    }
}
