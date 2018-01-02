package model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public abstract class User_ {
	public static volatile SingularAttribute<Appointment, Long> id;
	public static volatile SingularAttribute<Appointment, String> username;
	public static volatile SingularAttribute<Appointment, byte[]> passwordHash;
	public static volatile SingularAttribute<Appointment, Integer> userType;
	public static volatile SingularAttribute<Appointment, String> fullName;
}
