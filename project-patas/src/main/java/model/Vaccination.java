package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vaccination")
public class Vaccination {

	@Id
	@GeneratedValue
	private Long id;
	private Long dogId;
	private String vaccineName;
	private Date applicationDate;
	private Date nextApplicationDate;
	private String obs;

	public Vaccination() {
	}

	public Vaccination(Long id, Long dogId, String vaccineName,
			Date applicationDate, Date nextApplicationDate, String obs) {
		super();
		this.id = id;
		this.dogId = dogId;
		this.vaccineName = vaccineName;
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

	public String getVaccineName() {
		return this.vaccineName;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
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
		return "Vaccination [id=" + id + ", dogId=" + dogId + ", vaccineName="
				+ vaccineName + ", applicationDate="
				+ applicationDate + ", nextApplicationDate="
				+ nextApplicationDate + ", obs=" + obs + "]";
	}

}
