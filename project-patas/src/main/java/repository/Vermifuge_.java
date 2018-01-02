package repository;


import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import model.Vermifuge;

@StaticMetamodel(Vermifuge.class)
public abstract class Vermifuge_ {
	public static volatile SingularAttribute<Vermifuge, Long> id;
	public static volatile SingularAttribute<Vermifuge, Integer> dogId;
	public static volatile SingularAttribute<Vermifuge, String> vermifugeName;
	public static volatile SingularAttribute<Vermifuge, Double> dosage;
	public static volatile SingularAttribute<Vermifuge, Date> applicationDate;
	public static volatile SingularAttribute<Vermifuge, Date> nextapplicationDate;
	public static volatile SingularAttribute<Vermifuge, String> obs;
}
