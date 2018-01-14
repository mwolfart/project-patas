package controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

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
	
	// Checks if a string is numeric
	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	public static <T> Specification<T> buildSpecFromSpecList(List<Specification<T>> spec_list) {
		if (spec_list.size() < 1)
			return null;

		Specification<T> result_spec = spec_list.get(0);
		for (int i=1; i < spec_list.size(); i++)
			result_spec = Specifications.where(result_spec).and(spec_list.get(i));

		return result_spec;
	}	
	
	/*
	public static byte[] savePrescription(String path){
		try { 
			FileOutputStream file = new FileOutputStream(path); 
			ByteArrayOutputStream bytesFile = new ByteArrayOutputStream();
			bytesFile.flush();
			byte[] fileArray = bytesFile.toByteArray();
			file.write(fileArray);
			bytesFile.close();
			file.close();
			return fileArray;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	*/ 
}
