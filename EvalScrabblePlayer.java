import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

/*

  Author: Haoran Chang and Philip Chan
  Email: hchang2014@my.fit.edu
  Pseudocode: Philip Chan

  Usage: EvalScrabblePlayer wordFile [numOfGames] [seed]

  Input:
  wordFile has valid words, one on each line
  numOfGames is the number of different games/boards [optional]
  seed is for generating different boards [optional]

  Description:

  The goal is to evaluate ScrabblePlayer
  Validity and points for words are in the assignment.

  The performance of ScrabblePlayer is measured by:

  a.  points: total points of found word
  b.  speed: time in second for finding words
  c.  space consumption: memory consumption
  d.  overall score--(Points^2)/sqrt(time * memory)  


  --------Pseudocode for evaluating ScrabblePlayer---------------

     1.  create scrabble player
     ScrabblePlayer player = new ScrabblePlayer(wordFile) // a list of English words

     2.  play scrabble numOfGames times
     board = randomly generate a board
     availableLetters = 7 random letters from the scrabble word distribution 
     playerWord = player.getScrabbleWord(board, availableLetters)

     3.  report performance
     check validity of player word according to the rules in the assignment
     calculate points as in the assignment
     report performance

 */


public class EvalScrabblePlayer {

    private static ThreadMXBean bean;  // for measuring cpu time

    public static void main(String[] args) throws IOException {

        if (args.length < 1 || args.length > 3) {
            System.err.println("Usage: EvalScrabblePlayer wordFile [seed] [numOfGames]");
            System.exit(-1);
        }

        //Default seed if second argument is not passed
        long seed = 123456789;
        if ((args.length == 2) || (args.length == 3)) {
            seed = Long.parseLong(args[1]);
        }

        //Default number of games if third argument is not passed
        int numOfGames = 1;
        if (args.length == 3) {
            numOfGames = Integer.parseInt(args[2]);
        }

        // create a bean object for getting cpu time
        bean = ManagementFactory.getThreadMXBean();
        if (!bean.isCurrentThreadCpuTimeSupported()) {
            System.err.println("cpu time not supported, use wall-clock time:");
            System.err.println("Use System.nanoTime() instead of bean.getCurrentThreadCpuTime()");
            System.exit(-1);
        }

        // create our dictionary of words
        Scanner           dictFile = new Scanner(new File(args[0]));
        ArrayList<String> dictionary = new ArrayList<>();
        while (dictFile.hasNext()) {
            dictionary.add(dictFile.nextLine().toUpperCase());
        }
        // create the scrabble player and play scrabble
        ScrabblePlayer player = createScrabblePlayer(args[0]);
        playScrabble(player, dictionary, numOfGames, seed);

        ScrabblePlayer player2 = player;  // keep player used to avoid garbage collection of player
    }


    /*
     *  create the scrabble player and return the player object
     *  report time and space consumption
     */
    private static ScrabblePlayer createScrabblePlayer(String dictFile) 
    {
        //Preprocessing in ScrabblePlayer
        System.out.println("Preprocessing in ScrabblePlayer...");

        long startPreProcTime = bean.getCurrentThreadCpuTime();
        ScrabblePlayer player = new ScrabblePlayer(dictFile);
        long endPreProcTime = bean.getCurrentThreadCpuTime();

        //Stop if pre-processing runs for more than 5 minutes.
        double processingTimeInSec = (endPreProcTime - startPreProcTime) / 1.0E9;
        if (processingTimeInSec > 300) {
            System.err.println("Preprocessing time \"" + processingTimeInSec + " sec\" is too long...");
            System.exit(-1);
        }

        // report time and memory spent on preprocessing
        DecimalFormat df = new DecimalFormat("0.####E0");
        System.out.println("Pre-processing in seconds (not part of performance): " + df.format(processingTimeInSec));
        System.out.println("Used memory after pre-processing in bytes (not part of performance): " + peakMemoryUsage());

    return player;
    }


