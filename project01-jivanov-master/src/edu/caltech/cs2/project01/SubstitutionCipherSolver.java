package edu.caltech.cs2.project01;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;

public class SubstitutionCipherSolver {

    public static void replacement() throws IOException{
        BufferedReader read = new BufferedReader(new FileReader("cryptogram.txt"));
        BufferedWriter write = new BufferedWriter(new FileWriter("plaintext.txt"));
        String text;
        while ((text = read.readLine()) != null) {
            String upper = text.replaceAll("[^A-Z]", "");
            write.write(upper);
        }
        read.close();
        write.close();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Type a sentence to decrypt: ");
        String ciphertext = scan.nextLine();

        QuadGramLikelihoods likelihoods = new QuadGramLikelihoods();
        SubstitutionCipher best = new SubstitutionCipher(ciphertext);
        for (int i = 0; i < 20; i ++) {
            SubstitutionCipher cipher = best.getSolution(likelihoods);
            if (cipher.getScore(likelihoods) > best.getScore(likelihoods)) {
                best = cipher;
            }
        }
        System.out.println(best.getPlainText());
    }
}