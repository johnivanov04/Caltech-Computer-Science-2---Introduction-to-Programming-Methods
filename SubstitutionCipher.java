package edu.caltech.cs2.project01;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubstitutionCipher {
    private String ciphertext;
    private Map<Character, Character> key;

    // Use this Random object to generate random numbers in your code,
    // but do not modify this line.
    private static final Random RANDOM = new Random();
    public static final char[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * Construct a SubstitutionCipher with the given cipher text and key.
     * We assume that every letter in ciphertext is an upper-case
     * alphabetic letter. We also assume every letter in the key is an
     * upper-case alphabetic letter.
     * @param ciphertext the cipher text for this substitution cipher
     * @param key the map from cipher text characters to plaintext characters
     */
    public SubstitutionCipher(String ciphertext, Map<Character, Character> key) {
        this.ciphertext = ciphertext;
        this.key = key;
    }

    /**
     * Construct a SubstitutionCipher with the given cipher text and a randomly
     * initialized key. We assume that every letter in ciphertext is an upper-case
     * alphabetic letter.
     * @param ciphertext the cipher text for this substitution cipher
     */
    public SubstitutionCipher(String ciphertext) {
        this.ciphertext = ciphertext;
        this.key = new HashMap<>();
        for (char c = 'A'; c <= 'Z'; c++){
            this.key.put(c, c);
        }

        for (int i = 0; i < 10000; i++){
            this.key = randomSwap().key;
        }

    }

    /**
     * Returns the unedited cipher text that was provided by the user.
     * @return the cipher text for this substitution cipher
     */
    public String getCipherText() {
        return this.ciphertext;
    }

    /**
     * Applies this cipher's key onto this cipher's text.
     * That is, each letter should be replaced with whichever
     * letter it maps to in this cipher's key. We assume that
     * every letter in the plain text is an upper-case alphabetic letter.
     * @return the resulting plain text after the transformation using the key
     */
    public String getPlainText() {
        String returnStr = "";
        for (int i = 0; i < this.ciphertext.length(); i++){
            char cipherC = this.key.get(ciphertext.charAt(i));
            returnStr += Character.toString(cipherC);
        }
        return returnStr;
    }

    /**
     * Returns a new SubstitutionCipher with the same cipher text as this one
     * and a modified key with exactly one random pair of characters exchanged.
     *
     * @return the new SubstitutionCipher
     */
    public SubstitutionCipher randomSwap() {

        int char1 = RANDOM.nextInt(key.size());
        int char2 = RANDOM.nextInt(key.size());
        while (char1 == char2){
            char2 = RANDOM.nextInt(key.size());
        }
        Map<Character, Character> newKey = new HashMap<>(key);

        char char1st = newKey.get(ALPHABET[char1]);
        char char2nd = newKey.get(ALPHABET[char2]);

        newKey.put(ALPHABET[char2], char1st);
        newKey.put(ALPHABET[char1], char2nd);

        return new SubstitutionCipher(this.ciphertext, newKey);

    }

    /**
     * Returns the "score" for the "plain text" for this cipher.
     * The score for each individual quadgram is calculated by
     * the provided likelihoods object. The total score for the text is just
     * the sum of these scores.
     * @param likelihoods the object used to find a score for a quadgram
     * @return the score of the plain text as calculated by likelihoods
     */
    public double getScore(QuadGramLikelihoods likelihoods) {
        double score = 0.0;
        String plain_text = getPlainText();
        for (int i = 0; i < plain_text.length() - 3; i++){
            String quadgram = plain_text.substring(i, i+4);
            score += likelihoods.get(quadgram);
        }
        return score;
    }

    /**
     * Attempt to solve this substitution cipher through the hill
     * climbing algorithm. The SubstitutionCipher this is called from
     * should not be modified.
     * @param likelihoods the object used to find a score for a quadgram
     * @return a SubstitutionCipher with the same ciphertext and the optimal
     *  found through hill climbing
     */
    public SubstitutionCipher getSolution(QuadGramLikelihoods likelihoods) {
        SubstitutionCipher C = new SubstitutionCipher(this.ciphertext);

        int trials = 0;
        while (trials < 1000) {
            SubstitutionCipher M = C.randomSwap();

            if (M.getScore(likelihoods) > C.getScore(likelihoods)) {
                C = M;
                trials = 0;
            } else {
                trials++;
            }
        }
        return C;
    }
}
