import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public void fillCipher(ArrayList<Character> cipher) {
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

    public ArrayList<String> translate(String string) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        ArrayList<String> txtFile = new ArrayList();
        System.out.println("What is the name of your file?(include \".txt\" ending)");
        String title = input.nextLine();
        File name = new File(title);
        Scanner sc = new Scanner(name);
        while (sc.hasNextLine()) {
            String txtArray = sc.nextLine();
            txtFile.add(txtArray);
        }
        if (string.equals("decrypt")) {
            ArrayList<String> decrypted = new ArrayList();
            ArrayList<String> clone = new ArrayList();
            for (int i = txtFile.size() - 1; i >= 0; i--) {//decryption must be done backwards
                decrypted.add(inputDecrypt(txtFile.get(i)));
            }
            for (int i = decrypted.size() - 1; i >= 0; i--) {//reverse the line order
                clone.add(decrypted.get(i));
            }
            return clone;
        } else {
            ArrayList<String> encrypted = new ArrayList();
            for (String i : txtFile) {
                encrypted.add(inputEncrypt(i));
            }
            return encrypted;
        }
    }

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

    public String inputDecrypt(String toTranslate) {
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

    public String inputEncrypt(String toTranslate) {
        String encrypted = "";
        for (int i = 0; i < toTranslate.length(); i++) {
            Scanner input = new Scanner(System.in);
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

    private Character correspondingLetter(char x) {
        int indexInner = inner.indexOf(x);
        char letterOuter = outer.get(indexInner);
        int indexMid = middle.indexOf(letterOuter);
        rotate();
        return outer.get(indexMid);
    }

    private Character reverseCorrespondingLetter(char x) {
        reverseRotate();
        int indexOuter = outer.indexOf(x);
        char letterMid = middle.get(indexOuter);
        int letterOuter = outer.indexOf(letterMid);
        return inner.get(letterOuter);
    }

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

    public Cryptography readCipher() throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter cipher filename(include \".txt\" ending).");
        String fileName = input.nextLine();
        File file = new File(fileName);
        Scanner givenCipher = new Scanner(new FileInputStream(file));
        FileReader in = new FileReader(fileName);
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
        return this;
    }

    private void fillArray(ArrayList<Character> toFill, String[] icon) {
        for (int i = 0; i < icon.length; i++) {
            toFill.add(icon[i].trim().charAt(0));
        }
    }

    public void setRot(int rot) {
        innerRot = rot;
    }

    public int getRot() {
        return innerRot;
    }

    public List<Character> getInner() {
        return inner;
    }

    public List<Character> getOuter() {
        return outer;
    }

    public List<Character> getMiddle() {
        return middle;
    }

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
