package model;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Appointment.class)
public abstract class Appointment_ {
	public static volatile SingularAttribute<Appointment, Long> id;
	public static volatile SingularAttribute<Appointment, Long> dogId;
	public static volatile SingularAttribute<Appointment, String> responsibleName;
	public static volatile SingularAttribute<Appointment, Date> appointmentDate;
	public static volatile SingularAttribute<Appointment, String> location;
	public static volatile SingularAttribute<Appointment, String> vetName;
	public static volatile SingularAttribute<Appointment, Float> totalCost;
	public static volatile SingularAttribute<Appointment, String> reason;
	public static volatile SingularAttribute<Appointment, Boolean> examinated;
	public static volatile SingularAttribute<Appointment, String> examDescription;
	public static volatile SingularAttribute<Appointment, String> appointmentDescription;
}
