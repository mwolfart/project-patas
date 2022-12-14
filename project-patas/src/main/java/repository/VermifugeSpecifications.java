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
	public static Specification<Vermifuge> dogIdEquals(Long dogId) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Vermifuge_.dogId), dogId);
		};
	} 
	
	//Filter application day
	public static Specification<Vermifuge> applicationDayEquals(Integer applicationDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vermifuge_.applicationDate));
			return cb.equal(day, applicationDay);
		};
	}

	//Filter application month
	public static Specification<Vermifuge> applicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vermifuge_.applicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter application year
	public static Specification<Vermifuge> applicationYearEquals(Integer applicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vermifuge_.applicationDate));
			return cb.equal(year, applicationYear);
		};
	}

	//Filter next application day
	public static Specification<Vermifuge> nextApplicationDayEquals(Integer nextApplicationDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vermifuge_.nextApplicationDate));
			return cb.equal(day, nextApplicationDay);
		};
	}

	//Filter next application month
	public static Specification<Vermifuge> nextApplicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vermifuge_.nextApplicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter next application year
	public static Specification<Vermifuge> nextApplicationYearEquals(Integer nextApplicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vermifuge_.nextApplicationDate));
			return cb.equal(year, nextApplicationYear);
		};
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
	// given a criterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<Vermifuge> buildSpecFromCriterion(Entry<String, String> criterion) {
		Calendar cal = Calendar.getInstance();
		
		switch(criterion.getKey()) {
		case "vermifugeName":
			return vermifugeNameEquals(criterion.getValue());
		case "dogId":
			return dogIdEquals(Long.parseLong(criterion.getValue()));
		case "applicationDate":
			Date appDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(appDate);
			
			Specification<Vermifuge> specAppDay = applicationDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Vermifuge> specAppMonth = applicationMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Vermifuge> specAppYear = applicationYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specAppDay).and(specAppMonth).and(specAppYear);
		case "nextApplicationDate":
			Date nextAppDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(nextAppDate);
			
			Specification<Vermifuge> specNextAppDay = nextApplicationDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Vermifuge> specNextAppMonth = nextApplicationMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Vermifuge> specNextAppYear = nextApplicationYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specNextAppDay).and(specNextAppMonth).and(specNextAppYear);
		default:
			return null;
		}
	}
}