    // board score: https://en.wikipedia.org/wiki/Scrabble
    private static HashMap<String, String> createBoardScore()
    {
        HashMap<String, String> pointsOnBoard = new HashMap<String, String>();
        //double letter score
        pointsOnBoard.put("30", "2L");
        pointsOnBoard.put("110", "2L");
        pointsOnBoard.put("62", "2L");
        pointsOnBoard.put("82", "2L");
        pointsOnBoard.put("03", "2L");
        pointsOnBoard.put("73", "2L");
        pointsOnBoard.put("143", "2L");
        pointsOnBoard.put("26", "2L");
        pointsOnBoard.put("66", "2L");
        pointsOnBoard.put("86", "2L");
        pointsOnBoard.put("126", "2L");
        pointsOnBoard.put("37", "2L");
        pointsOnBoard.put("117", "2L");
        pointsOnBoard.put("28", "2L");
        pointsOnBoard.put("68", "2L");
        pointsOnBoard.put("88", "2L");
        pointsOnBoard.put("128", "2L");
        pointsOnBoard.put("011", "2L");
        pointsOnBoard.put("711", "2L");
        pointsOnBoard.put("1411", "2L");
        pointsOnBoard.put("612", "2L");
        pointsOnBoard.put("812", "2L");
        pointsOnBoard.put("314", "2L");
        pointsOnBoard.put("1114", "2L");
        
        //triple letter score
        pointsOnBoard.put("51", "3L");
        pointsOnBoard.put("91", "3L");
        pointsOnBoard.put("15", "3L");
        pointsOnBoard.put("55", "3L");
        pointsOnBoard.put("95", "3L");
        pointsOnBoard.put("135", "3L");
        pointsOnBoard.put("19", "3L");
        pointsOnBoard.put("59", "3L");
        pointsOnBoard.put("99", "3L");
        pointsOnBoard.put("139", "3L");
        pointsOnBoard.put("513", "3L");
        pointsOnBoard.put("913", "3L");
        
        //double word score
        pointsOnBoard.put("11", "2W");
        pointsOnBoard.put("22", "2W");
        pointsOnBoard.put("33", "2W");
        pointsOnBoard.put("44", "2W");
        pointsOnBoard.put("113", "2W");
        pointsOnBoard.put("212", "2W");
        pointsOnBoard.put("311", "2W");
        pointsOnBoard.put("410", "2W");
        pointsOnBoard.put("131", "2W");
        pointsOnBoard.put("122", "2W");
        pointsOnBoard.put("113", "2W");
        pointsOnBoard.put("104", "2W");
        pointsOnBoard.put("1010", "2W");
        pointsOnBoard.put("1111", "2W");
        pointsOnBoard.put("1212", "2W");
        pointsOnBoard.put("1313", "2W");
        
        //triple word score
        pointsOnBoard.put("00", "3W");
        pointsOnBoard.put("70", "3W");
        pointsOnBoard.put("07", "3W");
        pointsOnBoard.put("014", "3W");
        pointsOnBoard.put("140", "3W");
        pointsOnBoard.put("147", "3W");
        pointsOnBoard.put("714", "3W");
        pointsOnBoard.put("1414", "3W");

        return pointsOnBoard;
    }
    

