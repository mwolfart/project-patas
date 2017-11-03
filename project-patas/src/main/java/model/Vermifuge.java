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
	private Double amount;
	private Date appDate;
	private Date nextAppDate;
	private String obs;
	
	public Vermifuge() {
	}
	
	public Vermifuge(Long id, Long dogId, String vermifugeName, double amount,
			Date appDate, Date nextAppDate, String obs) {
		super();
		this.id = id;
		this.dogId = dogId;
		this.vermifugeName = vermifugeName;
		this.amount = amount;
		this.appDate = appDate;
		this.nextAppDate = nextAppDate;
		this.obs = obs;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDogId() {
		return this.dogId;
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
	
	public Double getAmount() {
		return this.amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Date getAppDate() {
		return this.appDate;
	}
	
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	
	public Date getNextAppDate() {
		return this.nextAppDate;
	}
	
	public void setNextAppDate(Date nextAppDate) {
		this.nextAppDate = nextAppDate;
	}
	
	public String getObs() {
		return this.obs;
	}
	
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	@Override
	public String toString() {
		return "Vermifuge [id=" + id + ", dogId=" + dogId 
				+ ", vermifugeName=" + vermifugeName 
				+ ", amount=" + amount + ", appDate=" 
				+ appDate + ", nextAppDate=" + nextAppDate
				+ ", obs=" + obs + ", getId()=" + getId()
				+ ", getDogId()=" + getDogId() 
				+ ", getVermifugeName()=" + getVermifugeName()
				+ ", getAmount()=" + getAmount() + ", getAppDate()="
				+ getAppDate() + ", getNextAppDate()"
				+ getNextAppDate() + ", obs=" + getObs()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
