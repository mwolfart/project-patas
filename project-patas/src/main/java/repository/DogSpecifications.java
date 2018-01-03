package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import model.Dog;
import model.Dog_;

public final class DogSpecifications {
	private DogSpecifications() {}

	//Filter name
	public static Specification<Dog> dogNameEquals(String name) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Dog_.name), name);
		};
	}

	//Filter sex
	public static Specification<Dog> dogSexEquals(String sex) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Dog_.sex), sex);
		};
	}

	//Filter birth month
	public static Specification<Dog> dogBirthMonthEquals(Integer birthMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Dog_.birthDate));
			return cb.equal(month, birthMonth);
		};
	}

	//Filter birth year
	public static Specification<Dog> dogBirthYearEquals(Integer birthYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Dog_.birthDate));
			return cb.equal(year, birthYear);
		};
	}

	//Filter arrival day
	public static Specification<Dog> dogArrivalDayEquals(Integer arrivalDay) {
		return (root, query, cb) -> {
			Expression<Integer> day = cb.function("day", Integer.class, root.get(Dog_.arrivalDate));
			return cb.equal(day, arrivalDay);
		};
	}

	//Filter arrival month
	public static Specification<Dog> dogArrivalMonthEquals(Integer arrivalMonth) {
		return (root, query, cb) -> {
			Expression<Integer> month = cb.function("month", Integer.class, root.get(Dog_.arrivalDate));
			return cb.equal(month, arrivalMonth);
		};
	}

	//Filter arrival year
	public static Specification<Dog> dogArrivalYearEquals(Integer arrivalYear) {
		return (root, query, cb) -> {
			Expression<Integer> year = cb.function("year", Integer.class, root.get(Dog_.arrivalDate));
			return cb.equal(year, arrivalYear);
		};
	}

	//Filter castrated
	public static Specification<Dog> dogCastratedEquals(Boolean castrated) {
		return (root, query, cb) -> {
			return cb.equal(root.get(Dog_.castrated), castrated);
		};
	}

	// buildSpecList
	// given a list of criteria in the format of a hashmap, build a list
	//   of specifications used for the querying.
	public static List<Specification<Dog>> buildSpecListFromCriteria(Map<String, String> criteria_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
		List<Specification<Dog>> spec_list = new ArrayList<Specification<Dog>>();

		for(Map.Entry<String, String> criterion : criteria_list.entrySet())
			spec_list.add(buildSpecFromCriterion(criterion));

		return spec_list;
	}

	// buildSpecFromCriterion
	// given a criterion entry from a hashmap, convert it to a specification
	// OBS.: the conditional values used in this function are directly related to the 
	//   fields "name" attribute in the html. Whenever one is changed, the other also has to.
	public static Specification<Dog> buildSpecFromCriterion(Entry<String, String> criterion) {
		switch(criterion.getKey()) {
		case "name":
			return dogNameEquals(criterion.getValue());
		case "birthMonth":
			return dogBirthMonthEquals(Integer.parseInt(criterion.getValue()));
		case "birthYear":
			return dogBirthYearEquals(Integer.parseInt(criterion.getValue()));
		case "sex":
			return dogSexEquals(criterion.getValue());
		case "arrivalDay":
			return dogArrivalDayEquals(Integer.parseInt(criterion.getValue()));
		case "arrivalMonth":
			return dogArrivalMonthEquals(Integer.parseInt(criterion.getValue()));
		case "arrivalYear":
			return dogArrivalYearEquals(Integer.parseInt(criterion.getValue()));
		case "castrated":
			return dogCastratedEquals(Boolean.parseBoolean(criterion.getValue()));
		case "vacinated":
			/* TODO */
		case "vermifuged":
			/* TODO */
		default:
			return null;
		}
	}

	// buildSpecFromSpecList
	// given a list of specification, return one specification containing all the given specs
	public static Specification<Dog> buildSpecFromSpecList(List<Specification<Dog>> spec_list) {
		// TODO: CHECK IF REFACTORABLE (Only the list types differ)
		if (spec_list.size() < 1)
			return null;

		Specification<Dog> result_spec = spec_list.get(0);
		for (int i=1; i < spec_list.size(); i++)
			result_spec = Specifications.where(result_spec).and(spec_list.get(i));

		return result_spec;
	}

	// filterDogInfo
	// given a list of dog classes, filter the information we want
	//   (given as a list of strings), and return it in a list.
	public static List<List<Object>> filterDogInfo(List<Dog> dog_list, String[] desired_fields) {
		List<List<Object>> filtered_list = new ArrayList<List<Object>>();

		for(Dog dog : dog_list) {
			List<Object> desired_info = new ArrayList<Object>();

			for(String field : desired_fields) {
				switch(field) {
				case "id":
					desired_info.add(dog.getId());
					break;
				case "name":
					desired_info.add(dog.getName());
					break;
				case "arrivalDate":
					desired_info.add(dog.getArrivalDate());
					break;
				case "sex":
					desired_info.add(dog.getSex());
					break;
				}
			}

			filtered_list.add(desired_info);
		}

		return filtered_list;
	}

}
