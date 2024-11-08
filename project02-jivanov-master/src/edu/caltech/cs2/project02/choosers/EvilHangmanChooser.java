package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class EvilHangmanChooser implements IHangmanChooser {

  private SortedSet<Character> wordsGuessed = new TreeSet<>();
//
  private List<String> best_family = new ArrayList<>();
  private String best_pattern;

  private int guessCount;
    
  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    if (wordLength < 1 || maxGuesses < 1){
      throw new IllegalArgumentException();
    }

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
    this.best_pattern = "";
    for (int i = 0; i < wordLength; i++){
      this.best_pattern += "-";
    }
    List<String> pos = new ArrayList<>(posWords);
    this.best_family = pos;
  }

  @Override
  public int makeGuess(char letter) {
    if (this.wordsGuessed.contains(letter)){
      throw new IllegalArgumentException();
    }

    if (!Character.isLowerCase(letter)){
      throw new IllegalArgumentException();
    }

    if (guessCount < 1){
      throw new IllegalStateException();
    }

    this.wordsGuessed.add(letter);
    Map<String, List<String>> families = new TreeMap<>();
    families.put(this.best_pattern, this.best_family);
    List<String> containsL = new ArrayList<>();

    for (String word : this.best_family) {
      if (word.indexOf(letter) >= 0) {
        containsL.add(word);
      }
    }
    for (String word : containsL){
      this.best_family.remove(word);
      families.replace(this.best_pattern, this.best_family);
    }
    for (String word : containsL){
      List<String> temp;
      boolean aAdd = false;
      for (String key : families.keySet()){
        String patternC = "";
        for (char c : word.toCharArray()){
          if (wordsGuessed.contains(c)){
            patternC += c;
          }
          else {
            patternC += "-";
          }
        }
        if (key.equals(patternC)){
          temp = families.get(key);
          temp.add(word);
          families.replace(key, temp);
          aAdd = true;
        }
      }
      if (!aAdd){
        String pattern = "";
        List<String> temp2 = new ArrayList<>();
        temp2.add(word);
        for (char c : word.toCharArray()){
          if (wordsGuessed.contains(c)){
            pattern += Character.toString(c);
          }
          else{
            pattern += "-";
          }
        }
        families.put(pattern, temp2);
      }
    }
    int mFSize = 0;
    String pBest = this.best_pattern;
    for (String key : families.keySet()){
      int fSize = families.get(key).size();
      if (fSize > mFSize){
        this.best_pattern = key;
        this.best_family = families.get(this.best_pattern);
        mFSize = fSize;
      }
      else if (families.get(key).size() == mFSize){
        if (this.best_pattern.compareTo(key) > 0){
          this.best_pattern = key;
          this.best_family = families.get(this.best_pattern);
          mFSize = fSize;
        }
      }
    }
    int occurences = 0;
    for (int i = 0; i < this.best_pattern.length(); i++) {
      if (pBest.charAt(i) == '-' && this.best_pattern.charAt(i) != '-') {
        occurences++;
      }
    }
    if (occurences == 0) {
      this.guessCount--;
    }
    return occurences;
  }

  @Override
  public boolean isGameOver() {
    if (!this.best_pattern.contains("-")){
      return true;
    }
    else if (this.guessCount == 0){
      return true;
    }
    return false;
  }

  @Override
  public String getPattern() {
    return this.best_pattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.wordsGuessed;
  }

  @Override
  public int getGuessesRemaining() {
    return this.guessCount;
  }

  @Override
  public String getWord() {
    this.guessCount = 0;
    return this.best_family.get(0);
  }
}