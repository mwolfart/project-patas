package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "antifleas")
public class Antifleas {
	@Id
	@GeneratedValue
	private Long id;
	private Long dogId;
	private String antifleasName;
	private Date applicationDate;
	private Date nextApplicationDate;
	private String obs;
	
	
	public Antifleas(Long id, Long dogId, String antifleasName,
			Date applicationDate, Date nextApplicationDate, String obs) {
		super();
		this.id = id;
		this.dogId = dogId;
		this.antifleasName = antifleasName;
		this.applicationDate = applicationDate;
		this.nextApplicationDate = nextApplicationDate;
		this.obs = obs;
	}
	public Long getId() {
		return id;
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
	public String getAntifleasName() {
		return antifleasName;
	}
	public void setAntifleasName(String antifleasName) {
		this.antifleasName = antifleasName;
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
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	@Override
	public String toString() {
		return "Antifleas [id=" + id + ", dogId=" + dogId + ", antifleasName="
				+ antifleasName + ", applicationDate=" + applicationDate
				+ ", nextApplicationDate=" + nextApplicationDate + ", obs="
				+ obs + "]";
	}
}
