package edu.caltech.cs2.project02.choosers;


import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomHangmanChooser implements IHangmanChooser {
  private final String chosen_word;
  private SortedSet<Character> ts = new TreeSet<>();
  private final static Random rand = new Random();
  private int guessCount;

//
  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
      if (wordLength < 1 || maxGuesses < 1){
        throw new IllegalArgumentException();
      }

      //ts = this.ts;
      guessCount = maxGuesses;

      SortedSet<String> posWords = new TreeSet<>();
      Scanner scan = new Scanner(new File("data/scrabble.txt"));
      while (scan.hasNext()){
          String next = scan.next();
          if (next.length() == wordLength){
              posWords.add(next);
          }
      }
      if (posWords.isEmpty()){
          throw new IllegalStateException();
      }

      int randomI = rand.nextInt(posWords.size());



      List<String> posList = new ArrayList<String>(posWords);
      chosen_word = posList.get(randomI);

  }
  @Override
  public int makeGuess(char letter) {
      if (this.ts.contains(letter)){
          throw new IllegalArgumentException();
      }
      if (!Character.isLowerCase(letter)){
          throw new IllegalArgumentException();
      }

      if (guessCount < 1){
        throw new IllegalStateException();
      }




    this.ts.add(letter);
    boolean matchCheck = false;
    int matches = 0;
    for (int i = 0; i < this.chosen_word.length(); i++){
      if (chosen_word.charAt(i) == letter){
        matches += 1;
        matchCheck = true;
      }
    }
    if (!matchCheck){
        this.guessCount -= 1;
    }

    return matches;
  }


  //Done
  @Override
  public boolean isGameOver() {
    if (getGuessesRemaining() == 0){
      return true;
    }
    if (!getPattern().contains("-")){
        return true;
    }
    return false;
  }


  //Done
  @Override
  public String getPattern() {
      String retStr = "";
      for (int i = 0; i < this.chosen_word.length(); i++){
        if (getGuesses().contains(this.chosen_word.charAt(i))){
            retStr += this.chosen_word.charAt(i);
        }
        else{
            retStr += "-";
        }
      }
      return retStr;
  }

  //Done
  @Override
  public SortedSet<Character> getGuesses() {
    return this.ts;
  }


  //Done
  @Override
  public int getGuessesRemaining() {
    return this.guessCount;
  }


  //Done
  @Override
  public String getWord() {
      this.guessCount = 0;
      return this.chosen_word;
  }
}