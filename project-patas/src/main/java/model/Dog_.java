package model;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import model.Dog;
import model.Dog.Availability;

@StaticMetamodel(Dog.class)
public abstract class Dog_ {
	public static volatile SingularAttribute<Dog, Long> id;
	public static volatile SingularAttribute<Dog, String> name;
	public static volatile SingularAttribute<Dog, Double> weight;
	public static volatile SingularAttribute<Dog, String> sex;
	public static volatile SingularAttribute<Dog, String> size;
	public static volatile SingularAttribute<Dog, String> furColor;
	public static volatile SingularAttribute<Dog, Date> birthDate;
	public static volatile SingularAttribute<Dog, Date> arrivalDate;
	public static volatile SingularAttribute<Dog, Boolean> castrated;
	public static volatile SingularAttribute<Dog, Date> castrationDate;
	public static volatile SingularAttribute<Dog, Availability> status;
	public static volatile SingularAttribute<Dog, String> ration;
	public static volatile SingularAttribute<Dog, String> rationCustomDescription;
	public static volatile SingularAttribute<Dog, Double> rationAmount;
	public static volatile SingularAttribute<Dog, Integer> rationMeasurement;
	public static volatile SingularAttribute<Dog, Boolean> hasDiseases;
	public static volatile SingularAttribute<Dog, String> diseaseDescriptions;
	public static volatile SingularAttribute<Dog, String> sponsors;
	//precisa colocar o campo de foto?
}
