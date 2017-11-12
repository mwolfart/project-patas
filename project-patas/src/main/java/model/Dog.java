package model;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dog")
public class Dog {
	
	public enum Availability{
		DISPONIVEL,INDISPONIVEL,ADOTADO,MORREU,LARREPOUSO,DESAPARECIDO;
	}
	
	@Id 
	@GeneratedValue
	private Long id;
	private String name;
	private Double weight;
	private String sex;
	private String size;
	private String furColor;
	private Date birthDate;
	private Integer age;
	private Date arrivalDate;
	private Boolean castrated;
	private Date castrationDate;
	private Availability status;
	private String ration;
	private String rationOther;
	private Double rationPortions;
	private Boolean disease;
	private String diseaseDescription;
	private String sponsors;
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "dog")
	private List<Vermifuge> vermifuges;
	
	

    public Dog() {
    }
    
	public Dog(Long id, String name, Double weight, String sex, String size,
			String furColor, Date birthDate, Integer age, Date arrivalDate,
			Boolean castrated, Date castrationDate, Availability status,
			String ration, String rationOther, Double rationPortions, 
			Boolean disease, String diseaseDescription, String sponsors) {
		super();
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.sex = sex; 
		this.size = size;
		this.furColor = furColor;
		this.birthDate = birthDate;
		this.age = age;
		this.arrivalDate = arrivalDate;
		this.castrated = castrated;
		this.castrationDate = castrationDate;
		this.status = status;
		this.ration = ration;
		this.rationOther = rationOther;
		this.rationPortions = rationPortions;
		this.disease = disease;
		this.diseaseDescription = diseaseDescription;
		this.sponsors = sponsors;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFurColor() {
		return furColor;
	}

	public void setFurColor(String furColor) {
		this.furColor = furColor;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

	public Availability getStatus() {
		return status;
	}

	public void setStatus(Availability status) {
		this.status = status;
	}
	
	public String getRation() {
		return this.ration;
	}
	
	public void setRation(String ration) {
		this.ration = ration;
	}
	
	public String getRationOther() {
		return this.rationOther;
	}
	
	public void setRationOther(String rationOther) {
		this.rationOther = rationOther;
	}
	
	public Double getRationPortions() {
		return this.rationPortions;
	}
	
	public void setRationPortions(Double rationPortions) {
		this.rationPortions = rationPortions;
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

	public String getSponsors() {
		return sponsors;
	}

	public void setSponsors(String sponsors) {
		this.sponsors = sponsors;
	}

	public List<Vermifuge> getVermifuges() {
		return vermifuges;
	}

	public void setVermifuges(List<Vermifuge> vermifuges) {
		this.vermifuges = vermifuges;
	}

	@Override
	public String toString() {
		return "Dog [id=" + id + ", name=" + name + ", weight=" + weight
				+ ", sex=" + sex + ", size=" + size + ", furColor=" + furColor
				+ ", birthDate=" + birthDate + ", age=" + age
				+ ", arrivalDate=" + arrivalDate + ", castrated=" + castrated
				+ ", castrationDate=" + castrationDate + ", status=" + status
				+ ", rationId=" + ration + ", rationOther=" + rationOther
				+ ", rationPortions=" + rationPortions
				+ ", disease=" + disease + ", diseaseDescription="
				+ diseaseDescription + ", sponsors=" + sponsors + ", getId()="
				+ getId() + ", getName()=" + getName() + ", getWeight()="
				+ getWeight() + ", getSex()=" + getSex() + ", getSize()="
				+ getSize() + ", getFurColor()=" + getFurColor()
				+ ", getBirthDate()=" + getBirthDate() + ", getAge()="
				+ getAge() + ", getArrivalDate()=" + getArrivalDate()
				+ ", getCastrated()=" + getCastrated()
				+ ", getCastrationDate()=" + getCastrationDate()
				+ ", getStatus()=" + getStatus() + ", getRation()="
				+ getRation() + ", getRationOther()=" + getRationOther() 
				+ ", getRationPortions()=" + getRationPortions()
				+ ", getDisease()=" + getDisease() + ", getDiseaseDescription()="
				+ getDiseaseDescription() + ", getSponsors()=" + getSponsors()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
