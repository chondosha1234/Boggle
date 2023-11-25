package ass4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

    }

    public Boggle(String[][] board) {
        this.board = board;
    }

    public Boggle(String[] dice) {

        // dice array will have length equal to total number of board spaces, which will be a square of the dimensions i.e. 4x4, 5x5
        int dimension = (int) Math.sqrt(dice.length);
        this.board = new String[dimension][dimension];

        int diceIndex = 0;

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {

                int letterIndex = random.nextInt(6);
                char letter = ' ';

                letter = dice[diceIndex].charAt(letterIndex);

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

    /**
     *
     * @param word
     * @return boolean
     *
     * Method to be the starting point of a search for a word in the board
     */
    public boolean matchWord(String word) {

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                resetBoardVisits();
                if (searchGrid(i, j, word, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recursive method to implement the backtracking searching algorithm
     */
    private boolean searchGrid(int row, int col, String word, int index) {

        // check board bounds
        if (index >= word.length() || row < 0 || row >= this.board.length || col < 0 || col >= this.board[0].length) {
            return false;
        }

        String s = Character.toString(word.charAt(index)).toLowerCase();
        String character = this.board[row][col];

        // space is already visited if the character is upper case
        if (character.equals(character.toUpperCase())) {
            return false;
        }

        if (s.equals(character)) {
            this.board[row][col] = character.toUpperCase();

            // have matched the last letter of the word
            if (index == word.length()-1) {
                return true;
            }

            if (searchGrid(row - 1, col - 1, word, index + 1)) {
                // go up and left
                return true;
            } else if (searchGrid(row - 1, col, word, index + 1)) {
                // go up
                return true;
            } else if (searchGrid(row - 1, col + 1, word, index + 1)) {
                //go up and right
                return true;
            } else if (searchGrid(row, col - 1, word, index + 1)) {
                // go left
                return true;
            } else if (searchGrid(row, col + 1, word, index + 1)) {
                // go right
                return true;
            } else if (searchGrid(row + 1, col -1, word, index + 1)) {
                // go down and left
                return true;
            } else if (searchGrid(row + 1, col, word, index + 1)) {
                // go down
                return true;
            } else if (searchGrid(row + 1, col + 1, word, index + 1)) {
                // go down and right
                return true;
            }

            // undo the visited spot for backtracking purposes
            this.board[row][col] = character.toLowerCase();
        }
        return false;
    }

    /**
     * Helper method to set the boggle board characters to lower case, since a search starting from one point
     * will set them to upper case to mark them as visited
     */
    private void resetBoardVisits() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                this.board[i][j] = this.board[i][j].toLowerCase();
            }
        }
    }

    public static List<String> getAllValidWords(String dictionaryName, String boardName) {
        Boggle boggle = new Boggle(boardName);

        List<String> validWords = new LinkedList<>();

        try {
            File file = new File(dictionaryName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();

                if (boggle.matchWord(word)) {
                    validWords.add(word);
                }


            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("No dictionary file found!");
        }

        return validWords;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String[] str : this.board) {
            for (String s : str) {
                sb.append(s.toLowerCase());
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     *
     * Helper method to create board with highlighted letters for a correct answer.
     * If this is put into the other methods like searchGrid, it would get used and print out boards
     * everytime the getAllValidWords method searches a word
     */
    private void printHighlightBoard() {
        for (String[] str : this.board) {
            for (String s : str) {
                System.out.print(s + " ");

            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String boardName = args[0];
        String dictName = args[1];

        Scanner scanner = new Scanner(System.in);
        int points = 0;
        boolean gameOver = false;

        Boggle boggle = new Boggle(boardName);

        System.out.println("Welcome to the game of Boggle!\n");

        while (!gameOver) {
            System.out.println("\n" + boggle);

            System.out.println("\nPlease enter a word that can be made from the letters: ");
            String word = scanner.nextLine();

            if (word.length() < 3) {
                System.out.println("That word is too short! Try again...");
                continue;
            }

            if (boggle.matchWord(word)) {
                System.out.println("That word is on the board!");
                boggle.printHighlightBoard();
                points += word.length()-2;
                System.out.println("You receive " + (word.length()-2) + " points.");
                System.out.println("You now have " + points + " points.");
            } else {
                System.out.println("That word is not on the board!");
            }

            System.out.println("\nDo you want to end the game? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")) {
                gameOver = true;
                System.out.println("You got a total of " + points + " points!");
                System.out.println("Do you want to see the maximum number of words that can be generated from this board? (y/n)");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    List<String> allValidWords = getAllValidWords(dictName, boardName);
                    System.out.println("There are " + allValidWords.size() + " possible words in this board.");
                }
            }
        }

    }

}
