import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cryptography {
    private ArrayList<Character> inner = new ArrayList();
    private ArrayList<Character> outer = new ArrayList();
    private ArrayList<Character> middle = new ArrayList();
    private int innerRot = 0;

    public Cryptography(ArrayList<Character> outer, ArrayList<Character> mid, ArrayList<Character> in, int rot) {
        this.inner = in;
        this.middle = mid;
        this.outer = outer;
        this.innerRot = rot;
    }

    public Cryptography() {
        fillCipher(this.outer);
        fillCipher(this.middle);
        fillCipher(this.inner);
        this.innerRot = 0;
    }

    /**
     * Returns ArrayList filled with characters in a random order
     *
     * @param cipher ArrayList to be filled
     */
    private void fillCipher(ArrayList<Character> cipher) {
        List<Character> alphabet = new ArrayList();
        for (char i = 'A'; i < 'Z' + 1; i++) {
            alphabet.add(i);
        }
        int x = 0;
        do {
            int i = (int) (Math.random() * alphabet.size());//make sure each list gets filled
            if (alphabet.size() > 0) {
                do {
                    cipher.add(alphabet.get(i));
                } while ((cipher.get(x) == null));
                alphabet.remove(i);
            }
            x++;
        } while (x != 26);
    }

    /**
     * either encrypts or decrypts a user-provided file.
     *
     * @param option command to encrypt or decrypt
     * @return ArrayList with the translated text
     */
    public ArrayList<String> translate(String option) {
        ArrayList<String> translated = new ArrayList();
        ArrayList<String> txtFile = new ArrayList();
        while (txtFile.isEmpty()) {
            Scanner input = new Scanner(System.in);
            System.out.println("What is the name of your file?(include \".txt\" ending)");
            String title = input.nextLine();
            File name = new File(title);
            try {
                Scanner sc = new Scanner(name);
                while (sc.hasNextLine()) {
                    String txtArray = sc.nextLine();
                    txtFile.add(txtArray);
                }
            } catch (FileNotFoundException ex) {
                System.out.println(title + " does not exist in this directory.\n");
            }
        }
        if (option.equals("decrypt")) {
            ArrayList<String> decrypted = new ArrayList();
            for (int i = txtFile.size() - 1; i >= 0; i--) {//decryption must be done backwards
                decrypted.add(inputDecrypt(txtFile.get(i)));
            }
            for (int i = decrypted.size() - 1; i >= 0; i--) {//reverse the line order
                translated.add(decrypted.get(i));
            }
        } else if (option.equals("encrypt")) {
            for (String i : txtFile) {
                translated.add(inputEncrypt(i));
            }
        }
        return translated;
    }

    /**
     * takes a parameter cipher (Cryptography) and translates a user-typed phrase using this cipher
     * @param currentCipher
     * @return String of the translated phrase
     */
    public String inputTranslation(Cryptography currentCipher) {
        Scanner input = new Scanner(System.in);
        String toTranslate, changedText = "";
        int choice;
        System.out.println("What would you like to translate?");
        toTranslate = input.nextLine();
        System.out.println("Encrypt(1) or Decrypt?(2)");
        choice = input.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Encrypted:");
                changedText = inputEncrypt(toTranslate);
                break;
            case 2:
                System.out.println("Decrypted:");
                changedText = inputDecrypt(toTranslate);
                break;
            default:
                System.out.println("Sorry, that wasn't an option.");
                break;
        }
        System.out.println(changedText);
        return changedText;
    }

    /**
     * Decrypts the parameter String
     * @param toTranslate the String to decrypt
     * @return the decrypted String
     */
    private String inputDecrypt(String toTranslate) {
        String decrypted = "";
        for (int i = toTranslate.length() - 1; i >= 0; i--) {
            char r;
            if (Character.isUpperCase(toTranslate.charAt(i))) {
                r = reverseCorrespondingLetter(toTranslate.charAt(i));
                decrypted = r + decrypted;
            } else if (Character.isLowerCase(toTranslate.charAt(i))) {
                r = reverseCorrespondingLetter(Character.toUpperCase(toTranslate.charAt(i)));
                decrypted = Character.toLowerCase(r) + decrypted;
            } else {
                r = toTranslate.charAt(i);
                decrypted = r + decrypted;
            }
        }
        return decrypted;
    }

    /**
     * encrypts a given parameter String
     * @param toTranslate the String to be encrypted
     * @return the encrypted String
     */
    private String inputEncrypt(String toTranslate) {
        String encrypted = "";
        for (int i = 0; i < toTranslate.length(); i++) {
            char r;
            if (Character.isUpperCase(toTranslate.charAt(i))) {
                r = correspondingLetter(toTranslate.charAt(i));
                encrypted += r;
            } else if (Character.isLowerCase(toTranslate.charAt(i))) {
                r = correspondingLetter(Character.toUpperCase(toTranslate.charAt(i)));
                encrypted += Character.toLowerCase(r);
            } else {
                r = toTranslate.charAt(i);
                encrypted += r;
            }
        }
        return encrypted;
    }

    /**
     * performs the cipher rotation and returns the corresponding letter
     * @param x letter to encrypt
     * @return encrypted equivalent
     */
    private Character correspondingLetter(char x) {
        int indexInner = inner.indexOf(x);
        char letterOuter = outer.get(indexInner);
        int indexMid = middle.indexOf(letterOuter);
        rotate();
        return outer.get(indexMid);
    }

    /**
     * performs the backwards operation (reversing the cipher rings to decrypt)
     * @param x the initial character to be decrypted
     * @return the corresponding decrypted letter
     */
    private Character reverseCorrespondingLetter(char x) {
        reverseRotate();
        int indexOuter = outer.indexOf(x);
        char letterMid = middle.get(indexOuter);
        int letterOuter = outer.indexOf(letterMid);
        return inner.get(letterOuter);
    }

    /**
     * rotate in forward direction
     */
    private void rotate() {
        if (innerRot < inner.size()) {
            char temp = inner.get(inner.size() - 1);
            for (int i = inner.size() - 1; i > 0; i--) {
                inner.set(i, inner.get(i - 1));
            }
            inner.set(0, temp);
            innerRot++;
        } else {
            char temp = middle.get(middle.size() - 1);
            for (int i = middle.size() - 1; i > 0; i--) {
                middle.set(i, middle.get(i - 1));
            }
            middle.set(0, temp);
            innerRot = 0;
        }
    }

    /**
     * rotate in opposite direction
     */
    private void reverseRotate() {
        if (innerRot > 0 && innerRot <= inner.size()) {
            char temp = inner.get(0);
            for (int i = 0; i < inner.size() - 1; i++) {
                inner.set(i, inner.get(i + 1));
            }
            inner.set(25, temp);
            innerRot--;
        } else {
            char temp = middle.get(0);
            for (int i = 0; i < middle.size() - 1; i++) {
                middle.set(i, middle.get(i + 1));
            }
            middle.set(25, temp);
            innerRot = 26;
        }
    }

    /**
     *reads in a file and converts it into cipher format.
     * @return formatted cipher
     */
    public Cryptography readCipher() {
        Scanner input = new Scanner(System.in);
        while (this.inner.isEmpty()) {
            System.out.println("Enter cipher filename(include \".txt\" ending).");
            String fileName = input.nextLine();
            try {
                File file = new File(fileName);
                Scanner givenCipher = new Scanner(file);
                while (givenCipher.hasNextLine()) {
                    String txtArray = givenCipher.nextLine();
                    String[] split = txtArray.split(" ");
                    if (outer.isEmpty()) {
                        fillArray(outer, split);
                    } else if (middle.isEmpty()) {
                        fillArray(middle, split);
                    } else if (inner.isEmpty()) {
                        fillArray(inner, split);
                    } else {
                        if (txtArray.trim().length() == 2) {
                            setRot(Character.getNumericValue(txtArray.charAt(0)) * 10
                                    + Character.getNumericValue(txtArray.charAt(1)));
                        } else {
                            setRot(Character.getNumericValue(txtArray.charAt(0)));
                        }
                    }
                }
                givenCipher.close();
            } catch (FileNotFoundException ex) {
                System.out.println(fileName + " does not exist in this directory.\n");
            }
        }
        return this;
    }

    /**
     * Fills parameter ArrayList with the characters in a String Array
     * @param toFill ArrayList to be filled
     * @param icon array with the characters to transfer
     */
    private void fillArray(ArrayList<Character> toFill, String[] icon) {
        for (int i = 0; i < icon.length; i++) {
            toFill.add(icon[i].trim().charAt(0));
        }
    }

    /**
     * changes inner rotation amount
     *
     * @param rot number to set rotation to
     **/
    private void setRot(int rot) {
        innerRot = rot;
    }

    /**
     * returns how much the ring has been shifted
     *
     * @return rotation amount
     */
    public int getRot() {
        return innerRot;
    }

    /**
     * returns string representation of three-ringed cipher
     *
     * @return string with all 3 rings
     */
    public String toString() {
        String converted = "";
        for (char a : outer) {
            converted += a + " ";
        }
        converted += "\n";
        for (char a : middle) {
            converted += a + " ";
        }
        converted += "\n";
        for (char a : inner) {
            converted += a + " ";
        }
        return converted;
    }

}
