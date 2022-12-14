package model;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Vermifuge.class)
public abstract class Vermifuge_ {
	public static volatile SingularAttribute<Vermifuge, Long> id;
	public static volatile SingularAttribute<Vermifuge, Long> dogId;
	public static volatile SingularAttribute<Vermifuge, String> vermifugeName;
	public static volatile SingularAttribute<Vermifuge, Double> dosage;
	public static volatile SingularAttribute<Vermifuge, Integer> dosageMeasurement;
	public static volatile SingularAttribute<Vermifuge, Date> applicationDate;
	public static volatile SingularAttribute<Vermifuge, Date> nextApplicationDate;
	public static volatile SingularAttribute<Vermifuge, String> obs;
}
