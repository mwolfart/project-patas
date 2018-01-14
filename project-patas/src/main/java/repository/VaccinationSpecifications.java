package repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.Expression;

import model.Vaccination;
import model.Vaccination_;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public final class VaccinationSpecifications {
	private VaccinationSpecifications() {}

	//Filter name
	public static Specification<Vaccination> vaccineNameEquals(String name) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Vaccination_.vaccineName), name);
		};
	} 

	//Filter dog id
	public static Specification<Vaccination> dogIdEquals(Long dogId) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Vaccination_.dogId), dogId);
		};
	} 
	
	//Filter application day
	public static Specification<Vaccination> applicationDayEquals(Integer applicationDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vaccination_.applicationDate));
			return cb.equal(day, applicationDay);
		};
	}

	//Filter application month
	public static Specification<Vaccination> applicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vaccination_.applicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter application year
	public static Specification<Vaccination> applicationYearEquals(Integer applicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vaccination_.applicationDate));
			return cb.equal(year, applicationYear);
		};
	}

	//Filter next application day
	public static Specification<Vaccination> nextApplicationDayEquals(Integer nextApplicationDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Vaccination_.nextApplicationDate));
			return cb.equal(day, nextApplicationDay);
		};
	}

	//Filter next application month
	public static Specification<Vaccination> nextApplicationMonthEquals(Integer applicationMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Vaccination_.nextApplicationDate));
			return cb.equal(month, applicationMonth);
		};
	}

	//Filter next application year
	public static Specification<Vaccination> nextApplicationYearEquals(Integer nextApplicationYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Vaccination_.nextApplicationDate));
			return cb.equal(year, nextApplicationYear);
		};
	}

	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	public static List<Specification<Vaccination>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		List<Specification<Vaccination>> spec_list = new ArrayList<Specification<Vaccination>>();

		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));

		return spec_list;
	}

	// buildSpecFromCriterion
	// given a criterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<Vaccination> buildSpecFromCriterion(Entry<String, String> criterion) {
		Calendar cal = Calendar.getInstance();
		
		switch(criterion.getKey()) {
		case "vaccineName":
			return vaccineNameEquals(criterion.getValue());
		case "dogId":
			return dogIdEquals(Long.parseLong(criterion.getValue()));
		case "applicationDate":
			Date appDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(appDate);
			
			Specification<Vaccination> specAppDay = applicationDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Vaccination> specAppMonth = applicationMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Vaccination> specAppYear = applicationYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specAppDay).and(specAppMonth).and(specAppYear);
		case "nextApplicationDate":
			Date nextAppDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(nextAppDate);
			
			Specification<Vaccination> specNextAppDay = nextApplicationDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Vaccination> specNextAppMonth = nextApplicationMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Vaccination> specNextAppYear = nextApplicationYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specNextAppDay).and(specNextAppMonth).and(specNextAppYear);
		default:
			return null;
		}
	}
}
