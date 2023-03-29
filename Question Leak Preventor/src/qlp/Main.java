import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.codec.binary.*;

public class Main
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("QLP-Question Leak Preventor");
		System.out.println("___________________________");
		System.out.println("Question Folder: ");
		String file=input.nextLine();
		System.out.println("Encryption Passphrase: ");
		String passphrase=input.nextLine();
		/*
		*/
		QLP s=new QLP();
		s.setFolder(file);
		s.setPass(passphrase);
		s.work();
	}
}
