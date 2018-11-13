package com.bibliofreaks.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bibliofreaks.action.GenerateFeedFromCsv;
import com.bibliofreaks.feed.FeedUtil;

public class FeedFormatUtils {

	public static Logger basicLogger                    = Logger.getLogger(FeedFormatUtils.class.getName());
	
	
	public static String imageNameFormat(String title){
		String temp = title;
		Pattern p = Pattern.compile("\\W+");
		
		Matcher m = p.matcher(temp);
		System.out.println("title before fomating for image name="+title);
		basicLogger.debug("title before fomating for image name="+title);
		if(m.find()){
			title = m.replaceAll("_");
		}else{
		   System.out.println("could not find any matching for replacing special characters");
		   basicLogger.debug("could not find any matching for replacing special characters");
		}
		
		System.out.println("title after formating for image name="+title);
		basicLogger.debug("title after formating for image name="+title);
		return title;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		System.out.println(FeedFormatUtils.imageNameFormat("90"));
		
//		System.out.println(FeedFormatUtils.formatedTitle("Twilight"));
	}

	public static String formatedTitle(String title) {
		// TODO Auto-generated method stub
		String titlehere = title;
		String[] newtitle = titlehere.split(",");
		if (newtitle.length == 2) {
			titlehere = newtitle[1] + " " + newtitle[0];
		} else {
			titlehere = newtitle[0];
		}
	
		titlehere = titlehere.replaceAll("[^a-zA-Z0-9+# ]", "");
		titlehere = titlehere.trim();
		// basicLogger.debug("title  formated=\t"+titlehere);
		return titlehere;
	}

	public static String formatedDescription(String description) {
		// TODO Auto-generated method stub
		String descr = description;
		descr = descr.replaceAll("(<br />){1,}", "<br />");
		descr = descr.replaceAll("(<br>){1,}", "<br>");
		descr = descr.replaceAll("\"", "");
		descr = descr.replaceAll("[^\u0000-\u007F]", " ");
		FeedUtil.basicLogger.debug("description  formated=\t" + descr);
		return descr;
	}

	public static String authorNameFormat(String author_details) {
		String author = author_details;
		String renamedAuthorName = "";
		// System.out.println(author);
		String firstName = "";
		String[] authorfullname = author.trim().split("\\|");
		String secondName = "";
		String[] temp = null;
		for (int i = 0; i < authorfullname.length; i++) {
			if (i > 0) {
				renamedAuthorName = renamedAuthorName + "|";
				// System.out.println("|");
	
			}
			if (authorfullname[i] != null) {
				temp = authorfullname[i].split(",");
				// String [] temp1 = line.split(",");
	
				if (temp.length == 2) {
					firstName = temp[1];
					// System.out.println("FirstName "+firstName);
					renamedAuthorName = renamedAuthorName + " " + firstName;
				}
				secondName = temp[0];
				// System.out.println("SecondName "+secondName);
				renamedAuthorName = renamedAuthorName + " " + secondName;
				renamedAuthorName = renamedAuthorName.trim();
			}
	
		}
	
		// System.out.println("Author FullName Formated=\t"+renamedAuthorName);
		// basicLogger.debug("Author FullName Formated=\t"+renamedAuthorName);
		return renamedAuthorName;
	
	}

}
