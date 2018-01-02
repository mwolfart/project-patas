package controller;

import java.util.HashMap;

// Class containing the helper functions
public class Helper {

	// Casts an object to Long
	public static Long objectToLong(Object object) {
		String stringValue = String.valueOf(object);
		Long longValue = Long.parseLong(stringValue);
		return longValue;
	}
	
	// splitCriteriaFromKeys
	// given a list of criteria (strings) in the format "crit":"val",
	//   split them in a hashmap.
	public static HashMap<String, String> splitCriteriaFromKeys(String[] criteria_pairs) {
		HashMap<String, String> criteria_list = new HashMap<String, String>();

		for (int i=1; i < criteria_pairs.length; i++) {
			String pair = criteria_pairs[i];
			String[] splitted_pair = pair.split("\"|:|\"");
			Boolean value_is_not_string = (splitted_pair.length == 4);

			if (value_is_not_string)
				criteria_list.put(splitted_pair[1], splitted_pair[3]);
			else criteria_list.put(splitted_pair[1], splitted_pair[4]);
		}

		return criteria_list;
	}
}
