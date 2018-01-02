package model;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Vaccination.class)
public abstract class Vaccination_ {
	public static volatile SingularAttribute<Vaccination, Long> id;
	public static volatile SingularAttribute<Vaccination, Long> dogId;
	public static volatile SingularAttribute<Vaccination, String> vaccineName;
	public static volatile SingularAttribute<Vaccination, Date> applicationDate;
	public static volatile SingularAttribute<Vaccination, Date> nextApplicationDate;
	public static volatile SingularAttribute<Vaccination, String> obs;
}
