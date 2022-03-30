import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Caesar {
    private static final ArrayList<Character> ALPHABET = new ArrayList<>();
    public static Integer key = 0;

    public static void main(String[] args) throws IOException {
        Collections.addAll(ALPHABET, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', ',', '.', ' ', '-',
                '\"', '\n', '\t', '–', ':', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '‘');
        menu();
    }

    /**
     * The user should input an integer key to be used in methods cypher() and decipher()
     */
    public static void keyInput() {
        System.out.println("You should input an integer in the range of -20 to 20");
        try {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                key = scanner.nextInt();
                if (key > 20 || key < -20) {
                    keyInput();
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("You should input an integer in the range of -20 to 20, restarting the program");
            keyInput();
        }
    }

    /**
     * Main menu where user chooses operations with files
     */
    public static void menu() {
        System.out.println("Choose an action");
        System.out.println("1 to cypher");
        System.out.println("2 to decipher");
        System.out.println("3 to decipher with brute force");
        System.out.println("4 to decipher with statistical analysis");
        try {
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextInt()) {
                case 1 -> {
                    System.out.println("Path to file to cypher: ");
                    String sourceCypher = scanner.next();
                    System.out.println("Path to the target file: ");
                    String targetCypher = scanner.next();
                    keyInput();
                    String readToCypher=Objects.requireNonNull(read(sourceCypher));
                    cypher(readToCypher, key, targetCypher);
                }
                case 2 -> {
                    System.out.println("Path to file to decipher: ");
                    String sourceDecypher = scanner.next();
                    System.out.println("Path to the target file: ");
                    String targetDecypher = scanner.next();
                    keyInput();
                    String readToDecipher = Objects.requireNonNull(read(sourceDecypher));
                    decipher(readToDecipher, key, targetDecypher);
                }
                case 3 -> {
                    System.out.println("Path to file to decipher: ");
                    String sourceBruteForce = scanner.next();
                    System.out.println("Path to the target file: ");
                    String targetBruteForce = scanner.next();
                    String readBruteForce=Objects.requireNonNull(read(sourceBruteForce));
                    bruteForce(readBruteForce, targetBruteForce);
                }
                case 4 -> {
                    System.out.println("Path to file to the source text: ");
                    String sourceTextStats = scanner.next();
                    System.out.println("Path to file to decipher: ");
                    String DecipherStats = scanner.next();
                    System.out.println("Path to the target file: ");
                    String targetStats = scanner.next();
                    String readStatsSource=Objects.requireNonNull(read(sourceTextStats));
                    String readStatsText=Objects.requireNonNull(read(DecipherStats));
                    statistics(readStatsSource, readStatsText, targetStats);
                }
                default -> {
                    System.out.println("Something went wrong");
                    menu();
                }
            }

        } catch (Exception e) {
            System.out.println("Something went wrong");
            menu();
        }
    }

    /**
     * This method is uded to generate a string that contains all data from the file to be cyphered or deciphered later
     * @param path The path to a file to read
     * @return Returns a string that contains all symbols from the text including \n
     */
    public static String read(String path) {
        StringBuilder text = new StringBuilder();
        try (FileReader in = new FileReader(path);
             BufferedReader reader = new BufferedReader(in)) {
            while (reader.ready()) {
                String line = reader.readLine();
                text.append(line);
                text.append('\n');
            }
            return text.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that cyphers data using the Caesar's cypher
     * @param source String that contains data from a source file
     * @param key The integer key to be used in the Caesar's cipher
     * @param target String that contains a path to the target file to write the cyphered data
     */
    public static void cypher(String source, int key, String target) throws IOException {
        char[] charArray = source.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char letter = charArray[i];
            int indexInAlphabet = ALPHABET.indexOf(letter);
            char cypheredLetter = letter;
            if (indexInAlphabet != -1) {
                cypheredLetter = ALPHABET.get(((ALPHABET.size() + indexInAlphabet + key) % ALPHABET.size()));
            }
            stringBuilder.append(cypheredLetter);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(target));
        writer.write(stringBuilder.toString());
        writer.close();
    }

    /**
     * Method that deciphers data using the Caesar's cypher
     * @param source String that contains data from a  cyphered source file
     * @param key The integer key to be used in the Caesar's cipher
     * @param target String that contains a path to the target file to write the deciphered data
     */
    public static void decipher(String source, int key, String target) throws IOException {
        char[] charArray = source.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char letter = charArray[i];
            int indexInAlphabet = ALPHABET.indexOf(letter);
            char cypheredLetter = letter;
            if (indexInAlphabet != -1) {
                cypheredLetter = ALPHABET.get(Math.floorMod((indexInAlphabet - key), ALPHABET.size()));
            }
            stringBuilder.append(cypheredLetter);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(target));
        writer.write(stringBuilder.toString());
        writer.close();
    }

    /**
     * Method that deciphers data using Caesar's cypher by means of brute force
     * @param source String that contains data from a  cyphered source file
     * @param target String that contains a path to the target file to write the deciphered data
     */
    public static void bruteForce(String source, String target) throws IOException {
        char[] charArray = source.toCharArray();
        int key = -20;
        while (key <= source.length()) {
            StringBuilder innerStringBuilder = new StringBuilder();
            for (int i = 0; i < source.length(); i++) {
                char letter = charArray[i];
                int indexInAlphabet = ALPHABET.indexOf(letter);
                int newindex = Math.floorMod((indexInAlphabet - key), ALPHABET.size());
                char cypheredLetter = letter;
                if (indexInAlphabet != -1) {
                    cypheredLetter = ALPHABET.get(newindex);
                }
                innerStringBuilder.append(cypheredLetter);
            }
            Pattern stringPattern = Pattern.compile("(,\\s).+(\\.\\s)");
            Matcher m = stringPattern.matcher(innerStringBuilder.toString());
            if (m.find() && innerStringBuilder.toString().contains("horror")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(target));
                writer.write(innerStringBuilder.toString());
                writer.close();
            }
            key++;
        }
    }

    /**
     * Method that deciphers data using Caesar's cypher by means of statistical analysis
     * @param sourceText String that contains data from a file to analyse
     * @param text String that contains data from a  cyphered source file
     * @param target String that contains a path to the target file to write the deciphered data
     */
    public static void statistics(String sourceText, String text, String target) throws IOException {
        char[] allLettersSource = sourceText.toCharArray();
        Stream<Character> charStreamSource = new String(allLettersSource).chars().mapToObj(i -> (char) i);
        Map<Character, Long> charsSourceText = charStreamSource
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        char[] allLettersText = text.toCharArray();
        Stream<Character> charStreamText = new String(allLettersText).chars().mapToObj(i -> (char) i);
        Map<Character, Long> charsText = charStreamText
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long maxValueInCharsSourceText = (Collections.max(charsSourceText.values()));
        Character maxKeySource = 'a';
        for (Map.Entry<Character, Long> entry : charsSourceText.entrySet()) {
            if (entry.getValue() == maxValueInCharsSourceText) {
                maxKeySource = entry.getKey();
            }
        }
        long maxValueInCharsText = (Collections.max(charsText.values()));
        Character maxKeyText = 'a';
        for (Map.Entry<Character, Long> entry : charsText.entrySet()) {
            if (entry.getValue() == maxValueInCharsText) {
                maxKeyText = entry.getKey();
            }
        }
        int indexInAlphabetSourceMax = ALPHABET.indexOf(maxKeySource);
        int indexInAlphabetTextMax = ALPHABET.indexOf(maxKeyText);
        int key = (indexInAlphabetTextMax - indexInAlphabetSourceMax);
        decipher(text, key, target);
    }

}
