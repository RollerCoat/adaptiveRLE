import java.util.Arrays;
public class Test {
    public static String listToString(int[] list, int maxLength) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Math.min(list.length, maxLength); i++) {
            sb.append(list[i]);
        }

        if (list.length > maxLength) {
            sb.append("... (").append(list.length).append(" Elemente)");
        }

        return sb.toString();
    }

    public static String maxStringCounter(String str, int maxLength) {
        StringBuilder sb = new StringBuilder();
        sb.append(
            str.substring(0, Math.min(maxLength, str.length()))
        );

        if (str.length() > maxLength) {
            sb.append("... (").append(str.length()).append(" Bits)");
        }

        return sb.toString();
    }

    public static double toMilliseconds(long start, long end) {
        return (end - start) / 1_000_000.0;
    }

    public static double[] test(
        int[] original
    ) {
        if (original.length <= 0) return new double[]{0, 0, 0};
        long startTimeCompress = System.nanoTime();
        String compressed = Compress.compress(original);
        long endTimeCompress = System.nanoTime();

        long startTimeDecompress = System.nanoTime();
        Decompress.decompress(compressed);
        long endTimeDecompress = System.nanoTime();

        return new double[]{
            (double) toMilliseconds(startTimeCompress, endTimeCompress),
            (double) toMilliseconds(startTimeDecompress, endTimeDecompress)};
    }

    public static void printTest(int[] original) {
        String compressed = Compress.compress(original);
        int[] decompressed = Decompress.decompress(compressed);

        boolean success = Arrays.equals(original, decompressed);
        int saved = original.length - compressed.length();

        System.out.println("====== ORIGINAL " + listToString(original, 25) + " ======");
        System.out.println("Komprimiert: " + maxStringCounter(compressed, 25));
        System.out.println("Wiederhergestellt: " + listToString(decompressed, 25));
        System.out.println(
            (saved >= 0 ? "Gespart: " : "Overhead: ")
            + Math.abs(saved) + " Bits."
        );
        System.out.println("====== " + (success ? "SUCCESS" : "FAIL") + " ======");
    }

    public static double[][] test(
        int[][] original
    ) {
        double[] allCompressTime = new double[original.length];
        double[] allDecompressTime = new double[original.length];

        int i = 0;
        for (int[] originalElement : original) {
            double[] testOfElement = test(
                originalElement
            );
            allCompressTime[i] = testOfElement[0];
            allDecompressTime[i] = testOfElement[1];

            i++;
        }
        return new double[][]{allCompressTime, allDecompressTime};
    }

    public static void multiTest(int[][] original) {
        int WARMUP = 100;

        for (int i = 0; i < WARMUP; i++) {
            test(original);
        }

        int AMT = 1000;
        double[] allCompressTime = new double[original.length];
        double[] allDecompressTime = new double[original.length];
        double[] averageCompressTime = new double[original.length];
        double[] averageDecompressTime = new double[original.length];
        Arrays.fill(allCompressTime, 0.0);
        Arrays.fill(allDecompressTime, 0.0);
        Arrays.fill(averageCompressTime, 0.0);
        Arrays.fill(averageDecompressTime, 0.0);

        for (int i = 0; i < AMT; i++) {
            double[][] testScores = test(original);
            for (int j = 0; j < allCompressTime.length; j++) {
                allCompressTime[j] += testScores[0][j];
                allDecompressTime[j] += testScores[1][j];
            }
        }
        for (int i = 0; i < original.length; i++) {
            averageCompressTime[i] = allCompressTime[i] / AMT;
            averageDecompressTime[i] = allDecompressTime[i] / AMT;

            System.out.println("\nTestfall " + (i + 1));
            printTest(original[i]);
            System.out.println("Komprimieren: " + String.format("%.4f", averageCompressTime[i]) + " ms");
            System.out.println("Dekomprimieren: " + String.format("%.4f", averageDecompressTime[i]) + " ms");
        }
    }

    public static void main(String[] args) {
        int[] tenThousandZeros = new int[10_000];
        Arrays.fill(tenThousandZeros, 0);

        int[] tenThousandZerosAndOnes = new int[10_000];
        for (int i = 0; i < 10_000; i++) {
            tenThousandZerosAndOnes[i] = i % 2;
        }

        int[] tenThousandRandoms = new int[10_000];
        for (int i = 0; i < 10_000; i++) {
            tenThousandRandoms[i] = (int) (Math.random() * 2);
        }

        int[][] testCases = {
            tenThousandZeros,
            tenThousandZerosAndOnes,
            tenThousandRandoms
        };

        multiTest(testCases);
    }
}