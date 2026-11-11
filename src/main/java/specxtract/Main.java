package specxtract;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -cp out specxtract.Main <file-or-folder>");
            return;
        }

        Path path = Paths.get(args[0]);
        List<Path> files = Files.isDirectory(path) ? findJavaFiles(path) : List.of(path);

        int total = 0;
        for (Path file : files) {
            String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            List<Token> tokens = new Lexer(text).tokenize();          // <-- the Lexer runs here

            System.out.println("=== " + file + "  (" + tokens.size() + " tokens)");
            for (Token t : tokens) {
                System.out.println("  line " + t.line + "   " + t);
            }
            total += tokens.size();
        }
        System.out.println("\nTotal: " + files.size() + " file(s), " + total + " tokens");
    }

    /** Finds every .java file inside a folder, including sub-folders. */
    private static List<Path> findJavaFiles(Path root) throws IOException {
        try (Stream<Path> s = Files.walk(root)) {
            return s.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }
}