    /*
     *  use the player object to play scrabble with a random seed
     *  measure time, space, points
     */
    private static void playScrabble(ScrabblePlayer player, 
                 ArrayList<String> dictionary,
                 int numOfGames, long seed)
    {
        System.out.println("Playing Scrabble...");

    int       totalPoints = 0;
    long      totalElapsedTime = 0;
    char[][]  board = new char[15][15];
    char[]    availableLetters = new char[7]; 
    Random    rand = new Random(seed);
    
    // points on board: https://en.wikipedia.org/wiki/Scrabble
    HashMap<String, String> scoresOnBoard = createBoardScore();
    // points of tiles: https://en.wikipedia.org/wiki/Scrabble_letter_distributions
    int[] tileScore = new int[] { 0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 
                                  1, 1, 3, 10,1, 1, 1, 1, 4, 4, 8, 4, 10};
    
    for (int game = 0; game < numOfGames; game++)
        {
        //to do: initialize the board with spaces
        //       add a random word of at most length 7 from the dictionary
        ScrabbleWord initialWord = generateBoard(board, dictionary, rand);

        //to do: Randomly pick 7 letters according to the distribution of letters in
        //       the wiki page in the assignment
        generateAvailableLetters(availableLetters, rand);
        
        
        // the player might change board and/or availableLetters, give the player a clone
        char[][] boardClone = board.clone();  
        char[]   availableLettersClone = availableLetters.clone();

        //Calculate the time taken to find the words on the board
        long startTime = bean.getCurrentThreadCpuTime();
        //Play the game of Scrabble and find the words
        ScrabbleWord playerWord = player.getScrabbleWord(boardClone, availableLettersClone);
        
        long endTime = bean.getCurrentThreadCpuTime();

        //System.out.println(endTime - startTime);
        if ((endTime - startTime)/1.0E9 > 1)  // longer than 1 second
            {
            System.err.println("player.getScrabbleWord() exceeded 1 minute");
            System.exit(-1);
            }
        totalElapsedTime += (endTime - startTime);

        //Calculate points for the words found
        totalPoints += calculatePoints(playerWord, initialWord, board, availableLetters, 
                                       dictionary, scoresOnBoard, tileScore);
        //System.out.println("Total: " + totalPoints);
        }

        reportPerformance(totalPoints, totalElapsedTime, peakMemoryUsage(), 
                          numOfGames);
    }




    /*
     * report performance of the scrabble player
     * based on time, space, and points
     */
    private static void reportPerformance(int totalPoints, long totalElapsedTime, long memory, 
                  int numOfGames)
    {
        double avgPoints = totalPoints / numOfGames;
        System.out.printf("Average Points: %.4f\n", avgPoints);

        System.out.println("totalElapsedTime " + totalElapsedTime);

        //Convert elapsed time into seconds, and calculate the Average time
        double avgTime = totalElapsedTime / 1.0E9 / numOfGames;
        //To format the Average time upto 4 decimal places.
        DecimalFormat df = new DecimalFormat("0.####E0"); 
        System.out.println("Time in seconds: " + df.format(avgTime));

        System.out.println("Used memory in bytes: " + memory);

        //Overall Performance
        double pointsSquared = avgPoints * avgPoints;
        if (avgPoints < 0)
            pointsSquared = - pointsSquared;

        System.out.printf("Overall Performance: %.4f\n",  pointsSquared / Math.sqrt(avgTime * memory));

    }


