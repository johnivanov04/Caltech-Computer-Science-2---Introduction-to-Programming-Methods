package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {
  private static final String dictionary = "data/scrabble.txt";
  @Override
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {
    SortedSet<String> wordCollection = new TreeSet<>();
    Scanner scan = new Scanner(new File(dictionary));
    String patternC;
    while (scan.hasNext()){
      patternC = "";
      String next = scan.next();
      if (next.length() == pattern.length()){
        for (char character : next.toCharArray()){
          if (guesses.contains(character)){
            patternC += Character.toString(character);
          }
          else{
            patternC += "-";
          }
        }
        if (pattern.equals(patternC)){
          wordCollection.add(next);
        }
      }
    }
//
    Map<Character, Integer> unguessCharacters = new TreeMap<>();
    for (char i = 'a'; i < 'z'; i++){
      if (!guesses.contains(i)){
        unguessCharacters.put(i,0);
      }
    }
    for (String word : wordCollection){
      for (char let : word.toCharArray()){
        if (unguessCharacters.containsKey(let)){
          int num = unguessCharacters.get(let) + 1;
          unguessCharacters.replace(let, num);
        }
        else if (!guesses.contains(let)){
          unguessCharacters.put(let, 0);
        }
      }
    }
    char returnLetter = 123;
    int highestFreq = -1;
    for (char key : unguessCharacters.keySet()){
      if (unguessCharacters.get(key) > highestFreq){
        highestFreq = unguessCharacters.get(key);
        returnLetter = key;
      }
      else if (unguessCharacters.get(key) == highestFreq){
        if (key < returnLetter){
          returnLetter = key;
        }
      }
    }
    return returnLetter;
  }
}
