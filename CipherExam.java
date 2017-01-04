import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//
public class CipherExam {

	public CipherExam(){
		ArrayList<Character> OCircle = new ArrayList<Character>(26);
		ArrayList<Character> MCircle = new ArrayList<Character>(26);
		ArrayList<Character> ICircle = new ArrayList<Character>(26);
		Cryptography currentCipher = new Cryptography(OCircle,MCircle,ICircle,0);
		ArrayList<String> translatedtext=new ArrayList();
		int choice;
		Scanner input = new Scanner(System.in);
		System.out.println("Would you like to randomly generate a cipher, or do you have one?\n1.)Random\n2.)Already have");

		choice=input.nextInt();
		if(choice==2)
		{
			currentCipher = currentCipher.readCipher();
		}
		else
		{

			fillCipher(OCircle);
			fillCipher(MCircle);
			fillCipher(ICircle);
			currentCipher=new Cryptography(OCircle,MCircle,ICircle,0);
		}
		System.out.println("Here is our cipher.");
		currentCipher.print();
		do{
			System.out.println("What would you like to do?\n1.)Encrypt a file\n2.)Decrypt a file\n3.)Encrypt or Decrypt a phrase\n4.)Exit");
			choice=input.nextInt();
			switch(choice){
			case 1: translatedtext=currentCipher.translate("encrypt");
			//	System.out.println(currentCipher.getRot());

			for(String a:translatedtext)
			{
				System.out.println(a);
			}
			break;
			case 2: 
				System.out.println(currentCipher.getRot());
				translatedtext=currentCipher.translate("decrypt");
				for(String a:translatedtext)
				{
					System.out.println(a);
				}

				break;
			case 3: currentCipher.inputTranslation(currentCipher);
			break;
			case 4: 
				//System.out.println(currentCipher.getRot());
				currentCipher.finalCiph().print();
				save(currentCipher,translatedtext);
				break;
			default: System.out.println("Please Enter a number from 1-4.");
			break;
			}
		}while(choice!=4);
	}
	public void fillCipher(List<Character> a)
	{
		List<Character> alphabet = new ArrayList<Character>();
		for(char i = 'A';i<'Z'+1;i++)
		{
			alphabet.add(i);
		}

		int x=0;
		do{
			int i=(int) (Math.random()*alphabet.size());//make sure each list gets filled
			if(alphabet.size()>0)
			{
				do
				{
					a.add(alphabet.get(i));
				}while((a.get(x)==null));
				alphabet.remove(i);
			}
			x++;
		}while(x!=26);
	}
	public void save(Cryptography a, ArrayList<String> translatedtext)
	{
		String title;
		Scanner input = new Scanner(System.in);
		System.out.println("What should we call your saved file?");
		title=input.nextLine();
		try {
			File file = new File(title+"cipher.txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));



			for(Character x: a.finalCiph().getOuter())
			{
				output.write(x+" ");
			}
			output.newLine();
			for(Character x: a.finalCiph().getMiddle())
			{
				output.write(x+" ");
			}
			output.newLine();
			for(Character x: a.finalCiph().getInner())
			{
				output.write(x+" ");
			}
			output.newLine();
			output.write(Integer.toString(a.getRot()));

			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		try {
			File mainfile = new File(title+".txt");
			BufferedWriter output = new BufferedWriter(new FileWriter(mainfile));
			for(String x: translatedtext)
			{
				output.write(x);
				output.newLine();
			}
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	private class Cryptography{
		private  ArrayList<Character> inner = new ArrayList <Character>();
		private  ArrayList<Character> outer = new ArrayList <Character>();
		private  ArrayList<Character> middle = new ArrayList <Character>();
		private  int innerrotations=0;
		public Cryptography(ArrayList<Character> oCircle,ArrayList<Character> mCircle,ArrayList<Character> iCircle,int i)
		{
			inner = iCircle;
			middle = mCircle;
			outer = oCircle;
			innerrotations=i;
		}
		public ArrayList<String> translate(String string) {
			Scanner input = new Scanner(System.in);
			ArrayList<String> textfile = new ArrayList<String>();
			System.out.println("What is the name of your file?(include \".txt\" ending)");
			String title=input.nextLine();
			try{	
				File name = new File (title);
				Scanner sc = new Scanner(new FileInputStream(name));
				FileReader in=new FileReader(title);
				BufferedReader bufferReader = new BufferedReader(in);
				while(sc.hasNextLine())
				{
					String txtarray=sc.nextLine();
					textfile.add(txtarray);
				}
				if(string.equals("decrypt"))
				{
					ArrayList<String> decrypted = new ArrayList<String>();
					ArrayList<String> clone = new ArrayList<String>();
					for(int i=textfile.size()-1;i>=0;i--)
					{
						decrypted.add(inputDecrypt(textfile.get(i)));
					}
					for(int i=decrypted.size()-1;i>=0;i--)
					{
						clone.add(decrypted.get(i));
					}
					return clone;
				}
				else if(string.equals("encrypt"))
				{
					ArrayList<String> encrypted = new ArrayList<String>();
					for(String i:textfile)
					{
						encrypted.add(inputEncrypt(i));
					}
					return encrypted;
				}

			}
			catch (FileNotFoundException ex)
			{
				System.out.println("*** Cannot open " + title
						+ " ***");
				System.exit(1);  // quit the program
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null;
		}
		public void inputTranslation(Cryptography currentCipher) {
			Scanner input = new Scanner(System.in);
			String totranslate,encrypted,decrypted;
			int choice;
			System.out.println("What would you like to translate?");
			totranslate=input.nextLine();
			System.out.println("Encrypt(1) or Decrypt?(2)");
			choice=input.nextInt();
			switch(choice){
			case 1: System.out.println("Encrypted:");
			encrypted=inputEncrypt(totranslate);
			System.out.println(encrypted);
			break;
			case 2:System.out.println("Decrypted:");
			decrypted=inputDecrypt(totranslate);
			System.out.println(decrypted);
			break;
			default:System.out.println("Sorry, that wasn't an option");
			break;
			}


		}
		public String inputDecrypt(String totranslate) {
			String decrypted="";
			for(int i=totranslate.length()-1;i>=0;i--)
			{
				char r;
				if(Character.isUpperCase(totranslate.charAt(i)))
				{
					r = reversecorrespondingLetter(totranslate.charAt(i));
					decrypted=r+decrypted;
				}
				else if(Character.isLowerCase(totranslate.charAt(i)))
				{
					r = reversecorrespondingLetter(Character.toUpperCase(totranslate.charAt(i)));
					decrypted=(Character.toLowerCase(Character.toUpperCase(r)))+decrypted;//totranslate.charAt(i))));
				}
				else
				{
					r=totranslate.charAt(i);
					decrypted=r+decrypted;
				}
			}
			return decrypted;
		}
		public String inputEncrypt(String totranslate) {
			String encrypted="";

			for(int i=0;i<totranslate.length();i++)
			{
				Scanner input = new Scanner(System.in);
				char r;
				if(Character.isUpperCase(totranslate.charAt(i)))
				{
					r = correspondingLetter(totranslate.charAt(i));
					encrypted+=r;
				}
				else if(Character.isLowerCase(totranslate.charAt(i)))
				{
					//System.out.println(totranslate);
					//	System.out.println(totranslate.charAt(i));
					r = correspondingLetter(Character.toUpperCase(totranslate.charAt(i)));
					encrypted+=(Character.toLowerCase(Character.toUpperCase(r)));
				}
				else
				{
					r=totranslate.charAt(i);
					encrypted+=r;
				}
			}
			return encrypted;
		}
		private  Character correspondingLetter(char x)
		{
			int innerindex= inner.indexOf(x);
			char outerletter=outer.get(innerindex);
			int middleindex=middle.indexOf(outerletter);
			rotate();
			//System.out.println(innerrotations);
			return outer.get(middleindex);
		}

		private  void rotate() {
			if(innerrotations<26)
			{
				char temp = inner.get(25);
				for(int i=25;i>0;i--)
				{
					inner.set(i,inner.get(i-1));
				}
				inner.set(0,temp);
				//	print();
				innerrotations++;
			}
			else
			{
				char temp = middle.get(25);
				for(int i=25;i>0;i--)
				{
					middle.set(i,middle.get(i-1));
				}
				middle.set(0,temp);
				//System.out.println("Middle");
				//print();
				innerrotations=0;

			}
		}

		private  Character reversecorrespondingLetter(char x)
		{
			reverserotate();
		//	System.out.println(innerrotations);

			int outerindex= outer.indexOf(x);
			char middleletter=middle.get(outerindex);
			int outerletter=outer.indexOf(middleletter);
			return inner.get(outerletter);

		}

		private  void reverserotate() {
			if(innerrotations!=0&&innerrotations<=26)
			{
				char temp = inner.get(0);
				for(int i=0;i<25;i++)
				{
					inner.set(i,inner.get(i+1));
				}
				inner.set(25,temp);
				//	print();
				innerrotations--;
			}
			else
			{
				char temp = middle.get(0);
				for(int i=0;i<25;i++)
				{
					middle.set(i,middle.get(i+1));
				}
				middle.set(25,temp);
				//		System.out.println("Middle");

				//	print();
				innerrotations=26;

			}
		}
		public Cryptography finalCiph(){
			return new Cryptography(outer,middle,inner,innerrotations);
		}

		public Cryptography readCipher(){
			ArrayList<Character> OCircle = new ArrayList<Character>(26);
			ArrayList<Character> MCircle = new ArrayList<Character>(26);
			ArrayList<Character> ICircle = new ArrayList<Character>(26);

			Scanner input = new Scanner(System.in);
			System.out.println("enter cipher filename.(include \".txt\" ending)");
			String filename = input.nextLine();
			try
			{

				File file = new File(filename);
				Scanner detcipher = new Scanner(new FileInputStream(file));
				FileReader in=new FileReader(filename);
				boolean oc=false,ic=false,mc=false;
				while(detcipher.hasNextLine())
				{
					String txtarray = detcipher.nextLine();
					String[] split = txtarray.split(" ");
					if(!oc){
						for(int i=0;i<split.length;i++)
						{
							OCircle.add(split[i].trim().charAt(0));
						}
						oc=true;
					}

					else if(!mc){
						for(int i=0;i<split.length;i++)
						{
							MCircle.add(split[i].trim().charAt(0));
						}
						mc=true;
					}
					else if(!ic){
						for(int i=0;i<split.length;i++)
						{
							ICircle.add(split[i].trim().charAt(0));
						}
						ic=true;
					}

					else
					{
						if(txtarray.trim().length()==2)
						{
							setRot(Character.getNumericValue(txtarray.charAt(0))*10+Character.getNumericValue(txtarray.charAt(1)));
						//	System.out.println(innerrotations);
						}
						else
						{
							setRot(Character.getNumericValue(txtarray.charAt(0)));
						//	System.out.println(innerrotations);
						}
					}

				}
				detcipher.close();
			}
			catch (FileNotFoundException ex)
			{
				System.out.println("*** Cannot open " + filename
						+ " ***");
				System.exit(1);  // quit the program
			}


			return new Cryptography(OCircle,MCircle,ICircle,innerrotations);

		}
		public void setRot(int rot)
		{
			System.out.println(rot);
			innerrotations=rot;
		}
		public int getRot()
		{
			return innerrotations;
		}
		public List<Character> getInner()
		{
			return inner;
		}
		public List<Character> getOuter()
		{
			return outer;
		}
		public List<Character> getMiddle()
		{
			return middle;
		}
		public void print()
		{
			for(char a : outer)
			{
				System.out.print(" " + a);
			}
			System.out.println();
			for(char a : middle)
			{
				System.out.print(" " + a);
			}
			System.out.println();

			for(char a : inner)
			{
				System.out.print(" " + a);
			}
			System.out.println();
			System.out.println();

		}

	}
}