    /**
     * to do:
     *
     * Checks if the word is valid and assigns positive points for valid word and negative points for invalid word
     *
     * @param playerWord word returned by the player
     * @param board The exisiting board before the adding the playerWord
     * @param dictionary dictionary for validity check
     * @return Positive or negative points for the word
     */
    private static int calculatePoints(ScrabbleWord playerWord, ScrabbleWord initialWord, char[][] board,
                                       char[] availableLetters, ArrayList<String> dictionary,
                                       HashMap<String, String> scoresOnBoard, int[] tilePoints) 
    {
        // check if it is a valid word
        if (!validPlayWord(playerWord, initialWord, board, availableLetters, dictionary))
            return 0;
        
        // calculate the points based on wiki        
        String playerW = playerWord.getScrabbleWord();
        int totalScore = 0, bonusForWord = 1;
        int rowID = playerWord.getStartRow();
        int colID = playerWord.getStartColumn();
        for (int i = 0; i < playerW.length(); i++)
        {
            char letterInWord = playerW.charAt(i);
            
            // if the letter is '_', then zero point for that, no need to calculate;
            // otherwise find the points of this letter and the bonus at this position
            if (letterInWord != '_')
            {
                // find the score for this letter
                int letterPoints = tilePoints[(int)letterInWord - 64]; // (int)'A' = 65
                
                //System.out.printf("The %d th letter of %s is %c: %d points, ", i, playerW, letterInWord, letterPoints);
                
                if (playerWord.getOrientation() == 'h')
                    colID = colID + i;
                else
                    rowID = rowID + i;
                
                //System.out.printf("pos (row, col): (%d, %d), ", rowID, colID);
                // find the score on board
                String key = Integer.toString(rowID) + Integer.toString(colID);
                if (scoresOnBoard.containsKey(key))
                {
                    // double/triple letter score
                    if (scoresOnBoard.get(key).equals("2L"))
                        letterPoints = letterPoints * 2;
                    else if (scoresOnBoard.get(key).equals("3L"))
                        letterPoints = letterPoints * 3;
                    // double/triple word score
                    else if (scoresOnBoard.get(key).equals("2W"))
                        bonusForWord = bonusForWord * 2;
                    else if (scoresOnBoard.get(key).equals("3W"))
                        bonusForWord = bonusForWord * 3;
                    //System.out.printf("bonus is %s%n", scoresOnBoard.get(key));
                }
                else
                    //System.out.printf("no bonus.%n");
                
                // sum them up
                totalScore = totalScore + letterPoints;
                
            }
        }
        // final score must multiply the bonus
        totalScore = totalScore * bonusForWord;
        
        return totalScore;
    }
    

