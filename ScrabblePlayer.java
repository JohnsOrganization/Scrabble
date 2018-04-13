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
    
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    // initialize ScrabblePlayer with a file of English words
    public ScrabblePlayer(String wordFile) throws FileNotFoundException
    {
        String[][] bonusHolder = new String[][]{
            {"3W", "  ", "  ", "2L", "  ", "  ", "  ", "3W", "  ", "  ", "  ", "2L","  ", "  ","3W"}, //0 done
            {"  ", "2W", "  ", "  ", "  ", "3L", "  ", "  ", "  ", "3L", "  ", "  ","  ", "2W","  "},       //1 done
            {"  ", "  ", "2W", "  ", "  ", "  ", "2L", "  ", "2L", "  ", "  ", "  ","2W", "  ","  "},       //2 done
            {"2L", "  ", "  ", "2W", "  ", "  ", "  ", "2L", "  ", "  ", "  ", "2W","  ", "  ","2L"},   //3 done
            {"  ", "  ", "  ", "  ", "2W", "  ", "  ", "  ", "  ", "  ", "2W", "  ","  ", "  ","  "},           //4 done
            {"  ", "3L", "  ", "  ", "  ", "3L", "  ", "  ", "  ", "3L", "  ", "  ","  ", "3L","  "},
            {"  ", "  ", "2L", "  ", "  ", "  ", "2L", "  ", "2L", "  ", "  ", "  ","2L", "  ","  "},
            {"3W", "  ", "  ", "2L", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "2L","  ", "  ","3W"},   //MIDDLE ROW
            {"  ", "  ", "2L", "  " , "  ",  "  ", "2L", "  ", "2L", "  ", "  ", "  ","2L", "  ","  "},
            {"  ", "3L", "  ", "  ", "  ", "3L", "  ", "  ", "  ", "3L", "  ", "  ","  ", "3L","  "},
            {"  ", "  ", "  ", "  ", "2W", "  ", "  ", "  ", "  ", "  ", "2W", "  ","  ", "  ","  "},           //4 done
            {"2L", "  ", "  ", "2W", "  ", "  ", "  ", "2L", "  ", "  ", "  ", "2W","  ", "  ","2L"},   //3 done
            {"  ", "  ", "2W", "  ", "  ", "  ", "2L", "  ", "2L", "  ", "  ", "  ","2W", "  ","  "},       //2 done
            {"  ", "2W", "  ", "  ", "  ", "3L", "  ", "  ", "  ", "3L", "  ", "  ","  ", "2W","  "},       //1 done
            {"3W", "  ", "  ", "2L", "  ", "  ", "  ", "3W", "  ", "  ", "  ", "2L","  ", "  ","3W"}, //0 done
            
    };
    
    for (int i = 0; i < 15; i++) {
        for (int j = 0; j< 15; j++) {
            System.out.print(bonusHolder[i][j]);
        }
        System.out.println();
    }
    

        Scanner dictFile = new Scanner(new File(wordFile));
        root = new Node(' ', null);
        while (dictFile.hasNext()) {
            
            String nextWord = dictFile.nextLine().toUpperCase();
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
        /*
        //parseDictionary();
        System.out.println(combinations.size());
        System.out.println(one.size());
        System.out.println(two.size());
        System.out.println(three.size());
        System.out.println(four.size());
        System.out.println(five.size());
        System.out.println(six.size());
        System.out.println(seven.size());
        System.out.println(other.size());
        */
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
        String temp = new String();
        for(int i = 0; i < availableLetters.length; i++) {
            temp = temp + availableLetters[i];
        }
        temp = temp.toUpperCase();
        availableLetters = temp.toCharArray();
        
        
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
        ScrabbleWord opponent = new ScrabbleWord(opponentWord, startRow, startCol, opponentOrientation);
        
        ///////////////////////////////////////////////
        //possibleStrings(availableLetters);
        //System.out.println(combinations.size());
        
        
        
        
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
        ArrayList<ArrayList<String>> validWordsPlusOppWord = new ArrayList<ArrayList<String>>(opponent.getScrabbleWord().length());
        for (int i = 0; i < opponent.getScrabbleWord().length(); i++) {
            //if our available letters contains a wildcard
            if (containsWildcard) {
                char[] improvLetters = new char[8];
                //Put our available letters into the new array
                for (int j = 0; j < availableLetters.length; j++) {
                    improvLetters[j] = availableLetters[j];
                }
                //Add opponent's letter to the new array
                improvLetters[7] = opponent.getScrabbleWord().charAt(i);
                //calculate all possibilities of the wildcard being any letter
                for (int k = 0; k < alphabet.length; k++) {
                    improvLetters[wildcardIndex] = alphabet[k];
                    ArrayList<String> results = enumerate(improvLetters, new ArrayList<String>());
                    validWordsPlusOppWord.add(results);
                }
                
                
            } else {
                char[] improvLetters = new char[8];
                for (int j = 0; j < availableLetters.length; j++) {
                    improvLetters[j] = availableLetters[j];
                }
                improvLetters[7] = opponent.getScrabbleWord().charAt(i);
                ArrayList<String> results = enumerate(improvLetters, new ArrayList<String>());
                validWordsPlusOppWord.add(results);
            }
            
        }
        
        
        
        //Prints pertinent information
        System.out.println(opponent.getScrabbleWord());
        for (int i = 0; i < availableLetters.length; i++) {
            System.out.println(availableLetters[i]);
        }
        System.out.printf("ArrayList size:%s%n", validWordsPlusOppWord.size());
        for (int i = 0; i < validWordsPlusOppWord.size(); i++) {
            //for (int j = 0; j < validWordsPlusOppWord.get(i))
            System.out.println(validWordsPlusOppWord.get(i).size());
        }
        //////////////////////////////////////////////////
        //System.out.println(opponent.getScrabbleWord());
        
        //Test for the checkValidity() method
        //String word = "ETHICS";
        //System.out.println(checkValidity(opponent.getScrabbleWord()));
        //ArrayList<String> allAvailableValidWords = enumerate(availableLetters, new ArrayList<String>());
        
        //System.out.println(allAvailableValidWords.size());
        
        System.out.println(opponent.getStartRow() + " " + opponent.getStartColumn());
        
        return  new ScrabbleWord("WON", 8, 4, 'h');
    }

    
    
    ArrayList<String> enumerate(char[] availLet, ArrayList<String> newValidWords) {
        
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