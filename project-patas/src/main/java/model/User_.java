package model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public abstract class User_ {
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, byte[]> passwordHash;
	public static volatile SingularAttribute<User, Integer> userType;
	public static volatile SingularAttribute<User, String> fullName;
}