    /**
    * function to check if the play word is valid or not
    * @param playWord: a ScrabbleWord that the player wants to add
    * @param board: The exisiting board before the adding the playerWord
    * @param dictionary: dictionary for validity check
    * @return valid: boolean value
    */
    private static boolean validPlayWord(ScrabbleWord playWord, ScrabbleWord initialWord, char[][] board,
                                 char[] availableLetters, ArrayList<String> dictionary)
    {
        // Invalid case 1: the playWord is not in the dictionary
        if (!isInDictionary(playWord.getScrabbleWord(), dictionary))
        {
            //System.out.println(playWord.getScrabbleWord() + " not in the dictionary.");
            return false;
        }
        
        //////////////////////////////////////////////////////////////////////////
        // Invalid case 2: out of boundary
        int maxIndex = 0;
        if (playWord.getOrientation() == 'h')
            maxIndex = playWord.getStartColumn() + playWord.getScrabbleWord().length() - 1;
        else
            maxIndex = playWord.getStartRow() + playWord.getScrabbleWord().length() - 1;
        if (maxIndex > board.length)
        {
            //System.out.println("out of boundary");
            return false;
        }
        // startRow/startColumn less than zero
        else if (playWord.getStartRow() < 0 || playWord.getStartColumn() < 0)
        {
            //System.out.println("out of boundary");
            return false;
        }
        
        ////////////////////////////////////////////////////////////////////////////////
        // Invalid case 3: connection
        String initialW = initialWord.getScrabbleWord();
        String playW = playWord.getScrabbleWord();
        // same orientation, check if playWord contains initialWord
        if (playWord.getOrientation() == initialWord.getOrientation())
        {
            if (!playW.contains(initialW))
            {
                //System.out.println("Can not connected to each other");
                return false;
            }
        }
        // different orientation, check the intersection
        else
        {
            int pStartRow = playWord.getStartRow(), pStartCol = playWord.getStartColumn();
            int iStartRow = initialWord.getStartRow(), iStartCol = initialWord.getStartColumn();
            int pEndRow, pEndCol, iEndRow, iEndCol;
            // playWord is horizontal
            if (playWord.getOrientation() == 'h')
            {
                pEndRow = pStartRow;
                pEndCol = pStartCol + playW.length() - 1;
                // initial word is vertical
                iEndRow = iStartRow + initialW.length() - 1;
                iEndCol = iStartCol;
                
                // if connect, then (pStartCol - 1 <= iStartCol <= pEndCol + 1) AND
                //                  (iStartRow - 1 <= pStartRow <= iEndRow + 1)
                if ((pStartCol - 1 <= iStartCol && iStartCol <= pEndCol + 1) && 
                    (iStartRow - 1 <= pStartRow && pStartRow <= iEndRow + 1))
                {
                    // within the range
                    if ((pStartCol <= iStartCol && iStartCol <= pEndCol) &&
                        (iStartRow <= pStartRow && pStartRow <= iEndRow))
                    {
                        // check the intersection part has the same letter or not
                        if (playW.charAt(iStartCol - pStartCol) != initialW.charAt(pStartRow - iStartRow))
                        {
                            //System.out.println("Invalid connection");
                            return false;
                        }
                    }
                    // boundary case
                    else if ((pStartCol <= iStartCol && iStartCol <= pEndCol) ||
                             (iStartRow <= pStartRow && pStartRow <= iEndRow))
                    {
                        String newWord;
                        
                        // intial word is the head of the new word
                        if (iStartCol == pStartCol - 1)
                            newWord = initialW.substring(pStartRow - iStartRow, pStartRow - iStartRow + 1) + playW;
                        // initial word is the tail of the new word
                        else if (iStartCol == pEndCol + 1)
                            newWord = playW + initialW.substring(pStartRow - iStartRow, pStartRow - iStartRow + 1);
                        // player word is the head of the new word
                        else if (pStartRow == iStartRow - 1)
                            newWord = playW.substring(iStartCol - pStartCol, iStartCol - pStartCol + 1) + initialW;
                        else
                            newWord = initialW + playW.substring(iStartCol - pStartCol, iStartCol - pStartCol + 1);
                        
                        //System.out.println(newWord);
                        if (!isInDictionary(newWord, dictionary))
                            return false;
                    }
                    else
                        return false;
                }
                // no connection
                else
                {
                    //System.out.println("not connected to each other");
                    return false;
                }
            }
            // playWord is vertical
            else
            {
                pEndRow = pStartRow + playW.length() - 1;
                pEndCol = pStartCol;
                // initial word is horizontal
                iEndRow = iStartRow;
                iEndCol = iStartCol + initialW.length() - 1;
                // if connect, then (pStartRow - 1 <= iStartRow <= pEndRow + 1) AND
                //                  (iStartCol - 1 <= pStartCol <= iEndCol + 1)
                if ((pStartRow - 1 <= iStartRow && iStartRow <= pEndRow + 1) && 
                    (iStartCol - 1 <= pStartCol && pStartCol <= iEndCol + 1))
                {
                    // within the range
                    if ((pStartRow <= iStartRow && iStartRow <= pEndRow) && 
                        (iStartCol <= pStartCol && pStartCol <= iEndCol))
                    {
                        // check the intersection part has the same letter or not
                        if (playW.charAt(iStartRow - pStartRow) != initialW.charAt(pStartCol - iStartCol))
                        {
                            //System.out.println("Invalid connection");
                            return false;
                        }
                    }
                    // boundary case
                    else if ((pStartRow <= iStartRow && iStartRow <= pEndRow) ||
                             (iStartCol <= pStartCol && pStartCol <= iEndCol))
                    {
                        String newWord;
                        
                        // intial word is the head of the new word
                        if (iStartRow == pStartRow - 1)
                            newWord = initialW.substring(pStartCol - iStartCol, pStartCol - iStartCol + 1) + playW;
                        // initial word is the tail of the new word
                        else if (iStartRow == pEndRow + 1)
                            newWord = playW + initialW.substring(pStartCol - iStartCol, pStartCol - iStartCol + 1);
                        // player word is the head of the new word
                        else if (pStartCol == iStartCol - 1)
                            newWord = playW.substring(iStartRow - pStartRow, iStartRow - pStartRow + 1) + initialW;
                        // player word is the tail of the new word
                        else
                            newWord = initialW + playW.substring(iStartRow - pStartRow, iStartRow - pStartRow + 1);
                        
                        //System.out.println(newWord);
                        if (!isInDictionary(newWord, dictionary))
                            return false;
                    }
                    else
                        return false;
                }
                // no connected to each other
                else
                {
                    //System.out.println("not connected to each other");
                    return false;
                }
            }
        }
        
        ///////////////////////////////////////////////////////////////////////////////
        // Invalid case 4: the playWord use letters that are not in availableLetters
        // Before checking, must remove the intersection part of the playWord and the starting word        
        // remove the intersection part
        String additionLetters = findAdditionLetters(initialWord, playWord);
        
        // use arraylist to store the letters
        ArrayList<String> playerLetters = new ArrayList<String>();        
        for (char i: additionLetters.toCharArray())
            playerLetters.add(Character.toString(i));
        
        ArrayList<String> validLetters = new ArrayList<String>();
        for (char i: availableLetters)
            validLetters.add(Character.toString(i));
        
        // check every letter in additionLetters
        for (int i = 0; i < additionLetters.length(); i++)
        {
            String tempChar = Character.toString(additionLetters.charAt(i));
            // if it is an available letter, remove it from both lists
            if (validLetters.contains(tempChar))
            {
                playerLetters.remove(tempChar);
                validLetters.remove(tempChar);
            }
        }
        // not empty means some letters that are not in availableLetters are used
        if (!playerLetters.isEmpty())
            return false;
        
        // otherwise, return true
        return true;
    }
    
