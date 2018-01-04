package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vermifuge")
public class Vermifuge {

	@Id
	@GeneratedValue
	private Long id;
	private Long dogId;
	private String vermifugeName;
	private Double dosage;
	private Integer dosageMeasurement;
	private Date applicationDate;
	private Date nextApplicationDate;
	private String obs;

	public Vermifuge() {
	}

	public Vermifuge(Long id, Long dogId, String vermifugeName, Double dosage, Integer dosageMeasurement,
			Date applicationDate, Date nextApplicationDate, String obs) {
		super();
		this.id = id;
		this.dogId = dogId;
		this.vermifugeName = vermifugeName;
		this.dosage = dosage;
		this.dosageMeasurement = dosageMeasurement;
		this.applicationDate = applicationDate;
		this.nextApplicationDate = nextApplicationDate;
		this.obs = obs;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDogId() {
		return dogId;
	}

	public void setDogId(Long dogId) {
		this.dogId = dogId;
	}

	public String getVermifugeName() {
		return this.vermifugeName;
	}

	public void setVermifugeName(String vermifugeName) {
		this.vermifugeName = vermifugeName;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}
	
	public Integer getDosageMeasurement() {
		return dosageMeasurement;
	}

	public void setDosageMeasurement(Integer dosageMeasurement) {
		this.dosageMeasurement = dosageMeasurement;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getNextApplicationDate() {
		return nextApplicationDate;
	}

	public void setNextApplicationDate(Date nextApplicationDate) {
		this.nextApplicationDate = nextApplicationDate;
	}

	public String getObs() {
		return this.obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public String toString() {
		return "Vermifuge [id=" + id + ", dogId=" + dogId + ", vermifugeName="
				+ vermifugeName + ", dosage=" + dosage + ", dosageMeasurement="
				+ dosageMeasurement + ", applicationDate=" + applicationDate
				+ ", nextApplicationDate=" + nextApplicationDate + ", obs=" 
				+ obs + "]";
	}

}
