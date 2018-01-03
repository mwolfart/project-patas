package repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.Expression;

import model.Vermifuge;
import model.Vermifuge_;

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

	//Filter dog id
	public static Specification<Vermifuge> vermifugeDogIdEquals(Long dogId) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Vermifuge_.dogId), dogId);
		};
	} 
	
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
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vermifuge_.nextApplicationDate));
			return cb.equal(day, nextApplicationDay);
		};
	}

	//Filter next application month
	public static Specification<Vermifuge> vermifugeNextApplicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vermifuge_.nextApplicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter next application year
	public static Specification<Vermifuge> vermifugeNextApplicationYearEquals(Integer nextApplicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vermifuge_.nextApplicationDate));
			return cb.equal(year, nextApplicationYear);
		};
	}

	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	public static List<Specification<Vermifuge>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
		List<Specification<Vermifuge>> spec_list = new ArrayList<Specification<Vermifuge>>();

		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));

		return spec_list;
	}

	// buildSpecFromCriterion
	// given a criterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<Vermifuge> buildSpecFromCriterion(Entry<String, String> criterion) {
		Calendar cal = Calendar.getInstance();
		
		switch(criterion.getKey()) {
		case "vermifugeName":
			return vermifugeNameEquals(criterion.getValue());
		case "dogId":
			return vermifugeDogIdEquals(Long.parseLong(criterion.getValue()));
		case "applicationDate":
			Date appDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(appDate);
			
			Specification<Vermifuge> specAppDay = vermifugeApplicationDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Vermifuge> specAppMonth = vermifugeApplicationMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Vermifuge> specAppYear = vermifugeApplicationYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specAppDay).and(specAppMonth).and(specAppYear);
		case "nextApplicationDate":
			Date nextAppDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(nextAppDate);
			
			Specification<Vermifuge> specNextAppDay = vermifugeApplicationDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Vermifuge> specNextAppMonth = vermifugeApplicationMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Vermifuge> specNextAppYear = vermifugeApplicationYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specNextAppDay).and(specNextAppMonth).and(specNextAppYear);
		default:
			return null;
		}
	}

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	public static Specification<Vermifuge> buildSpecFromSpecList(List<Specification<Vermifuge>> spec_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
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
				case "dogId":
					desired_info.add(vermifuge.getDogId());
					break;
				case "applicationDate":
					desired_info.add(vermifuge.getApplicationDate());
					break;
				case "nextApplicationDate":
					desired_info.add(vermifuge.getNextApplicationDate());
					break;
				}
			}
			filtered_list.add(desired_info);
		}

		return filtered_list;
	}


}
