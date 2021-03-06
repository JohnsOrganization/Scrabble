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
  Description of the overall algorithm and key data structures:
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ScrabblePlayer
{
    //Global fields
    Node root;
    public static Set<String> set = new HashSet<>();
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    private static final int[] LETTERS_SCORE =
        {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3,
         1, 1, 3, 10,1, 1, 1, 1, 4, 4, 8, 4, 10 };
    
    private static final char[] LETTERS =
        {'_', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
         'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        
    // initialize ScrabblePlayer with a file of English words
    public ScrabblePlayer(String wordFile) throws FileNotFoundException
    {

        Scanner dictFile = new Scanner(new File(wordFile));
        int ctr = 0;
        root = new Node(' ', null);
        while (dictFile.hasNext()) {
            
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
            
            
        }

        System.out.printf("ctr: %d%n", ctr);
        
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
        
        
        
        
    }

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

    
    public ScrabbleWord getScrabbleWord(char[][] board, char[] availableLetters)
    {

        printAvailableLetters(availableLetters);
        
        
        
        /*
         * CH:
         *  the board that is passed in has a random word from words.txt placed in a
         *  random position on the board. So scan through the board to determine the location,
         *  length, and orientation of the first word.
         */
        
        //Will print the board if uncommented, it will show you where the word is in the board and
        //what the orientation, start pos, and end pos should be.
        /*
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                System.out.print(board[row][col]);
            }
            System.out.println();
        }
        */
        
        //*************************************************************
        ScrabbleWord opponent = getOpponentWord(board);
        
        ///////////////////////////////////////////////
        //possibleStrings(availableLetters);
        //System.out.println(combinations.size());
        
        
        startEnumeration(availableLetters, opponent);
        

        //Determines the word with the maximum value
        ArrayList<String> validWords = new ArrayList<String>(set);
        
        
        MaxWord  determinedMaxWord = determineMaxWord(validWords);
        System.out.printf("LargestWord: %s%nValue: %d%n", determinedMaxWord.getWord(), determinedMaxWord.getValue());
        
        
        
        //Test to determine which row the word should be in
        StringBuilder availLetWord = determineOppLetUsed(availableLetters, determinedMaxWord);
        System.out.printf("availLetWord: %s%n", availLetWord.toString());
        
        
        return validateLocation(opponent, determinedMaxWord, availLetWord, validWords, availableLetters);
    }

    ScrabbleWord validateLocation(ScrabbleWord opponent, MaxWord determinedMaxWord, StringBuilder availLetWord, ArrayList<String> validWords, char[] availableLetters) {
        ScrabbleWord sWord = determineWordLocation(opponent, determinedMaxWord, availLetWord);
        
        StringBuilder availLetsCopy = new StringBuilder(availableLetters.toString());
        
        while (!validBoundary(sWord)) {
            validWords.remove(sWord.getScrabbleWord());
            MaxWord nextMaxWord = determineMaxWord(validWords);
            StringBuilder nextAvailLetWord = determineOppLetUsed(availableLetters, nextMaxWord);
            sWord = determineWordLocation(opponent, nextMaxWord, nextAvailLetWord);
        }
        
        //this is where the word gets passed in
        //we need to change the tile that was once changed to be a valid word,
        //back to being an underscore....
        StringBuilder opUsed = determineOppLetUsed(availableLetters, determinedMaxWord);
        ArrayList<Character> notFromAvailLet = new ArrayList<Character>();
        for (int i = 0; i < opUsed.length(); i++) {
        	notFromAvailLet.add(opUsed.charAt(0));
        }
        char intersection = ' ';
        
        if (notFromAvailLet.size() > 1) {
        	char[] oppWord = opponent.getScrabbleWord().toCharArray();
        	for (int i = 0; i < notFromAvailLet.size(); i++) {
        		for (int k = 0; k > oppWord.length; k++) {
                		if (oppWord[k] == notFromAvailLet.get(i)) {
                			intersection = notFromAvailLet.remove(i);
                		}
                		
        		}
        	}
        	
        for (int k = 0; k < availLetWord.length(); k ++) {
        	if (availLetWord.charAt(k) != intersection) {
        		availLetWord.deleteCharAt(k);
        	}
        }
        	
        
        String toReturn = determinedMaxWord.getWord().replace(notFromAvailLet.get(0),'_').toString();
        int indexOfIntersecting = toReturn.indexOf(intersection);
        char[] finalW = new char[toReturn.length()];
        int j = 0;
        for (int i = 0; i < toReturn.length(); i++) {
        	if (i == indexOfIntersecting) {
        		j = j + 1;
        	}
        	finalW[i] = toReturn.charAt(j);
        	j++;
        }
        String returning = finalW.toString();
        ScrabbleWord finalWord = new ScrabbleWord(toReturn, sWord.getStartRow(), sWord.getStartColumn(), sWord.getOrientation());
        System.out.println(availLetWord.charAt(0));
        finalWord = determineWordLocation(opponent, new MaxWord(finalWord.getScrabbleWord(), 14), availLetWord);
        
        System.out.println(finalWord.getScrabbleWord());
        System.out.println(finalWord.getStartRow());
        System.out.println(finalWord.getStartColumn());
        
        
        System.out.println("WORD BEING RETURNED TO OTHER: " + finalWord.getScrabbleWord());
        
        return finalWord;
        }
        else {
        	return sWord;
        }
        
        
    }
    
    boolean validBoundary(ScrabbleWord myWord) {
        if ((myWord.getStartRow() < 0) || (myWord.getStartColumn() < 0)) {
            return false;
        } else {
            return true;
        }
    }
    
    private void printAvailableLetters(char[] availableLetters) {
        for (int i = 0; i < availableLetters.length; i++) {
            System.out.printf("%d: %s%n", i, availableLetters[i]);
        }
        
    }

    void enumerate(ArrayList<Character> availLet, boolean[] visited, int position, String returnWord) {      
        visited[position] = true;
         returnWord = returnWord + availLet.get(position);
         if (checkValidity(returnWord)) {
             set.add(returnWord);
             if (returnWord.equals("SEQUOIA")) {
             System.out.println(returnWord + "!!!!");
             }
         }
         for (int i = 0; i < availLet.size(); i++) {
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
    ArrayList<String> enumerate(char[] availLet, ArrayList<String> newValidWords) {
        
        for(int i = 0; i < availLet.length; i++) {
            System.out.println(availLet[i]);
        }
        
        for (int a = 0; a < availLet.length; a++) {
            String aString = (Character.toString(availLet[a])).toUpperCase();
            if(checkValidity(aString)) {
                newValidWords.add(aString);
            }
            for (int b = 1; b < availLet.length; b++) {
                String bString = (aString + Character.toString(availLet[b])).toUpperCase();
                if (checkValidity(bString)) {
                    newValidWords.add(bString);
                }
                for (int c = 2; c < availLet.length; c++) {
                    String cString = (bString + Character.toString(availLet[c])).toUpperCase();
                    if (checkValidity(cString)) {
                        newValidWords.add(cString);
                    }
                    for (int d = 3; d < availLet.length; d++) {
                        String dString = (cString + Character.toString(availLet[d])).toUpperCase();
                        if (checkValidity(dString)) {
                            newValidWords.add(dString);
                        }
                        for (int e = 4; e < availLet.length; e++) {
                            String eString = (dString + Character.toString(availLet[e])).toUpperCase();
                            if (checkValidity(eString)) {
                                newValidWords.add(eString);
                            }
                            for (int f = 5; f < availLet.length; f++) {
                                String fString = (eString + Character.toString(availLet[f])).toUpperCase();
                                if (checkValidity(fString)) {
                                    newValidWords.add(fString);
                                }
                                for (int g = 6; g < availLet.length; g++) {
                                    String gString = (fString + Character.toString(availLet[g])).toUpperCase();
                                    if (checkValidity(gString)) {
                                        newValidWords.add(gString);
                                    }
                                    for (int m = 7; m < availLet.length; m++) {
                                        String mString = (gString + Character.toString(availLet[m])).toUpperCase();
                                        //System.out.println("");
                                        if (checkValidity(mString)) {
                                            newValidWords.add(mString);
                                        }
                                    }
                                    gString = fString;
                                }
                                fString = eString;
                            }
                            eString = dString;
                        }
                        dString = cString;
                    }
                    cString = bString;
                }
                bString = aString;
            }
        }
        
        
        return newValidWords;
    }
    */
    
    ScrabbleWord determineWordLocation(ScrabbleWord opponent, MaxWord determinedMaxWord, StringBuilder availLetWord) {
        int arrayListNum = 0;
        for (int i = 0; i < opponent.getScrabbleWord().length(); i++) {
            if (opponent.getScrabbleWord().charAt(i) == availLetWord.charAt(0)) {
                arrayListNum = i;
            }
        }
        System.out.printf("ArrayListNum: %d%n", arrayListNum);
        
        
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
                       // System.out.println("I WAS REACHED");
                        System.out.printf("i: %d%nj: %d%n%n", i, j);
                        System.out.println(opponent.getStartRow());
                        System.out.println("reached....j: " +j );
                        playerStartRow = (opponent.getStartRow() - j);
                        System.out.println(playerStartRow);
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
                        System.out.printf("i: %d%nj: %d", i, j);
                        playerStartCol = (opponent.getStartColumn() - j);
                        break;
                    }
                }
                if (found)
                    break;
            }
        }
        
        
        
        //////////////////////////////////////////////////
        //System.out.println(opponent.getScrabbleWord());
        
        //Test for the checkValidity() method
        //String word = "ETHICS";
        //System.out.println(checkValidity(opponent.getScrabbleWord()));
        //ArrayList<String> allAvailableValidWords = enumerate(availableLetters, new ArrayList<String>());
        
        //System.out.println(allAvailableValidWords.size());
        //System.out.printf("pStartRow: %s%npStartCol: %s%n", playerStartRow, playerStartCol);
        //return  new ScrabbleWord(maxWord, playerStartRow, playerStartCol, pOrientation);
        
        
        System.out.printf("Word: %s%nStartRow: %d%nStartCol: %d%nOrientation: %s%n", determinedMaxWord.getWord(), playerStartRow, playerStartCol, pOrientation);
        return new ScrabbleWord(determinedMaxWord.getWord(), playerStartRow, playerStartCol, pOrientation);
    }
    
    void startEnumeration(char[] availableLetters, ScrabbleWord opponent) {
      //Determines if there is a wildcard in the availableLetters
        boolean containsWildcard = false;
        int wildcardIndex = -1;
        for (int j = 0; j < availableLetters.length; j++) {
            if (availableLetters[j] == '_') {
                containsWildcard = true;
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
            
            //containsWildcard = false;
            
            char [] wildCardAlph = {'Q', 'Z', 'J', 'X', 'K'};
            if (containsWildcard) {
                //calculate all possibilities of the wildcard being any letter
                for (int k = 0; k < wildCardAlph.length; k++) {
                    improvLetters.set(wildcardIndex, wildCardAlph[k]);
                    for (int p = 0; p < availableLetters.length; p++) {
                    	//for (char e : improvLetters) {
                    	//	System.out.print(e);
                    	//}
                    	//System.out.println();
                    	enumerate(improvLetters, new boolean[improvLetters.size()], 0, "");
                        improvLetters.add(improvLetters.remove(0));  
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
        return availLetWord;
    }
    
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
                System.out.print(board[row][col]);
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
            System.out.println();
        }
        System.out.printf("Start row: %d%n", startRow);
        System.out.printf("Start col: %d%n", startCol);
        return new ScrabbleWord(opponentWord, startRow, startCol, opponentOrientation);
    }
    
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
        
        //here we need to check if there is a wild card so
        //that if there is, we can send it back without the wildcard filled
        //if the wildcard is filled then eval scrabble player 
        //sends an error bc we are trying to use words that are not in avail letters
        //OR we need to change the available letters...
        
        
        System.out.println(maxWord + " is max word....");
        System.out.println("maxWord score " + maxWordScore);
        return new MaxWord(maxWord, maxWordScore);
    }
    
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
            //System.out.println();
        }
        return false;
    }
    
    static class MaxWord {
        String word;
        int value;
        //Try ArrayList.remove(index) vs. ArrayList.remove(Object) to see which is faster
        
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