    // check if a word is in the dictionary
    private static boolean isInDictionary(String word, ArrayList<String> dictionary)
    {
        // if not blank tile
        if (!word.contains("_"))
        {
            // if the word doesn't contain blank tile, directly check
            if (!dictionary.contains(word))
            {
                //System.out.println(word + " not in the dictionary.");
                return false;
            }
            else
                return true;
        }
        else
        {
            int indexOfBlank = word.indexOf("_");
            // try different letters (a-z)
            for (int i = 0; i <= 26; i++)
            {
                char trial = (char)(i + 65); // (int)'A' = 65
                // change blank to trial
                String testingWord = word.substring(0, indexOfBlank) + trial + 
                                     word.substring(indexOfBlank + 1, word.length());
                
                // check if this testingWord in dictionary
                // recursive call because it may have multiple blanks
                if (isInDictionary(testingWord, dictionary))
                    return true;
            }
            
            return false;
        }
    }
    
    
    /**
    * function to find the letters in the playWord that are added by the player
    * @param initialWord: a ScrabbleWord in the board at the beginning
    * @param playWord: a ScrabbleWord that the player wants to add
    * @return additionLetters: kind of playWord - initialWord
    */
    private static String findAdditionLetters(ScrabbleWord initialWord, ScrabbleWord playWord)
    {
        String additionLetters;
        String playerW = playWord.getScrabbleWord();
        String initialW = initialWord.getScrabbleWord();
        
        // find the intersection according to the orientation
        // same orientation, remove the whole initialWord from playWord
        if (playWord.getOrientation() == initialWord.getOrientation())
        {
            additionLetters = playerW.replace(initialW, "");
        }
        // different orientation
        else
        {
            int intersect = 0;
            if (playWord.getOrientation() == 'h')
                intersect = initialWord.getStartColumn() - playWord.getStartColumn();
            else
                intersect = initialWord.getStartRow() - playWord.getStartRow();
            // remove intersect letter
            additionLetters = playerW.substring(0, intersect) + playerW.substring(intersect + 1, playerW.length());
        }
        
        return additionLetters;
    }
    
    
    /**
     * Setup the board
     */
    private static ScrabbleWord generateBoard(char[][] board, ArrayList<String> dictionary, Random rand)
    {        
        // randomly choose a word
        int randomIndex = rand.nextInt(dictionary.size());
        String initialWord = dictionary.get(randomIndex);
        while (initialWord.length() > 7)
        {
            randomIndex = rand.nextInt(dictionary.size());
            initialWord = dictionary.get(randomIndex);
        }
        
        // choose the orientation and position, put the initial word onto the board
        boolean flipCoin = rand.nextBoolean();
        char orientation = 'h';
        int rowPos, colPos;
        // flipCoin == true, then horizontal
        if (flipCoin)
        {
            orientation = 'h';
            // randomly choose the rowPos
            rowPos = rand.nextInt(board.length);
            // since it is horizontal, colPos may not be 14
            // for example, if word.length = 6, then Max(colPos) = 9 = 15 - 6 + 1
            colPos = rand.nextInt(board[0].length - initialWord.length() + 2);
            // horizontal, so each letter has the same rowID
            for (int i = colPos; i < initialWord.length(); i++)
            {
                board[rowPos][i] = initialWord.charAt(i);
            }
        }
        // otherwise, vertical
        else
        {
            orientation = 'v';
            // similarly, colPos = 0 ~ 14, rowPos = 0 ~ (15 - word.length + 1)
            colPos = rand.nextInt(board[0].length);
            rowPos = rand.nextInt(board.length - initialWord.length() + 1);
            // vertical, so each letter has the same colID
            for (int i = rowPos; i < initialWord.length(); i++)
            {
                board[i][colPos] = initialWord.charAt(i);
            }
        }
        
        ScrabbleWord wordOnBoard = new ScrabbleWord(initialWord, rowPos, colPos, orientation);
        return wordOnBoard;
    }
    
    
    /**
    * Haoran: randomly pick up 7 letters to be the available letters
    *         according to the distribution on wiki page
    */
    private static void generateAvailableLetters(char[] availableLetters, Random rand)
    {
        // initial distribution (total 100 tiles):
        // blank:2 A:9 B:2 C:2 D:4 E:12 F:2 G:3 H:2 I:9 J:1 K:1 L:4 M:2
        //     N:6 O:8 P:2 Q:1 R:6 S:4  T:6 U:4 V:2 W:2 X:1 Y:2 Z:1
        int[] distribution = {2, 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2,
                              6, 8, 2, 1, 6, 4,  6, 4, 2, 2, 1, 2, 1};
        
        int tilesNum = 100;
        for (int i = 0; i < 7; i++)
        {
            int randomNum = rand.nextInt(tilesNum) + 1;
            tilesNum = tilesNum - 1;
            
            // according to the randomNum, find the character
            int cumulative = 0;
            int counter = 0;
            while (randomNum > cumulative)
            {
                cumulative += distribution[counter];
                counter++;
            }
            
            // counter - 1 is the actual index of distribution
            // distribution[counter - 1] should minus 1 because one tile has been picked
            distribution[counter - 1]--;
            
            // which letter is chosen
            if ((counter - 1) == 0)
                availableLetters[i] = '_';
            else
            {
                char letter = (char)((counter - 1) + 64);
                availableLetters[i] = letter;
            }
        }
    }
    

    /*
     * return peak memory usage in bytes
     *
     * adapted from

     * https://stackoverflow.com/questions/34624892/how-to-measure-peak-heap-memory-usage-in-java 
     */
    private static long peakMemoryUsage() 
    {

    List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
    long total = 0;
    for (MemoryPoolMXBean memoryPoolMXBean : pools)
        {
        if (memoryPoolMXBean.getType() == MemoryType.HEAP)
        {
            long peakUsage = memoryPoolMXBean.getPeakUsage().getUsed();
            // System.out.println("Peak used for: " + memoryPoolMXBean.getName() + " is: " + peakUsage);
            total = total + peakUsage;
        }
        }

    return total;
    }

}
