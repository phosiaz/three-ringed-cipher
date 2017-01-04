import java.io.*;
import java.util.*;


public class CipherSim {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Character> outerRing = new ArrayList(26);
        ArrayList<Character> midRing = new ArrayList(26);
        ArrayList<Character> innerRing = new ArrayList(26);
        Cryptography currentCipher = new Cryptography(outerRing, midRing, innerRing, 0);
        ArrayList<String> changedText = new ArrayList();
        int choice;
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to randomly generate a cipher, or do you have one?" +
                "\n1.)Random\n2.)Already have");
        choice = input.nextInt();
        if (choice == 2) {
            currentCipher = currentCipher.readCipher();
        } else {
            currentCipher = new Cryptography();
        }
        System.out.println("Here is our cipher.");
        System.out.println(currentCipher.toString());
        do {
            System.out.println("What would you like to do?" +
                    "\n1.)Encrypt a file\n2.)Decrypt a file\n3.)Encrypt or Decrypt a phrase\n4.)Exit");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    changedText = currentCipher.translate("encrypt");
                    for (String a : changedText) {
                        System.out.println(a);
                    }
                    break;
                case 2:
                    changedText = currentCipher.translate("decrypt");
                    for (String a : changedText) {
                        System.out.println(a);
                    }
                    break;
                case 3:
                    changedText.add(currentCipher.inputTranslation(currentCipher));
                    break;
                case 4:
                    System.out.println(currentCipher.toString());
                    save(currentCipher, changedText);
                    break;
                default:
                    System.out.println("Please Enter a number from 1-4.");
                    break;
            }
        } while (choice != 4);
    }

    public static void save(Cryptography cipherToSave, ArrayList<String> changedText) throws FileNotFoundException {
        String title;
        Scanner input = new Scanner(System.in);
        System.out.println("What should we call your saved file? (no \".txt\")");
        title = input.nextLine();
        File cipherFile = new File(title + "cipher.txt");
        PrintStream output = new PrintStream(cipherFile);
        output.println(cipherToSave.toString());
        output.print(cipherToSave.getRot());
        File changedTextFile = new File(title + ".txt");
        output = new PrintStream(changedTextFile);
        for (String x : changedText) {
            output.println(x);
        }
        output.close();
    }
}


