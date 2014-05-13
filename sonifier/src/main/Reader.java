package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.Constants.Category;

public class Reader {

	public static Email createEmail(String email) {
		String [] tokens = email.split(",");
		Category category = null;
		switch(Integer.parseInt(tokens[1])) {
			case 0:
				category= Category.OFFICIAL;
				break;
			case 1:
				category = Category.LOST_AND_FOUND;
				break;
			case 2:
				category = Category.BUYING_AND_SELLING;
				break;
			case 3:
				category = Category.COLLEGE_ACTIVITIES;
				break;
			case 4:
				category = Category.USG_AND_PARLY;
				break;
	
			case 5:
				category = Category.PARTY;
				break;
	
			case 6:
				category = Category.ACADEMICS;
				break;
			default:
				category = Category.OTHERS;
				break;
		}

		int length = Integer.parseInt(tokens[1]);
		int timestamp = Integer.parseInt(tokens[2]);
		
		return new Email(category, length, timestamp, 0);
	}
	
	public static List<Email> readEmails(String filename) {
		List<Email> emails = new ArrayList<Email>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String email = reader.readLine();
			while (email != null) {
				emails.add(createEmail(email));
//				if (emails.size() == 3) {
//					reader.close();
//					return emails;
//				}
				email = reader.readLine();
			}
			reader.close();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		return emails;
	}
}
