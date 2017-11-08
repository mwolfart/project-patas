package repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface Specification<Dog> {
	Predicate toPredicate(Root<Dog> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
