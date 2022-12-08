package dev.mqqx.aoc.year22;

import static dev.mqqx.aoc.util.SplitUtils.linesList;
import static java.lang.Integer.parseInt;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Day7 {

  static int solvePart1(Resource input) {
    final List<String> commands = linesList(input);

    return readFileSystem(commands).values().stream()
        .map(Directory::size)
        .filter(size -> size <= 100_000)
        .reduce(0, Integer::sum);
  }

  record File(String name, int size) {}

  record Directory(String name, Set<File> files, Set<Directory> subDirectories, String parent) {
    public int sizeOfFiles() {
      return files.stream().map(File::size).reduce(0, Integer::sum);
    }

    public int size() {
      return sizeOfFiles() + subDirectories.stream().map(Directory::size).reduce(0, Integer::sum);
    }
  }

  static int solvePart2(Resource input) {
    final Map<String, Directory> fileSystem = readFileSystem(linesList(input));

    final int diskSpace = 70_000_000;
    final int updateSpace = 30_000_000;
    final int freeSpace = diskSpace - fileSystem.get("/").size();
    final int spaceNeededToBeFreedAtLeast = updateSpace - freeSpace;

    return fileSystem.values().stream()
        .map(Directory::size)
        .filter(size -> size >= spaceNeededToBeFreedAtLeast)
        .min(Comparator.naturalOrder())
        .orElse(-1);
  }

  private static Map<String, Directory> readFileSystem(List<String> commands) {
    final Map<String, Directory> fileSystem = new HashMap<>();
    Directory lastDirectory = null;
    boolean isLS = false;
    String currentPath = "";

    for (String command : commands) {
      if ("$ cd ..".equals(command) && lastDirectory.parent != null) {
        lastDirectory = fileSystem.get(lastDirectory.parent);
        currentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
      } else if ("$ ls".equals(command)) {
        isLS = true;
      } else if (command.startsWith("$ cd ")) {
        isLS = false;
        final String[] commandParts = command.split(" ");
        final String folderName = commandParts[commandParts.length - 1];
        if (lastDirectory == null) {
          lastDirectory = new Directory(folderName, new HashSet<>(), new HashSet<>(), null);
          fileSystem.put(folderName, lastDirectory);
        } else {
          // set full path as directory name as there are many directories with the same name
          currentPath += "/" + folderName;
          Directory currentDirectory = fileSystem.get(folderName);

          // only create directory if it does not exist yet
          if (currentDirectory == null) {
            currentDirectory =
                new Directory(currentPath, new HashSet<>(), new HashSet<>(), lastDirectory.name);
            lastDirectory.subDirectories.add(currentDirectory);
            fileSystem.put(currentPath, currentDirectory);
          }
          lastDirectory = currentDirectory;
        }
      } else if (isLS && Character.isDigit(command.charAt(0))) {
        final String[] commandParts = command.split(" ");

        lastDirectory.files.add(new File(commandParts[1], parseInt(commandParts[0])));
      }
    }
    return fileSystem;
  }
}
