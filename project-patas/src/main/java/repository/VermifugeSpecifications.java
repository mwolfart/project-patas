package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.criteria.Expression;
import model.Vermifuge;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public final class VermifugeSpecifications {
	private VermifugeSpecifications() {}

	//Filter name
	public static Specification<Vermifuge> vermifugeNameEquals(String name) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Vermifuge_.vermifugeName), name);
		};
	} 

	//VER COMO FAZER
	//	//Filter name of dog
	//	public static Specification<Vermifuge> vermifugeNameEquals(Dog dog) {
	//		return (root, query, cb) -> {
	//			return cb.equal(root.get(Vermifuge_.dog), dog);
	//		};
	//	} 

	//Filter application day
	public static Specification<Vermifuge> vermifugeApplicationDayEquals(Integer applicationDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vermifuge_.applicationDate));
			return cb.equal(day, applicationDay);
		};
	}

	//Filter application month
	public static Specification<Vermifuge> vermifugeApplicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vermifuge_.applicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter application year
	public static Specification<Vermifuge> vermifugeApplicationYearEquals(Integer applicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vermifuge_.applicationDate));
			return cb.equal(year, applicationYear);
		};
	}

	//Filter next application day
	public static Specification<Vermifuge> vermifugeNextApplicationDayEquals(Integer nextApplicationDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vermifuge_.nextapplicationDate));
			return cb.equal(day, nextApplicationDay);
		};
	}

	//Filter next application month
	public static Specification<Vermifuge> vermifugeNextApplicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vermifuge_.nextapplicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter next application year
	public static Specification<Vermifuge> vermifugeNextApplicationYearEquals(Integer nextApplicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vermifuge_.nextapplicationDate));
			return cb.equal(year, nextApplicationYear);
		};
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

	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	public static List<Specification<Vermifuge>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		List<Specification<Vermifuge>> spec_list = new ArrayList<Specification<Vermifuge>>();

		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));

		return spec_list;
	}

	// buildSpecFromCriterion
	// given a griterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<Vermifuge> buildSpecFromCriterion(Entry<String, String> criterion) {
		switch(criterion.getKey()) {
		case "name":
			return vermifugeNameEquals(criterion.getValue());
		case "dogName":
			//TODO
		case "applicationDay":
			return vermifugeApplicationDayEquals(Integer.parseInt(criterion.getValue()));
		case "applicationMonth":
			return vermifugeApplicationMonthEquals(Integer.parseInt(criterion.getValue()));
		case "applicationYear":
			return vermifugeApplicationYearEquals(Integer.parseInt(criterion.getValue()));
		case "nextApplicationDay":
			return vermifugeNextApplicationDayEquals(Integer.parseInt(criterion.getValue()));
		case "nextApplicationMonth":
			return vermifugeNextApplicationMonthEquals(Integer.parseInt(criterion.getValue()));
		case "nextApplicationYear":
			return vermifugeNextApplicationYearEquals(Integer.parseInt(criterion.getValue()));
		default:
			return null;
		}
	}

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	public static Specification<Vermifuge> buildSpecFromSpecList(List<Specification<Vermifuge>> spec_list) {
		if (spec_list.size() < 1)
			return null;

		Specification<Vermifuge> result_spec = spec_list.get(0);
		for (int i=1; i < spec_list.size(); i++)
			result_spec = Specifications.where(result_spec).and(spec_list.get(i));

		return result_spec;
	}

	// filterVermifugeInfo
	// given a list of vermifuge classes, filter the information we want
	//   (given as a list of strings), and return it in a list.
	public static List<List<Object>> filterVermifugeInfo(List<Vermifuge> vermifuge_list, String[] desired_fields) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();

		for(Vermifuge vermifuge : vermifuge_list) {
			List<Object> desired_info = new ArrayList<Object>();

			for(String field : desired_fields) {
				switch(field) {
				case "id":
					desired_info.add(vermifuge.getId());
					break;
				case "name":
					desired_info.add(vermifuge.getVermifugeName());
					break;
				case "dogName":
					//TODO
					//desired_info.add(vermifuge.getVermifugeName());
					break;
				case "applicationDate":
					desired_info.add(vermifuge.getApplicationDate());
					break;
				case "NextApplicationDate":
					desired_info.add(vermifuge.getNextapplicationDate());
					break;
				}
			}
			filtered_list.add(desired_info);
		}

		return filtered_list;
	}


}
