package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.User;
import model.User_;

import org.springframework.data.jpa.domain.Specification;

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
}
