/*
  Authors (group members):
      Andrea Swanson (T)
      John Linn
      Cameron Haupt
  Email addresses of group members:
      aswanson2016@fit.edu
      jlinn2016@fit.edu
      chaupt2013@fit.edu
  Group name: 14b  
  Course: CSE2010
  Section: 01
 
  Description of the overall algorithm and key data structures: Class initializes dictionary as a trie in the constructor
  Trie efficiently stores the letters used in all dictionary words
  Word enumerations are stored in a hash set to cancel duplicates and allow for easy access
  Numerous values of Chars and strings are stored in arrays when size does not change
  Algorithms used include those for enumerating all possible words, and 
  finding the best word to used on the board both have an essence of recursion
  
  Algorithms and structures used took into account both time and space complexity
  when determining which to use
  
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ScrabblePlayer
{
    //Global field Variables
    //Initializes the root of the trie
    Node root;
    //Set for all enumerated words
    public static Set<String> set = new HashSet<>();
    //Char Alphabet 
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    //Letter scores for scrabble tiels
    private static final int[] LETTERS_SCORE =
        {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3,
         1, 1, 3, 10,1, 1, 1, 1, 4, 4, 8, 4, 10 };
    //Uppercase Alphabet
    private static final char[] LETTERS =
        {'_', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
         'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        
    // initialize ScrabblePlayer with a file of English words in << 5 Minutes
    public ScrabblePlayer(String wordFile) throws FileNotFoundException
    {
        //create scanner for the dictionary
        Scanner dictFile = new Scanner(new File(wordFile));
        int ctr = 0;
        root = new Node(' ', null);
        //While not at EOF 
        while (dictFile.hasNext()) {
            //read in one line at a time
            String nextWord = dictFile.nextLine().toUpperCase();
            if (nextWord.length() < 9) {
                ctr++;
                ArrayList<Node> children = root.getChildren();
                for (int charCtr = 0; charCtr < nextWord.length(); charCtr++) {
                    //Nothing has been added to the root
                    if (root.getChildren() == null) {
                        root.appendChild(new Node(nextWord.charAt(0), nextWord));
                    } else {
                        //Chilren of root is not equal to null

                        //Bolean flag to see if the char is already a child
                        boolean doesExist = false;
                        for (int childCtr = 0; childCtr < children.size(); childCtr++) {
                            if (nextWord.charAt(charCtr) == children.get(childCtr).getLetter()) {
                                if (charCtr == (nextWord.length() - 1)) {
                                    
                                    children.get(childCtr).validWord = nextWord;
                                }
                                children = children.get(childCtr).getChildren();
                                doesExist = true;
                                break;
                            }
                        }
                        
                        //If the character is not a child yet, add it
                        if (!doesExist) {
                            if (charCtr == (nextWord.length() - 1)) {
                                children.add(new Node (nextWord.charAt(charCtr), nextWord));
                            } else {
                                children.add(new Node (nextWord.charAt(charCtr), null));
                            }
                            //Get the children of the node that was just added
                            children = children.get(children.size()-1).getChildren();                      
                        }  
                    }
                }
            }
            
            
        } // End of scanner reading

        //************************************************************************CHANGES********************************
        //ArrayList<Node> children = root.getChildren().get(25).getChildren().get(0).getChildren();
        //for (Node e : children) {
        //    System.out.println(e.getWord());
        //}
        /*
         * From the pdf file: "The constructor allows you to process the data and construct data structures in preparation for identifying words"
         * I belive our goal is to build data structures in the constructor in under 5 minutes, that will then allow us to generate the best
         * word in under 1 second.
         */
        
        
        
        
    }// End of the Constructor
    
    // TO-Do

    // based on the board and available letters, 
    //    return a valid word with its location and orientation
    //    See ScrabbleWord.java for details of the ScrabbleWord class 
    //
    // board: 15x15 board, each element is an UPPERCASE letter or space;
    //    a letter could be an underscore representing a blank (wildcard);
    //    first dimension is row, second dimension is column
    //    ie, board[row][col]     
    //    row 0 is the top row; col 0 is the leftmost column
    // 
    // availableLetters: a char array that has seven letters available
    //    to form a word
    //    a blank (wildcard) is represented using an underscore '_'
    //

    
    //finds the best scrabble word to play in the game
    //Parameters: 2D char array of initial game board, char array of availble 7 letters
    public ScrabbleWord getScrabbleWord(char[][] board, char[] availableLetters)
    {
        //Strictly used for testing to show letters availible
        //printAvailableLetters(availableLetters);
        
        
        
        /*
         *  the board that is passed in has a random word from words.txt placed in a
         *  random position on the board. So scan through the board to determine the location,
         *  length, and orientation of the first word.
         */
        
        /*
         * calls getOpponentWord(char[][] board) 
         * Mehtod turns word from board into a scrabble word
         * Scrabble word has string, row start, col start, and orientation
         * returns scrabble word
         */
        ScrabbleWord opponent = getOpponentWord(board);
        
        
        /*
         * calls startEnumeration(availableLetters, opponent);
         * Method takes given 7 letters and searches for wildcards
         * for wild cards forms multiple strings with each possible letter for underscore tile
         * Otherwise forms a string of letters and calls the enumerate method on the sequence
         */
        startEnumeration(availableLetters, opponent);
        

        /*
         * changes the set of valid words to an array for compatibility
         */
        ArrayList<String> validWords = new ArrayList<String>(set);
        
        /*
         * Uses MaxWord object to hold max word
         * Method determines the highest scoring word in the valid list
         */
        MaxWord  determinedMaxWord = determineMaxWord(validWords);
       //Test Print
        //System.out.printf("LargestWord: %s%nValue: %d%n", determinedMaxWord.getWord(), determinedMaxWord.getValue());
        
        
        
        /*
         * Mehtod determineOppLetUsed();
         * Finds which letter the AI scrabble word should play off of in the game
         */
        StringBuilder availLetWord = determineOppLetUsed(availableLetters, determinedMaxWord);
        //TestPrint
        //System.out.printf("availLetWord: %s%n", availLetWord.toString());
        
        
        
        
        /*
         * Returns the scrabble word to be evaluaed in the EvalScrabblePLayer.java class
         * Method Validate location ensures that the scrabble word starts and ends on the board
         * then returns the strongest word
         */
        return validateLocation(opponent, determinedMaxWord, availLetWord, validWords, availableLetters);
    }

    /*
     * Method takes in:
     * Opponets word, the MaxWord, the availible letters, all Valid words
     * Returns the best word that fits on the board
     */
    ScrabbleWord validateLocation(ScrabbleWord opponent, MaxWord determinedMaxWord, StringBuilder availLetWord, ArrayList<String> validWords, char[] availableLetters) {
        ScrabbleWord sWord = determineWordLocation(opponent, determinedMaxWord, availLetWord);
        while (!validBoundary(sWord)) {
            validWords.remove(sWord.getScrabbleWord());
            MaxWord nextMaxWord = determineMaxWord(validWords);
            StringBuilder nextAvailLetWord = determineOppLetUsed(availableLetters, nextMaxWord);
            sWord = determineWordLocation(opponent, nextMaxWord, nextAvailLetWord);
        }
        
        return sWord;
    }
    
    /*
     * Method takes in a scrabble word
     * returns true if it fits on the board
     * false if the word goes off the board
     * Rows and Cols are 0-15
     */
    boolean validBoundary(ScrabbleWord myWord) {
        if ((myWord.getStartRow() < 0) || (myWord.getStartColumn() < 0)) {
            return false;
        } else if(myWord.getOrientation()=='h' && myWord.getStartColumn() + myWord.getScrabbleWord().length()-1 > 14) {
            return false;
        } else if(myWord.getOrientation()=='v' && myWord.getStartRow() + myWord.getScrabbleWord().length()-1 > 14) {
            return false;
        } else {
            return true;
        }
    }
    
    /*
     * Test method used to print the letters 
     */
    @SuppressWarnings("unused")
    private void printAvailableLetters(char[] availableLetters) {
        for (int i = 0; i < availableLetters.length; i++) {
            //System.out.printf("%d: %s%n", i, availableLetters[i]);
        }
        
    }

    /*
     * Enumeration method take parameters:
     * ArrayList of availible letters, boolean for visited, position, the word
     */
    void enumerate(ArrayList<Character> availLet, boolean[] visited, int position, String returnWord) {      
        visited[position] = true;
         returnWord = returnWord + availLet.get(position);
         //if the word is valid add it to the set
         if (checkValidity(returnWord)) {
             set.add(returnWord);
         }
         for (int i = 0; i < availLet.size(); i++) {
             //if the word hasn't been visited recursively call enumerate
             if (!visited[i]) {
                 enumerate(availLet, visited, i, returnWord);
             }
             }
         
         if (returnWord.length() != 0) {
         returnWord = returnWord.substring(returnWord.length() - 1);
         }
             visited[position] = false;
     }
    
    /*
     * determineWordLocation method takes parameters:
     * opponent's word, the MaxWord, string builder
     */
    ScrabbleWord determineWordLocation(ScrabbleWord opponent, MaxWord determinedMaxWord, StringBuilder availLetWord) {
        int arrayListNum = 0;
        for (int i = 0; i < opponent.getScrabbleWord().length(); i++) {
            if (opponent.getScrabbleWord().charAt(i) == availLetWord.charAt(0)) {
                arrayListNum = i;
            }
        }
        //TEst Print
        //System.out.printf("ArrayListNum: %d%n", arrayListNum);
        
        
        //Determine the position that the word should be in
        int playerStartRow = -1;
        int playerStartCol = -1;
        boolean found = false;
        char pOrientation;
        if (opponent.getOrientation() == 'h') {
            pOrientation = 'v';
            playerStartCol = opponent.getStartColumn() + arrayListNum;
            for (int i = 0; i < opponent.getScrabbleWord().length(); i++) {
                for (int j = 0; j < determinedMaxWord.getWord().length(); j++) {
                    if (determinedMaxWord.getWord().charAt(j) == availLetWord.charAt(0)) {
                        found = true;
                        playerStartRow = (opponent.getStartRow() - j);
                        break;
                    }
                }
                if (found)
                    break;
            }
        } else {
            pOrientation = 'h';
            playerStartRow = opponent.getStartRow() + arrayListNum;
            for (int i = 0; i < opponent.getScrabbleWord().length(); i++) {
                for (int j = 0; j < determinedMaxWord.getWord().length(); j++) {
                    if (determinedMaxWord.getWord().charAt(j) == availLetWord.charAt(0)) {
                        found = true;
                        playerStartCol = (opponent.getStartColumn() - j);
                        break;
                    }
                }
                if (found)
                    break;
            }
        }
        //Test Print
        //System.out.printf("Word: %s%nStartRow: %d%nStartCol: %d%nOrientation: %s%n", determinedMaxWord.getWord(), playerStartRow, playerStartCol, pOrientation);
        //Returns a scrabble word at the particular location
        return new ScrabbleWord(determinedMaxWord.getWord(), playerStartRow, playerStartCol, pOrientation);
    }
    
    /*
     * startEnumeration method takes parameters:
     * availible letters, opponent word
     * Starts the enumeration of all possible strings
     * has check for wild cards
     */
    
    //////////////////////////////////////////////////    //////////////////////////////////////////////////
    void startEnumeration(char[] availableLetters, ScrabbleWord opponent) {
      //Determines if there is a wildcard in the availableLetters
        boolean containsWildcard = false;
        int wildcardIndex = -1;
        for (int j = 0; j < availableLetters.length; j++) {
            if (availableLetters[j] == '_') {
                wildcardIndex = j;
            }
        }
        
        //Add each latter from the opponent's word into the calculation of valid words         
        for (int i = 0; i < opponent.getScrabbleWord().length(); i++) {
            //if our available letters contains a wildcard
            ArrayList<Character> improvLetters = new ArrayList<Character>();
            for (int j = 0; j < availableLetters.length; j++) {
                improvLetters.add(availableLetters[j]);
            }
            improvLetters.add(opponent.getScrabbleWord().charAt(i));
            if (containsWildcard) {
                char[] wildAlpha = {'Q','Z', 'J', 'X', 'K'};
                //calculate all possibilities of the wildcard being any letter
                for (int k = 0; k < wildAlpha.length; k++) {
                    improvLetters.set(wildcardIndex, wildAlpha[k]);
                    for (int p = 0; p < availableLetters.length; p++) {
                        improvLetters.add(improvLetters.remove(0));
                        enumerate(improvLetters, new boolean[improvLetters.size()], 0, "");
                    }
                    
                }
                
                
            } else {
                
                enumerate(improvLetters, new boolean[improvLetters.size()], 0, "");
                for (int k = 0; k < availableLetters.length; k++) {
                    improvLetters.add(improvLetters.remove(0));
                    enumerate(improvLetters, new boolean[improvLetters.size()], 0, "");
                }
            }
            
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /*
     * determineOppLetUsed takes parameters:
     * char array of availible letters, and the max word
     * method finds the letter in the opponents 
     */
    StringBuilder determineOppLetUsed(char[] availableLetters, MaxWord determinedMaxWord) {
        StringBuilder availLetWord = new StringBuilder("");
        for (int i = 0; i < determinedMaxWord.getWord().length(); i++) {
            availLetWord.append(determinedMaxWord.getWord().charAt(i));
        }
        for (int i = 0; i < availableLetters.length; i++) {
            for (int j = 0; j < availLetWord.length(); j++) {
                if (availableLetters[i] == availLetWord.charAt(j)) {
                    availLetWord.deleteCharAt(j);
                    break;
                }
            }
        }
        //returns the letter in the form of a string builder of the word
        return availLetWord;
    }
    
    /*
     * Searches through the board and parses the 2D array for the word
     * Returns a scrabble word with word start row, start col, ad orientation
     */
    ScrabbleWord getOpponentWord(char[][] board) {
        /*
         * determines what and the opponent's word is, the orientation, and where it is located on the board
         * Then it creates that words as a ScrabbleWord object to hold all that information.
         */
        String opponentWord = "";
        char opponentOrientation = 'n';
        int startRow = -1;
        int startCol = -1;
        //Iterate through the rows of the board
        for (int row = 0; row < board.length; row++) {
            //Iterate through the cols of each row in the board
            for (int col = 0; col < board[0].length; col++) {
                //****************
                //System.out.print(board[row][col]);
                if (Character.getNumericValue(board[row][col]) != -1) {
                    opponentWord = opponentWord + board[row][col];
                    
                    //If the word is empty, determine the orientation
                    if (opponentWord.length() == 1) {
                        startRow = row;
                        startCol = col;
                        //Accounts for if word is on the border
                        if (((row + 1) < board.length) && (Character.getNumericValue(board[row + 1][col]) != -1)) {
                            opponentOrientation = 'v';
                        } else if (((col + 1) < board[0].length) && (Character.getNumericValue(board[row][col + 1]) != -1)) {
                            opponentOrientation = 'h';
                        }
                    }
                }
            }
            
        }
      
        return new ScrabbleWord(opponentWord, startRow, startCol, opponentOrientation);
    }
    
    /*
     * Finds the max word in the list of valid words
     * returns in the form of a MAxWord with string and value
     */
    MaxWord determineMaxWord(ArrayList<String> paramValidWords) {
        int maxWordScore = 0;
        String maxWord = "";
        for (int i = 0; i < paramValidWords.size(); i++) {
            int currentWordScore = 0;
            
            for (int k = 0; k < paramValidWords.get(i).length(); k++) {
                char letterInWord = paramValidWords.get(i).charAt(k);
                
                for (char tempChar: LETTERS)
                {
                    if (tempChar == letterInWord)
                        currentWordScore += LETTERS_SCORE[k];
                }
            }
            if (currentWordScore > maxWordScore) {
                maxWordScore = currentWordScore;
                maxWord = paramValidWords.get(i);
            }
        }
        //return the word
        return new MaxWord(maxWord, maxWordScore);
    }
    
    /*
     * checks to make sure that the words are in the dictionary
     * goes through trie created in the constructor and compares to the potential word
     */
    boolean checkValidity(String testWord) {
        ArrayList<Node> currentChildren = root.getChildren();
        //Node current = currentChildren.get(0);
        for (int i = 0; i < testWord.length(); i++) {
            //System.out.printf("%s:", testWord.charAt(i));
            INNER_LOOP:
            for (int j = 0; j < currentChildren.size(); j++) {
                //System.out.print(currentChildren.get(j).getLetter());
                if (testWord.charAt(i) == currentChildren.get(j).getLetter()) {
                    //System.out.println(currentChildren.get(j).getLetter());
                    if ((currentChildren.get(j).getWord() != null) && currentChildren.get(j).getWord().equals(testWord)) {
                        //System.out.println();
                        return true;
                    } else {
                        currentChildren = currentChildren.get(j).getChildren();
                        break INNER_LOOP;
                    }
                }
            }
        }
        return false;
    }
    
    /*
     * Nested class for a MaxWord Object
     * has fields:
     * Word, value
     */
    static class MaxWord {
        String word;
        int value;
        
        MaxWord(String newWord, int newValue) {
            word = newWord;
            value = newValue;
        }
        
        String getWord() {
            return word;
        }
        
        int getValue() {
            return value;
        }
    }
    
    /*
     * Nested class for nodes used in the trie
     * has fields valid word, letter, child list
     */
    static class Node {
        String validWord;
        char letter;
        ArrayList<Node> children;
        
        Node(char newLetter, String newValidWord) {
            validWord = newValidWord;
            letter = newLetter;
            children = new ArrayList<Node>();
        }
        
        String getWord() {
            return validWord;
        }
        
        char getLetter() {
            return letter;
        }
        
        ArrayList<Node> getChildren() {
            return children;
        }
        
        void appendChild(Node newChild) {
            children.add(newChild);
        }
    }     
}