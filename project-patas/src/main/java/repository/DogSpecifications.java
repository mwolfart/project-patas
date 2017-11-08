package repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.Dog;
import model.Dog_;

public class DogSpecifications {		
	// Filter name
	public static Specification<Dog> dogNameEquals(String name) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Dog_.name), name);
			}
		};
	}
	
	//Filter sex
	public static Specification<Dog> dogSexEquals(String sex) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Dog_.sex), sex);
			}
		};
	}
	
	//Filter birth month
	public static Specification<Dog> dogBirthMonthEquals(Integer birthMonth) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Expression<Integer> month = cb.function("month", Integer.class, root.get(Dog_.birthDate));
				return cb.equal(month, birthMonth);
			}
		};
	}
	
	//Filter birth year
	public static Specification<Dog> dogBirthYearEquals(Integer birthYear) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Expression<Integer> year = cb.function("year", Integer.class, root.get(Dog_.birthDate));
				return cb.equal(year, birthYear);
			}
		};
	}
	
	//Filter arrival day
	public static Specification<Dog> dogArrivalDayEquals(Integer arrivalDay) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Expression<Integer> day = cb.function("day", Integer.class, root.get(Dog_.arrivalDate));
				return cb.equal(day, arrivalDay);
			}
		};
	}
	
	//Filter arrival month
	public static Specification<Dog> dogArrivalMonthEquals(Integer arrivalMonth) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Expression<Integer> month = cb.function("month", Integer.class, root.get(Dog_.arrivalDate));
				return cb.equal(month, arrivalMonth);
			}
		};
	}
	
	//Filter arrival year
	public static Specification<Dog> dogArrivalYearEquals(Integer arrivalYear) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Expression<Integer> year = cb.function("year", Integer.class, root.get(Dog_.arrivalDate));
				return cb.equal(year, arrivalYear);
			}
		};
	}
	
	//Filter sex
	public static Specification<Dog> dogCastratedEquals(Boolean castrated) {
		return new Specification<Dog>() {
			public Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Dog_.castrated), castrated);
			}
		};
	}
}
