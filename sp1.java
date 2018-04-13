
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
    static ArrayList<String> dictionary;
    static ArrayList<String> validWords = new ArrayList<>();
    public static ArrayList<String> combinations = new ArrayList<>();
    
    
    public static ArrayList<String> one = new ArrayList<>();
    public static ArrayList<String> two= new ArrayList<>();
    public static ArrayList<String> three = new ArrayList<>();
    public static ArrayList<String> four = new ArrayList<>();
    public static ArrayList<String> five = new ArrayList<>();
    public static ArrayList<String> six= new ArrayList<>();
    public static ArrayList<String> seven  = new ArrayList<>();
    public static ArrayList<String> other  = new ArrayList<>();

    
    
    
    
    // initialize ScrabblePlayer with a file of English words
    public ScrabblePlayer(String wordFile) throws FileNotFoundException
    {
        //Read in the word file and store the created dictionary in the "dictionary" global field
        @SuppressWarnings("resource")
        Scanner dictFile = new Scanner(new File(wordFile));
        dictionary = new ArrayList<>();
        while (dictFile.hasNext()) {
            dictionary.add(dictFile.nextLine().toUpperCase());
        }
        //Parse the words
        parseDictionary();
        System.out.println(one.size());
        System.out.println(two.size());
        System.out.println(three.size());
        System.out.println(four.size());
        System.out.println(five.size());
        System.out.println(six.size());
        System.out.println(seven.size());
        System.out.println(other.size());
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
        
        practice(board, availableLetters);
        
        return  new ScrabbleWord("MYWORD", 0, 0, 'h');
    }

    //Gets all combinations of the letters for a certain size

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
    
    //Just to test stuff out called from the getScrabbleWord
    //for time information
    public static void practice(char[][] board, char[] availableLetters) {
        //Prints out the 7 availible letters
        for(int i = 0; i < availableLetters.length; i++) {
            System.out.print(availableLetters[i] + " ");
        }
        System.out.println();
        
        StringBuilder boardWord = new StringBuilder();
        //Goes through the board and gets the word
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                //System.out.print(board[i][j]);
                if(board[i][j]!= ' ') {
                    boardWord.append(board[i][j]);
                }
            }
        }//prints the word
        System.out.println(boardWord);
        
        for(int i = 1; i<=4; i++) {
            getValidWords(i,availableLetters);
        }
        //prints valid words and size
        System.out.println(validWords.toString());
        System.out.println(validWords.size());
       
        /*/Prints out the combinations
        for(int i = 0; i< combinations.size(); i++) {
            System.out.println(combinations.get(i));
        }
        //*/
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
    

} 