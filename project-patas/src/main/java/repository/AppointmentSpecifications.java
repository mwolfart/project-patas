package repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.Expression;

import model.Appointment;
import model.Appointment_;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public final class AppointmentSpecifications {
	private AppointmentSpecifications() {}

	//Filter dog id
	public static Specification<Appointment> dogIdEquals(Long dogId) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Appointment_.dogId), dogId);
		};
	} 
	
	//Filter appointment day
	public static Specification<Appointment> appointmentDayEquals(Integer appointmentDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Appointment_.appointmentDate));
			return cb.equal(day, appointmentDay);
		};
	}

	//Filter appointment month
	public static Specification<Appointment> appointmentMonthEquals(Integer appointmentMonth) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("month", Integer.class, root.get(Appointment_.appointmentDate));
			return cb.equal(day, appointmentMonth);
		};
	}
	
	//Filter appointment year
	public static Specification<Appointment> appointmentYearEquals(Integer appointmentYear) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("year", Integer.class, root.get(Appointment_.appointmentDate));
			return cb.equal(day, appointmentYear);
		};
	}

	//Filter location
	public static Specification<Appointment> locationEquals(String location) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Appointment_.location), location);
		};
	} 
	
	//Filter veterinarian name
	public static Specification<Appointment> vetNameEquals(String vetName) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Appointment_.vetName), vetName);
		};
	} 
		
	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	public static List<Specification<Appointment>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		List<Specification<Appointment>> spec_list = new ArrayList<Specification<Appointment>>();

		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));

		return spec_list;
	}

	// buildSpecFromCriterion
	// given a criterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<Appointment> buildSpecFromCriterion(Entry<String, String> criterion) {
		Calendar cal = Calendar.getInstance();
		
		switch(criterion.getKey()) {
		case "dogId":
			return dogIdEquals(Long.parseLong(criterion.getValue()));
		case "appointmentDate":
			Date appointmentDate = new Date(Long.parseLong(criterion.getValue()));
			cal.setTime(appointmentDate);
			
			Specification<Appointment> specDay = appointmentDayEquals(cal.get(Calendar.DAY_OF_MONTH));
			Specification<Appointment> specMonth = appointmentMonthEquals(cal.get(Calendar.MONTH) + 1);
			Specification<Appointment> specYear = appointmentYearEquals(cal.get(Calendar.YEAR));
			
			return Specifications.where(specDay).and(specMonth).and(specYear);
		case "location":
			return locationEquals(criterion.getValue());
		case "vetName":
			return vetNameEquals(criterion.getValue());
		default:
			return null;
		}
	}
}
