package model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	private Date arrivalDate;
	private Boolean castrated;
	private Date castrationDate;
	private Availability status;
	private String ration;
	private String rationCustomDescription;
	private Double rationAmount;
	private Integer rationMeasurement;
	private Boolean hasDiseases;
	private String diseaseDescription;
	private String sponsors;	
	public byte[] photo;

    public Dog() {
    }
    
	public Dog(Long id, String name, Double weight, String sex, String size,
			String furColor, Date birthDate, Date arrivalDate,
			Boolean castrated, Date castrationDate, Availability status,
			String ration, String rationCustomDescription, Double rationAmount, Integer rationMeasurement,
			Boolean hasDiseases, String diseaseDescription, String sponsors) {
		super();
		this.id = id;
		this.name = name;
		this.weight = weight;
		this.sex = sex; 
		this.size = size;
		this.furColor = furColor;
		this.birthDate = birthDate;
		this.arrivalDate = arrivalDate;
		this.castrated = castrated;
		this.castrationDate = castrationDate;
		this.status = status;
		this.ration = ration;
		this.rationCustomDescription = rationCustomDescription;
		this.rationAmount = rationAmount;
		this.rationMeasurement = rationMeasurement;
		this.hasDiseases = hasDiseases;
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
	
	public String getRationCustomDescription() {
		return this.rationCustomDescription;
	}
	
	public void setRationCustomDescription(String rationCustomDescription) {
		this.rationCustomDescription = rationCustomDescription;
	}
	
	public Double getRationAmount() {
		return this.rationAmount;
	}
	
	public void setRationAmount(Double rationAmount) {
		this.rationAmount = rationAmount;
	}

	public Integer getRationMeasurement() {
		return rationMeasurement;
	}

	public void setRationMeasurement(Integer rationMeasurement) {
		this.rationMeasurement = rationMeasurement;
	}

	public Boolean getHasDiseases() {
		return hasDiseases;
	}

	public void setHasDiseases(Boolean hasDiseases) {
		this.hasDiseases = hasDiseases;
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
	
	@Lob
	public byte[] getPhoto() {
		return photo;
	}
	
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Dog [id=" + id + ", name=" + name + ", weight=" + weight
				+ ", sex=" + sex + ", size=" + size + ", furColor=" + furColor
				+ ", birthDate=" + birthDate
				+ ", arrivalDate=" + arrivalDate + ", castrated=" + castrated
				+ ", castrationDate=" + castrationDate + ", status=" + status
				+ ", rationId=" + ration + ", rationCustomDescription=" + rationCustomDescription
				+ ", rationAmount=" + rationAmount + ", rationMeasurement=" + rationMeasurement
				+ ", hasDiseases=" + hasDiseases + ", diseaseDescription="
				+ diseaseDescription + ", sponsors=" + sponsors + "]";
	}

}
