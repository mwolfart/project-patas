package model;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Antifleas.class)
public abstract class Antifleas_ {
	public static volatile SingularAttribute<Antifleas, Long> id;
	public static volatile SingularAttribute<Antifleas, Long> dogId;
	public static volatile SingularAttribute<Antifleas, String> antifleasName;
	public static volatile SingularAttribute<Antifleas, Date> applicationDate;
	public static volatile SingularAttribute<Antifleas, Date> nextApplicationDate;
	public static volatile SingularAttribute<Antifleas, String> obs;

}
