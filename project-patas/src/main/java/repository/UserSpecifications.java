package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.User;
import model.User_;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public final class UserSpecifications {
	private UserSpecifications() {}

	//Filter username
	public static Specification<User> usernameEquals(String username) {
		return (root, query, cb) -> {
			return cb.equal(root.get(User_.username), username);
		};
	} 
	
	//Filter user type
	public static Specification<User> userTypeEquals(Integer userType) {
		return (root, query, cb) -> {
			return cb.equal(root.get(User_.userType), userType);
		};
	} 
		
	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	public static List<Specification<User>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
		List<Specification<User>> spec_list = new ArrayList<Specification<User>>();

		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));

		return spec_list;
	}

	// buildSpecFromCriterion
	// given a criterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<User> buildSpecFromCriterion(Entry<String, String> criterion) {
		switch(criterion.getKey()) {
		case "username":
			return usernameEquals(criterion.getValue());
		case "userType":
			return userTypeEquals(Integer.parseInt(criterion.getValue()));
		default:
			return null;
		}
	}

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	public static Specification<User> buildSpecFromSpecList(List<Specification<User>> spec_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
		if (spec_list.size() < 1)
			return null;

		Specification<User> result_spec = spec_list.get(0);
		for (int i=1; i < spec_list.size(); i++)
			result_spec = Specifications.where(result_spec).and(spec_list.get(i));

		return result_spec;
	}

	// filterUserInfo
	// given a list of appointment classes, filter the information we want
	//   (given as a list of strings), and return it in a list.
	public static List<List<Object>> filterUserInfo(List<User> user_list, String[] desired_fields) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();

		for(User user : user_list) {
			List<Object> desired_info = new ArrayList<Object>();

			for(String field : desired_fields) {
				switch(field) {
				case "id":
					desired_info.add(user.getId());
					break;
				case "username":
					desired_info.add(user.getUsername());
					break;
				case "fullName":
					desired_info.add(user.getFullName());
					break;
				case "userType":
					desired_info.add(user.getUserType());
					break;
				}
			}
			filtered_list.add(desired_info);
		}

		return filtered_list;
	}


}
