import java.util.Arrays;

public class Decompress {
    public static int getBitLength(String compressed) {
        String firstFiveNums = compressed.substring(
            0, Math.min(5, compressed.length())
        );
        return Integer.parseInt(firstFiveNums, 2);
    }

    public static int[] decompressAlgorithm(String compressed) {
        if (compressed.length() < 6) return new int[0]; // Edge cases for f.ex. "001"

        StringBuilder resultString = new StringBuilder();
        int bitLength = getBitLength(compressed);
        if (bitLength <= 0) throw new IllegalArgumentException();

        char firstNumber = compressed.charAt(5);

        for (int i = 6; i < compressed.length(); i += bitLength) {
            int amount = Integer.parseInt(
                compressed.substring(i, i + bitLength), 2
            );
            resultString.append(
                String.valueOf(firstNumber).repeat(amount)
            );

            firstNumber = (firstNumber == '1') ? '0' : '1'; // 0 => 1, 1 => 0
        }

        int[] result = new int[resultString.length()];

        for (int i = 0; i < resultString.length(); i++) {
            result[i] = resultString.charAt(i) - '0';
        }

        return result;
    }

    public static int[] stripFirstNumber(String compressed) {
        int[] result = new int[compressed.length() - 1];

        for (int i = 1; i < compressed.length(); i++) {
            result[i - 1] = compressed.charAt(i) - '0';
        }

        return result;
    }

    public static int[] decompress(String compressed) {
        if (compressed.charAt(0) == '0') {
            return stripFirstNumber(compressed);
        }
        else {
            return decompressAlgorithm(compressed.substring(1)); // TODO
        }
    }

    public static void main(String[] args) {
        int[] original = Compress.createOriginal(10000);
        String compressed = Compress.compress(original);
        int[] result;

        result = decompress(compressed);

        System.out.println(
            Arrays.equals(original, result)
        );
    }
}
