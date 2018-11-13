package com.bibliofreaks.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static FileInputStream fi ;
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		try {
			fi = new FileInputStream("D:/Personal/BiblioFreaks/feedcreation/Oct26ThStock/freelance/Bookstest1.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		
//		System.out.println(dateFormat.format(date));
//		
//		Calendar rightNow = Calendar.getInstance();
//		System.out.println(rightNow);
//		
//		String str ="Silas £and Cathleen have loved each other since childhood. But when a jealous woman drives them apart, each is swept to a place where it seems there is no love, no peace, and no way back. This is a dramatic tale of courage and heartbreak.Per Dirk Pitt, semplicemente, una vita senza avventura non è degna di essere vissuta. Questa volta l'avventura si chiama Qin Shang, un abietto cinese privo di scrupoli e avido di ricchezze che ha fondato il proprio impero sul contrabbando di droga, di armi e soprattutto di vite umane, la sua merce di scambio preferita, al centro di loschi traffici d'immigrazione illegale. E adesso Qin Shang è disposto a qualsiasi cosa pur d'impadronirsi del relitto di una misteriosa nave carica di capolavori dell'antica arte cinese razziati da Chiang Kaishek durante la sua fuga dalla Cina e comprendente i resti preziosissimi del cosiddetto uomo di Pechino. La formidabile squadra della NUMA, comandata da Dirk Pitt, non ha però alcuna intenzione di stare a guardare. Seguendo le tracce di Qin Shang ha costruito un'enorme installazione portuale in una zona lontana dalle comuni rotte commerciali. Perchè ha scelto un luogo così improbabile? Che cosa si nasconde dietro quell'impresa? Mentre si prepara la drammatica resa dei conti finale, una sola cosa è sicura. Pitt ha di fronte il più pericoloso nemico che abbia mai incontrato...";
//				
//		str = str.replaceAll("[^\u0000-\u007F]", " ");
//		
//		System.out.println(str);
//		System.out.println(Integer.parseInt("9780747257585"));
//		
//		Calendar now = Calendar.getInstance();
//		System.out.println(now);
		
//		String str= "Rs. s 864 5 ";
//		
//		str = str.replaceAll("\\D","");
//		
//		System.out.println(str);
//		
//		int price = Integer.parseInt(str);
//		
//		System.out.println("price= "+price);
//		
//		System.out.print("dil"+"\n"+"pyar");
	}

}
