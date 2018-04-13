/*

  Authors (group members):
  Email addresses of group members:
  Group name:

  Course:
  Section:

  Description of the overall algorithm and key data structures:


*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ScrabblePlayer
{
    //Global fields
    Node root;
    static ArrayList<String> dictionary = new ArrayList<String>();
    public static ArrayList<String> one = new ArrayList<>();
    public static ArrayList<String> two= new ArrayList<>();
    public static ArrayList<String> three = new ArrayList<>();
    public static ArrayList<String> four = new ArrayList<>();
    public static ArrayList<String> five = new ArrayList<>();
    public static ArrayList<String> six= new ArrayList<>();
    public static ArrayList<String> seven  = new ArrayList<>();
    public static ArrayList<String> other  = new ArrayList<>();
    public static ArrayList<String> combinations = new ArrayList<>();
    static ArrayList<String> validWords = new ArrayList<>();
    
    // initialize ScrabblePlayer with a file of English words
    public ScrabblePlayer(String wordFile) throws FileNotFoundException
    {

        Scanner dictFile = new Scanner(new File(wordFile));
        root = new Node(' ', null);
        while (dictFile.hasNext()) {
            
            String nextWord = dictFile.nextLine().toUpperCase();
            dictionary.add(nextWord);
            if (nextWord.length() < 15) {
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
        
        parseDictionary();
        System.out.println(one.size());
        System.out.println(two.size());
        System.out.println(three.size());
        System.out.println(four.size());
        System.out.println(five.size());
        System.out.println(six.size());
        System.out.println(seven.size());
        System.out.println(other.size());
        
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
        
        /*
         * lines 76 - 101
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
        ScrabbleWord opponent = new ScrabbleWord(opponentWord, startRow, startCol, opponentOrientation);
        //System.out.println(opponent.getScrabbleWord());
        
        //Test for the checkValidity() method
        //String word = "ETHICS";
        //System.out.println(checkValidity(opponent.getScrabbleWord()));
        //ArrayList<String> allAvailableValidWords = enumerate(availableLetters, new ArrayList<String>());
        
        //System.out.println(allAvailableValidWords.size());
        
        
        
        return  new ScrabbleWord("MYWORD", 0, 0, 'h');
    }

    //parses the dictionary into size based arrays
    public static void parseDictionary() {
        int size;
        String word;
        for(int i = 0; i < dictionary.size(); i++) {
            size = dictionary.get(i).length();
            word =  dictionary.get(i);
            if(size==1) {
                one.add(word);
            } else  if(size==2) {
                two.add(word);
            } else  if(size==3) {
                three.add(word);
            } else  if(size==4) {
                four.add(word);
            } else  if(size==5) {
                five.add(word);
            } else  if(size==6) {
                six.add(word);
            } else  if(size==7) {
                seven.add(word);
            }else {
                other.add(word);
            }
            
        }
    }
    
    public static void getValidWords(int size, char[] availableLetters) {
        //gets all n letter combinations from the 7 letters
        //possibleStrings(N, availableLetters,"");
        possibleStrings(size, availableLetters,"");
        //Compares the possible combinations with valid words the adds the valid to list
        
        //words of size 1
        if(size == 1) {
            for(int i = 0; i < combinations.size(); i++) {
                if(one.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
        //words of size 2
        else if(size == 2) {
            for(int i = 0; i < combinations.size(); i++) {
                if(two.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
        //for words of size three
        else if(size == 3) {
            for(int i = 0; i < combinations.size(); i++) {
                if(three.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
        //words of 4
        else if(size == 4) {
            for(int i = 0; i < combinations.size(); i++) {
                if(four.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
      //words of 5
        else if(size == 5) {
            for(int i = 0; i < combinations.size(); i++) {
                if(five.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
      //words of 6
        else if(size == 6) {
            for(int i = 0; i < combinations.size(); i++) {
                if(six.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
      //words of 7
        else if(size == 7) {
            for(int i = 0; i < combinations.size(); i++) {
                if(seven.contains(combinations.get(i))) {
                    validWords.add(combinations.get(i));
                }
            }
            //Resets the combinations
            combinations = new ArrayList<>();
        }
    }
    
    //*************NEED TO EDIT*****************************
    public static void possibleStrings(int maxLength, char[] alphabet, String curr) {

        // If the current string has reached it's maximum length
        if(curr.length() == maxLength) {
            combinations.add(curr);

        // Else add each letter from the alphabet to new strings and process these new strings again
        } else {
            for(int i = 0; i < alphabet.length; i++) {
                String oldCurr = curr;
                curr += alphabet[i];
                possibleStrings(maxLength,alphabet,curr);
                curr = oldCurr;
            }
        }
    }
    
    /*
    ArrayList<String> enumerate(char[] availLet, ArrayList<String> validWords) {
        
        for (int a = 0; a < availLet.length; a++) {
            String aString = Character.toString(availLet[a]);
            for (int b = (a+1); b < availLet.length; b++) {
                String bString = aString + Character.toString(availLet[b]);
                for (int c = (b+1); c < availLet.length; c++) {
                    String cString = bString + Character.toString(availLet[c]);
                    for (int d = (c+1); d < availLet.length; d++) {
                        String dString = cString + Character.toString(availLet[d]);
                        for (int e = (d+1); e < availLet.length; e++) {
                            String eString = dString + Character.toString(availLet[e]);
                            for (int f = (e+1); f < availLet.length; f++) {
                                String fString = eString + Character.toString(availLet[f]);
                                for (int g = (f+1); g < availLet.length; g++) {
                                    String gString = fString + Character.toString(availLet[g]);
                                    
                                    if (checkValidity(gString)) {
                                        validWords.add(gString);
                                    }
                                }
                                System.out.println(fString);
                                if (checkValidity(fString)) {
                                    validWords.add(fString);
                                }
                            }
                            if (checkValidity(eString)) {
                                validWords.add(eString);
                            }
                        }
                        if (checkValidity(dString)) {
                            validWords.add(dString);
                        }
                    }
                    if (checkValidity(cString)) {
                        validWords.add(cString);
                    }
                }
                if (checkValidity(bString)) {
                    validWords.add(bString);
                }
            }
            if(checkValidity(aString)) {
                validWords.add(aString);
            }
        }
        
        
        return validWords;
    }
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
            //System.out.println();
        }
        return false;
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