package com.bibliofreaks.feed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class FeedUtil {

	public static Logger basicLogger = Logger.getLogger(FeedUtil.class
			.getName());

	public static String hitUrl(String preparedUrl) {
		// Uncomment the below for using proxy
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
//				"10.138.190.51", 8080));
//		 System.setProperty("http.proxyHost", "10.138.190.51");
//		 System.setProperty("http.proxyPort", "8080");
//		 System.setProperty("http.proxyUser", "siva.sankar.reddy.indireddy@sap.com");
//		 System.setProperty("http.proxyPassword", "Anjali@310713");
		URL Url = null;
		String sUrl = preparedUrl;
		try {
			Url = new URL(sUrl);
			System.out.println(Url);
			basicLogger.debug("Url formed is=\t" + Url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			// Uncomment the below for using proxy
//			con = (HttpURLConnection) Url.openConnection(proxy);
			 con = (HttpURLConnection)Url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream inStream = null;
		try {
			inStream = con.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int asd = 0;
		StringBuffer sb = new StringBuffer();
		try {
			while ((asd = inStream.read()) != -1)
				sb.append((char) asd);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(sb.toString());

		return sb.toString();
	}

	// public static HashMap<String,String> dataaccumulator(String key,String
	// value)
	// {
	//
	// HashMap<String,String> h1 = new HashMap<String,String>();
	// h1.put(key, value);
	// return h1;
	// }

	public void fileformatter(HashMap<String, String> data) {

		HashMap<String, String> h1 = new HashMap<String, String>();
		h1 = data;

		// String fileContent = ""+value;
		// return h1;
		String newhtml = "";
		FileWriter fstream;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		System.out.println("date with which final feed file will be created=\t"
				+ dateFormat.format(date));
		basicLogger.debug("date with which final feed file will be created=\t"
				+ dateFormat.format(date));
		try {
			fstream = new FileWriter(
					"D:\\Personal\\BiblioFreaks\\feedcreation\\BooksDhyameshSet1_"
							+ dateFormat.format(date) + ".csv", true);

			BufferedWriter feedfile = new BufferedWriter(fstream);

			File myFile = new File(
					"D:\\Personal\\BiblioFreaks\\feedcreation\\BooksDhyameshSet1_"
							+ dateFormat.format(date) + ".csv");
			if (myFile != null && myFile.exists() && myFile.length() <= 0) {
				feedfile.write("\"PRODUCTNAME\",\"CATEGORY\",\"SUB_CATEGORY\",\"ALTERNATE_SORT_VALUE\",\"SKU_PRODUCT_ID\",\"IMAGE_1\",\"IMAGE_2\",\"IMAGE_3\",\"IMAGE_4\",\"IMAGE_5\",\"PRODUCT_DESCRIPTION\",\"RELEVANT_TAGS\",\"OPTIONS_VALUES\",\"PRICE_MRP\",\"DISCOUNTED_PRICE\",\"QUANTITY_IN_STOCK\",\"SHIPPING_CHARGES\",\"WEIGHT_OF_PRODUCT\",\"CUSTOM_MESSAGE\",\"SIZE_CHART\",\"SEO_TITLE_TAG\",\"SEO_META_DESCRIPTION\",\"ALT_TEXT_PRODUCT_IMAGE1\",\"ALT_TEXT_PRODUCT_IMAGE2\",\"ALT_TEXT_PRODUCT_IMAGE3\",\"ALT_TEXT_PRODUCT_IMAGE4\",\"ALT_TEXT_PRODUCT_IMAGE5\",\"CUSTOM_URL\"");
				// feedfile.write("\"ISBN\",\"description\"");
			}
			feedfile.write("\n");
			/* To get the sample format of the html to be written */
			BufferedReader htm = new BufferedReader(
					new FileReader(
							"D:\\Personal\\BiblioFreaks\\feedcreation\\referencehtmlforacademics.html"));
			StringBuilder samplehtml = new StringBuilder();
			String line = "";
			while ((line = htm.readLine()) != null) {
				samplehtml.append(line);
			}

			System.out.println("samplehtml" + samplehtml);
			basicLogger.debug("samplehtml" + samplehtml);
			newhtml = samplehtml.toString();

			if ((h1.get("<LANGUAGE>") == "en")
					|| ("english".equalsIgnoreCase(h1.get("<LANGUAGE>")))) {
				newhtml = newhtml.replace("<BTITLE>", h1.get("<BTITLE>"));
				newhtml = newhtml.replace("<AUTHORS>", h1.get("<AUTHORS>"));
				newhtml = newhtml.replace("<ISBN>", h1.get("<ISBN>"));
				newhtml = newhtml.replace("<PUBLISHER>", h1.get("<PUBLISHER>"));
				newhtml = newhtml.replace("<PUBLISHEDDATE>",
						h1.get("<PUBLISHEDDATE>"));
				newhtml = newhtml.replace("<LANGUAGE>", h1.get("<LANGUAGE>"));
				newhtml = newhtml.replace("<DESCRIPTION>",
						h1.get("<DESCRIPTION>"));
				newhtml = newhtml.replace("<PRINTTYPE>", h1.get("<PRINTTYPE>"));
				newhtml = newhtml.replace("<NOP>", h1.get("<NOP>"));
				newhtml = newhtml.replace("<AWARDS>", h1.get("<AWARDS>"));
				newhtml = newhtml.replace("<CONTENTS>", h1.get("<CONTENTS>"));
				System.out.println("Formated html is : " + newhtml);
				basicLogger.debug("Formated html is : " + newhtml);
			} else {
				newhtml = newhtml.replace("<BTITLE>", h1.get("<BTITLE>"));
				newhtml = newhtml.replace("<AUTHORS>", h1.get("<AUTHORS>"));
				newhtml = newhtml.replace("<ISBN>", h1.get("<ISBN>"));
				newhtml = newhtml.replace("<PUBLISHER>", h1.get("<PUBLISHER>"));
				newhtml = newhtml.replace("<PUBLISHEDDATE>",
						h1.get("<PUBLISHEDDATE>"));
				// newhtml = newhtml.replace("<LANGUAGE>",
				// h1.get("<LANGUAGE>"));
				newhtml = newhtml.replace("<DESCRIPTION>",
						h1.get("<DESCRIPTION>"));
				newhtml = newhtml.replace("<PRINTTYPE>", h1.get("<PRINTTYPE>"));
				newhtml = newhtml.replace("<NOP>", h1.get("<NOP>"));
				newhtml = newhtml.replace("<AWARDS>", h1.get("<AWARDS>"));
				newhtml = newhtml.replace("<CONTENTS>", h1.get("<CONTENTS>"));
				System.out.println("Formated html is : " + newhtml);
				basicLogger.debug("Formated html is : " + newhtml);
			}
			// feedfile.write("\""+h1.get("<BTITLE>")+"\",\"Fictions and Novels\",\"Romance\",\"Used - Good Condition\",\""+h1.get("<THUMBNAIL>")+"\",,,,,"+newhtml+",\"40\",,,,,\"1\",\"Used - Good condition\",\"1\",,,,,");
			/* below line is commented to genrate file from CSV */
			// feedfile.append("\""+h1.get("<BTITLE>")+"\",\"Fictions and Novels\",\"Random\",\"Used - Good Condition\",\""+h1.get("<THUMBNAIL>")+"\",,,,,"+newhtml+",\"40\",,,,,\"1\",\"Used - Good condition\",\"1\",,,,,");
//			feedfile.append("\""
//					+ h1.get("<BTITLE>")
//					+ "\",\"Recently Added\",\"\",\"Used - Good Condition\",\"\",\""
//					+ h1.get("<IMAGE1>")
//					+ "\",\""
//					+ h1.get("<IMAGE2>")
//					+ "\",,,,"
//					+ newhtml
//					+ ",\""
//					+ h1.get("<BTITLE>")
//					+ ","
//					+ h1.get("<AUTHORS>")
//					+ ",used books,second hand books\",\"Used - Good Condition\",,,\"1\",\"40\",,,");
			// feedfile.append("\""+h1.get("<ISBN>")+"\""+",\""+h1.get("<DESCRIPTION>")+"\"");

			/* Partner Books */
			String row = "\"" + h1.get("<BTITLE>")
					+ "\",\"Academics\",\"\",\"Used - Good Condition\",\"\",\""
					+ h1.get("<IMAGE1>") + "\",\"" 
					+ h1.get("<IMAGE2>") + "\",,,," 
					+ newhtml
					+ ",\"Second hand books,Used books,BANG_DHYA\",\"Used - Good Condition\",,,\"1\",\"50\",,,," 
					+ "\""+ h1.get("<BTITLE>") + "- Second Hand Book - BiblioFreaks\",\""
					+ h1.get("<BTITLE>") + " by " + h1.get("<AUTHORS>") + " second hand book for sale\",\""
					+ h1.get("<IMAGE1>") + "\",\"" 
					+ h1.get("<IMAGE2>") + "\",,,,,";
			feedfile.append(row);
			try {
				feedfile.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				basicLogger.debug("Error: " + e.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
