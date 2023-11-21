package ass4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Boggle {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final double[] FREQUENCIES = {
            0.08167, 0.01492, 0.02782, 0.04253, 0.12703, 0.02228,
            0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025,
            0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987,
            0.06327, 0.09056, 0.02758, 0.00978, 0.02360, 0.00150,
            0.01974, 0.00074
    };

    // 16 boggle dice version (4x4 board)
    private static final String[] BOGGLE_1992 = {
            "LRYTTE", "VTHRWE", "EGHWNE", "SEOTIS",
            "ANAEEG", "IDSYTT", "OATTOW", "MTOICU",
            "AFPKFS", "XLDERI", "HCPOAS", "ENSIEU",
            "YLDEVR", "ZNRNHL", "NMIQHU", "OBBAOJ"
    };


    // 25 boggle dice version (5x5 board)
    private static final String[] BOGGLE_BIG = {
            "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
            "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCENST",
            "CEIILT", "CEILPT", "CEIPST", "DDHNOT", "DHHLOR",
            "DHLNOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
            "FIPRSY", "GORRVW", "IPRRRY", "NOOTUW", "OOOTTU"
    };


    private String[][] board;

    private final Random random = new Random();


    public Boggle(int N) {
        this.board = new String[N][N];

        double[] cumulativeFreq = new double[26];
        double total = 0;

        for (int i = 0; i < 26; i++) {
            total += FREQUENCIES[i];
            cumulativeFreq[i] = total;
        }

        for (double val : cumulativeFreq) {
            System.out.println(val);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                double selection = random.nextDouble(1);
                System.out.println(selection);

                // use the random double to select letter based on freq
                for (int k = 0; k < 26; k++) {
                    // if the random double is in the cumulative range, it will choose that char and set board space
                    if (selection <= cumulativeFreq[k]) {
                        this.board[i][j] = Character.toString(ALPHABET.charAt(k));
                        k = 26; // first value found, so stop the k loop
                    }
                }
            }
        }

        for (String[] strs : board) {
            for (String s : strs) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

    }

    public Boggle(String[][] board) {
        this.board = board;
    }

    public Boggle(String[] dice) {

        int diceIndex = 0;

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {

                int letterIndex = random.nextInt(5);
                char letter = ' ';

                if (this.board.length == 4) {
                    letter = BOGGLE_1992[diceIndex].charAt(letterIndex);
                } else if (this.board.length == 5) {
                    letter = BOGGLE_BIG[diceIndex].charAt(letterIndex);
                }
                diceIndex++;

                this.board[i][j] = Character.toString(letter);
            }
        }

    }

    public Boggle(String filename) {

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            int width = scanner.nextInt();
            int height = scanner.nextInt();
            scanner.nextLine();

            String[][] board = new String[width][height];

            int rowCounter = 0;
            int columnCounter;
            while (scanner.hasNextLine()) {
                columnCounter = 0;
                while (columnCounter < width) {
                    board[rowCounter][columnCounter] = scanner.next();
                    columnCounter++;
                }
                rowCounter++;
                scanner.nextLine();
            }

            this.board = board;

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("No file found with that name.");
        }
    }

    public boolean matchWord(String word) {
        return true;
    }

    public static List<String> getAllValidWords(String dictionaryName, String boardName) {
        return null;
    }

    public static void main(String[] args) {
        Boggle b = new Boggle(4);
    }

}
