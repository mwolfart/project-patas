package repository;

import javax.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;

import model.Dog;

final public class DogSpecifications {
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
	
}
