import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Caesar {
    private static final ArrayList<Character> ALPHABET = new ArrayList<>();
    public static Integer key=0;

    public static void main(String[] args) throws IOException {
        Collections.addAll(ALPHABET, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', ',', '.', ' ', '-', '\"', '\n', '\t', '–', ':', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','‘');
        menu();
    }

    public static void input(){
        System.out.println("You should input an integer in the range of -20 to 20");
        try{
            Scanner scanner=new Scanner(System.in);
            if(scanner.hasNextInt()) {
                key = scanner.nextInt();
                if (key>20 || key <-20) {
                    input();
                }
            }else {
                throw new Exception();
            }
        }catch(Exception e){
            System.out.println("You should input an integer in the range of -20 to 20, restarting the program");
            input();
    }
    }

    public static void menu() throws IOException {
        System.out.println("Sir Lovecraft is at the phone");
        System.out.println("1 to cypher");
        System.out.println("2 to decipher");
        System.out.println("3 to decipher with brute force");
        System.out.println("4 to decipher with statistical analysis");
        try {
            Scanner scanner=new Scanner(System.in);
            int input=scanner.nextInt();
            if (input == 1) {
                System.out.println("Path to file to cypher: ");
                String source=scanner.next();
                System.out.println("Path to the target file: ");
                String target=scanner.next();
                input();
                cypher(Objects.requireNonNull(read(source)), key, target);
            } else if (input == 2) {
                System.out.println("Path to file to decipher: ");
                String source= scanner.next();
                System.out.println("Path to the target file: ");
                String target= scanner.next();
                input();
                decypher(Objects.requireNonNull(read(source)), key, target);
            } else if (input == 3) {
                System.out.println("Path to file to decipher: ");
                String source= scanner.next();
                System.out.println("Path to the target file: ");
                String target= scanner.next();
                brute_force(Objects.requireNonNull(read(source)),target);
            } else if (input == 4) {
                System.out.println("Path to file to the source text: ");
                String source_text= scanner.next();
                System.out.println("Path to file to decipher: ");
                String source= scanner.next();
                System.out.println("Path to the target file: ");
                String target= scanner.next();
                statistics(Objects.requireNonNull(read(source_text)), Objects.requireNonNull(read(source)),target);
            } else {
                System.out.println("You've made a mistake you will regret. Cthulhu is coming for you. \nWe can give you one last chance");
                menu();
            }
        }catch(Exception e){
            System.out.println("Something went wrong");
            menu();
        }
    }
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void cypher(String source, int key, String target) throws IOException {
        char[] charArray = source.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char letter = charArray[i];
            int index_in_alphabet = ALPHABET.indexOf(letter);
            char cyphered_letter=letter;
            if(index_in_alphabet!=-1) {
                cyphered_letter = ALPHABET.get(((ALPHABET.size()+index_in_alphabet + key) % ALPHABET.size()));
            }
            stringBuilder.append(cyphered_letter);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(target));
        writer.write(stringBuilder.toString());
        writer.close();
    }
    public static void decypher(String source, int key,String target) throws IOException {
        char[] charArray = source.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char letter = charArray[i];
            int index_in_alphabet = ALPHABET.indexOf(letter);
            char cyphered_letter=letter;
            if(index_in_alphabet!=-1) {
                cyphered_letter = ALPHABET.get(Math.floorMod((index_in_alphabet - key), ALPHABET.size()));
            }
            stringBuilder.append(cyphered_letter);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(target));
        writer.write(stringBuilder.toString());
        writer.close();
    }
    public static void brute_force(String source,String target) throws IOException {
        char[] charArray = source.toCharArray();
        int key = -20;
        while (key <= source.length()) {
            StringBuilder inner_string_builder = new StringBuilder();
            for (int i = 0; i < source.length(); i++) {
                char letter = charArray[i];
                int index_in_alphabet = ALPHABET.indexOf(letter);
                int newindex = Math.floorMod((index_in_alphabet - key), ALPHABET.size());
                char cyphered_letter=letter;
                if(index_in_alphabet!=-1) {
                    cyphered_letter = ALPHABET.get(newindex);
                }
                inner_string_builder.append(cyphered_letter);
            }
            Pattern stringPattern = Pattern.compile("(,\\s).+(\\.\\s)");
            Matcher m = stringPattern.matcher(inner_string_builder.toString());
            if (m.find()&&inner_string_builder.toString().contains("horror")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(target));
                writer.write(inner_string_builder.toString());
                writer.close();
            }
            key++;
        }
    }
    public static void statistics(String source_text, String text,String target) throws IOException {
        char[] all_letters_source=source_text.toCharArray();
        Stream<Character> charStreamSource = new String(all_letters_source).chars().mapToObj(i->(char)i);
        Map<Character, Long> chars_source_text = charStreamSource
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        char[] all_letters_text=text.toCharArray();
        Stream<Character> charStreamText = new String(all_letters_text).chars().mapToObj(i->(char)i);;
        Map<Character, Long> chars_text = charStreamText
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long maxValueIn_chars_source_text=(Collections.max(chars_source_text.values()));
        Character maxKey_source='a';
        for (Map.Entry<Character, Long> entry : chars_source_text.entrySet()) {
            if (entry.getValue() == maxValueIn_chars_source_text) {
                maxKey_source = entry.getKey();
            }
        }
        long maxValueIn_chars_text=(Collections.max(chars_text.values()));
        Character maxKey_text='a';
        for (Map.Entry<Character, Long> entry : chars_text.entrySet()) {
            if (entry.getValue() == maxValueIn_chars_text) {
                maxKey_text = entry.getKey();
            }
        }
        int index_in_alphabet_source_max = ALPHABET.indexOf(maxKey_source);
        int index_in_alphabet_text_max = ALPHABET.indexOf(maxKey_text);
        int key=(index_in_alphabet_text_max-index_in_alphabet_source_max);
        decypher(text,key,target);
    }

}