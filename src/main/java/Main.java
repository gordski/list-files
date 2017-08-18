
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;

public class Main {
  public static void main(String[] args) throws URISyntaxException, IOException {

    // Get URI for folder from Classpath.
    URI folderURI = Main.class.getResource("/file-folder").toURI();
    System.out.println(String.format("URI: %s", folderURI));

    try {

      // Try getting folder directly.
      handleFolder(Paths.get(folderURI));

    } catch (FileSystemNotFoundException e) {

      // Failed to get the folder, probably a jar file, try creating a file system.
      FileSystem folderFs = FileSystems.newFileSystem(folderURI, Collections.singletonMap("create", "true"));
      handleFolder(folderFs.provider().getPath(folderURI));
      folderFs.close();

    }
  }

  private static void handleFolder(Path folderPath) {
    try {

      // Process all the files.
      Files.walk(folderPath)
              .filter(Files::isRegularFile)
              .forEach(Main::handleFile);

    } catch (IOException ignored) {

    }
  }

  private static void handleFile(Path p) {
    try {

      System.out.println("File: " + p.toString());
      BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(p)));
      reader.lines().forEach(l -> System.out.println("  " + l));

    } catch (IOException ignored) {

    }
  }
}
