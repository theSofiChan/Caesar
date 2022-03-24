import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class cypher {
    private static final ArrayList<Character> ALPHABET = new ArrayList<>();
    public static String src = "dagon.txt";
    public static String dest="cyphered.txt";
    public static String dest1="deciphered.txt";
    public static String dest2="bruteforce.txt";
    public static String source="theCallOfCthulhu.txt";
    public static String stats="decypheres_stats.txt";
    public static Scanner scanner=new Scanner(System.in);
    public static Integer key=0;




    public static void main(String[] args) throws IOException {
        Collections.addAll(ALPHABET, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', ',', '.', ' ', '-', '\"', '\n', '\t', '–', ':', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','‘');
        menu();
    }
    public static void input(){
        try{
            key= scanner.nextInt();
            if(key<0){
                throw new Exception();
            }
        }catch(Exception e){
            System.out.println("You should input a positive integer");
            input();
        }
    }

    public static void menu() throws IOException {
        System.out.println("Sir Lovecraft is at the phone");
        System.out.println("1 to cypher");
        System.out.println("2 to decipher");
        System.out.println("3 to decipher with brute force");
        System.out.println("4 to decipher with statistical analysis");
        int input=scanner.nextInt();
        if(input == 1){
            System.out.println("Write down a positive number as a key");
            input();
            cypher(read(src), key);
        }else if(input==2){
            System.out.println("Write down a positive number as a key");
            input();
            decypher(read(dest), key,dest1);
        }else if(input==3){
            brute_force(read(dest));
        }else if(input==4){
            statistics(read(source), read(dest));
        }else{
            System.out.println("You've made a mistake you will regret. Cthulhu is coming for you. \nWe can give you one last chance");
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
    public static String cypher(String st, int key) throws IOException {
        char[] charArray = st.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < st.length(); i++) {
            char letter = charArray[i];
            int index_in_alphabet = ALPHABET.indexOf(letter);
            char cyphered_letter = ALPHABET.get(((index_in_alphabet + key) % ALPHABET.size()));
            stringBuilder.append(cyphered_letter);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
        writer.write(stringBuilder.toString());
        writer.close();
        return stringBuilder.toString();
    }
    public static String decypher(String st, int key,String destination) throws IOException {
        char[] charArray = st.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < st.length(); i++) {
            char letter = charArray[i];
            int index_in_alphabet = ALPHABET.indexOf(letter);
            char cyphered_letter = ALPHABET.get(Math.floorMod((index_in_alphabet - key), ALPHABET.size()));
            stringBuilder.append(cyphered_letter);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(destination));
        writer.write(stringBuilder.toString());
        writer.close();
        return stringBuilder.toString();
    }
    public static String brute_force(String st) throws IOException {
        char[] charArray = st.toCharArray();
        int key = 0;
        while (key <= st.length()) {
            StringBuilder inner_string_builder = new StringBuilder();
            for (int i = 0; i < st.length(); i++) {
                char letter = charArray[i];
                int index_in_alphabet = ALPHABET.indexOf(letter);
                int newindex = Math.floorMod((index_in_alphabet - key), ALPHABET.size());
                char newa = ALPHABET.get(newindex);
                inner_string_builder.append(newa);
            }
            Pattern stringPattern = Pattern.compile("(,\\s).+(\\.\\s)");
            Matcher m = stringPattern.matcher(inner_string_builder.toString());
            if (m.find()&&inner_string_builder.toString().contains("horror")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(dest2));
                writer.write(inner_string_builder.toString());
                writer.close();
                return inner_string_builder.toString();
            }
            key++;
        }
        return null;
    }
    public static void statistics(String source_text, String text) throws IOException {
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
        System.out.println("больше всего символа "+maxKey_source);
        long maxValueIn_chars_text=(Collections.max(chars_text.values()));
        Character maxKey_text='a';
        for (Map.Entry<Character, Long> entry : chars_text.entrySet()) {
            if (entry.getValue() == maxValueIn_chars_text) {
                maxKey_text = entry.getKey();
            }
        }
        int index_in_alphabet_source_max = ALPHABET.indexOf(maxKey_source);
        int index_in_alphabet_text_max = ALPHABET.indexOf(maxKey_text);
        int key=(Math.abs(index_in_alphabet_source_max-index_in_alphabet_text_max));
        decypher(text,key,stats);
    }

}
