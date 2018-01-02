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
	private Integer dogId;
	private String vermifugeName;
	private Double dosage;
	private Date applicationDate;
	private Date nextapplicationDate;
	private String obs;

	public Vermifuge() {
	}

	public Vermifuge(Long id, Integer dogId, String vermifugeName, Double dosage,
			Date applicationDate, Date nextapplicationDate, String obs) {
		super();
		this.id = id;
		this.dogId = dogId;
		this.vermifugeName = vermifugeName;
		this.dosage = dosage;
		this.applicationDate = applicationDate;
		this.nextapplicationDate = nextapplicationDate;
		this.obs = obs;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDogId() {
		return dogId;
	}

	public void setDogId(Integer dogId) {
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

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getNextapplicationDate() {
		return nextapplicationDate;
	}

	public void setNextapplicationDate(Date nextapplicationDate) {
		this.nextapplicationDate = nextapplicationDate;
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
				+ vermifugeName + ", dosage=" + dosage + ", applicationDate="
				+ applicationDate + ", nextapplicationDate="
				+ nextapplicationDate + ", obs=" + obs + "]";
	}

}
