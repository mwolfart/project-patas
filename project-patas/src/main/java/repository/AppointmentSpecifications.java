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
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
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

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	public static Specification<Appointment> buildSpecFromSpecList(List<Specification<Appointment>> spec_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
		if (spec_list.size() < 1)
			return null;

		Specification<Appointment> result_spec = spec_list.get(0);
		for (int i=1; i < spec_list.size(); i++)
			result_spec = Specifications.where(result_spec).and(spec_list.get(i));

		return result_spec;
	}

	// filterAppointmentInfo
	// given a list of appointment classes, filter the information we want
	//   (given as a list of strings), and return it in a list.
	public static List<List<Object>> filterAppointmentInfo(List<Appointment> appointment_list, String[] desired_fields) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();

		for(Appointment appointment : appointment_list) {
			List<Object> desired_info = new ArrayList<Object>();

			for(String field : desired_fields) {
				switch(field) {
				case "id":
					desired_info.add(appointment.getId());
					break;
				case "location":
					desired_info.add(appointment.getLocation());
					break;
				case "dogId":
					desired_info.add(appointment.getDogId());
					break;
				case "appointmentDate":
					desired_info.add(appointment.getAppointmentDate());
					break;
				case "vetName":
					desired_info.add(appointment.getVetName());
					break;
				}
			}
			filtered_list.add(desired_info);
		}

		return filtered_list;
	}


}
