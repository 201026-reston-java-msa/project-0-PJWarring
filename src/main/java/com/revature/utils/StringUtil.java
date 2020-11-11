package com.revature.utils;

import java.util.List;

public class StringUtil {

	public static boolean isValidInput(String input, List<String> validInput, boolean ignoreCaseBool) {
		if (ignoreCaseBool) {
			for (String s : validInput) {
				if (s.equalsIgnoreCase(input)) return true;
			}
		} else {
			for (String s: validInput) {
				if (s.equals(input)) return true;
			}
		}
		return false;
	}
	
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
