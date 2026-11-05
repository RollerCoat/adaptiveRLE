import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Compress {
    public static String to_str(int[] numbers) {
        StringBuilder sb = new StringBuilder();

        for (int num : numbers) {
            sb.append(num);
        }
        return sb.toString();
    }

    public static ArrayList<Integer> turnToList(int[] original) {
        ArrayList<Integer> compressed = new ArrayList<>();
        int previous = original[0];
        int size = 0;
        for (int i : original) {
            if (previous != i) {
                compressed.add(size);
                size = 0;
            }
            size++;
            previous = i;
        }

        compressed.add(size);
        return compressed;
    }

    public static int log2(int n){ // 1 + floor[log2(x)]
        if (n <= 0) throw new IllegalArgumentException();
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    public static int get_largest_space_needed(ArrayList<Integer> list) {
        int max = Collections.max(list);
        return log2(max);
    }

    public static String binary_fixed(int bits, int i) {
        String binary = Integer.toBinaryString(i);
        if (binary.length() > bits) throw new IllegalArgumentException();

        return (String
        .format("%" + bits + "s", binary)
        .replace(' ', '0'));
    }

    public static String to_binary(ArrayList<Integer> list) {
        int bits_per_group = get_largest_space_needed(list);
        StringBuilder result = new StringBuilder();

        for (int i : list) {
            result.append(binary_fixed(bits_per_group, i));
        }

        return result.toString();
    }

    public static int[] createOriginal(int BIT_SIZE) {
        if (BIT_SIZE <= 1) throw new IllegalArgumentException();

        int[] original = new int[BIT_SIZE]; // 40 by 25 image
        Random rand = new Random();
        int i = 0;
        while (i < original.length) {
            int random_number = rand.nextInt(2);
            for (int j = 0; j < rand.nextInt((int)(Math.sqrt(BIT_SIZE))); j++) {
                if (i >= original.length) break;

                original[i] = random_number; // Generiert 0 oder 1
                i++;
            }
        }

        return original;
    }

    public static String compressAlgorithm(int[] original) {
        if (original.length == 0) {
            return "0";
        }

        ArrayList<Integer> compressed = turnToList(original);
        String first = String.valueOf(original[0]);
        String compressed_binary = (
            binary_fixed(5, get_largest_space_needed(compressed)) +
            first +
            to_binary(compressed)
        );

        return compressed_binary;
    }

    public static String compress(int[] original) {
        String compressed = compressAlgorithm(original);
        boolean isDataSaved = compressed.length() < original.length;

        String firstNumber = isDataSaved ? "1" : "0";
        if (isDataSaved) return firstNumber + compressed;
        else return firstNumber + to_str(original);
    }

    public static void main(String[] args) {
        int[] original = createOriginal(10000);
        String compressed_binary = compress(original);

        System.out.println(
            "=".repeat(120) +
            "\nUnkomprimiert:\n" +
            to_str(original) +
            "\n(" +
            original.length +
            " Bits)\n" +
            "=".repeat(120) +
            "\n\n" +
            "=".repeat(120) +
            "\nKomprimiert:\n" +
            compressed_binary +
            "\n(" +
            compressed_binary.length() +
            " Bits)\n" +
            "=".repeat(120)
        );
    }
}