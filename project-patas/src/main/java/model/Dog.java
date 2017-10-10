package model;
import java.util.Date;


public class Dog {
	
	private String name;
	private Double weight;
	private String gender;
	private String size;
	private String pelageColor;
	private Date dateBirth;
	private Integer age;
	private Date arrivalDate;
	private Boolean castrated;
	private Date castrationDate;
	private Availability availability;
	private Boolean disease;
	private String diseaseDescription;
	private String godfathers;
	
	
	//Introducing the dummy constructor
    public Dog() {
    }
    
	public enum Availability{
		DISPONIVEL,INDISPONIVEL,ADOTADO,MORREU,LARREPOUSO,DESAPARECIDO;
	}

	public Dog(String name, Double weight, String gender, String size,
			String pelageColor, Date dateBirth, Integer age, Date arrivalDate,
			Boolean castrated, Date castrationDate, Availability availability,
			Boolean disease, String diseaseDescription, String godfathers) {
		super();
		this.name = name;
		this.weight = weight;
		this.gender = gender;
		this.size = size;
		this.pelageColor = pelageColor;
		this.dateBirth = dateBirth;
		this.age = age;
		this.arrivalDate = arrivalDate;
		this.castrated = castrated;
		this.castrationDate = castrationDate;
		this.availability = availability;
		this.disease = disease;
		this.diseaseDescription = diseaseDescription;
		this.godfathers = godfathers;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getPelageColor() {
		return pelageColor;
	}
	public void setPelageColor(String pelageColor) {
		this.pelageColor = pelageColor;
	}
	public Date getDateBirth() {
		return dateBirth;
	}
	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public Boolean getCastrated() {
		return castrated;
	}
	public void setCastrated(Boolean castrated) {
		this.castrated = castrated;
	}
	public Date getCastrationDate() {
		return castrationDate;
	}
	public void setCastrationDate(Date castrationDate) {
		this.castrationDate = castrationDate;
	}
	public Availability getAvailability() {
		return availability;
	}
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
	public Boolean getDisease() {
		return disease;
	}
	public void setDisease(Boolean disease) {
		this.disease = disease;
	}
	public String getDiseaseDescription() {
		return diseaseDescription;
	}
	public void setDiseaseDescription(String diseaseDescription) {
		this.diseaseDescription = diseaseDescription;
	}
	public String getGodfathers() {
		return godfathers;
	}
	public void setGodfathers(String godfathers) {
		this.godfathers = godfathers;
	}

	@Override
	public String toString() {
		return "Dog [name=" + name + ", weight=" + weight + ", gender="
				+ gender + ", size=" + size + ", pelageColor=" + pelageColor
				+ ", dateBirth=" + dateBirth + ", age=" + age
				+ ", arrivalDate=" + arrivalDate + ", castrated=" + castrated
				+ ", castrationDate=" + castrationDate + ", availability="
				+ availability + ", disease=" + disease
				+ ", diseaseDescription=" + diseaseDescription
				+ ", godfathers=" + godfathers + "]";
	}
	
	
}
