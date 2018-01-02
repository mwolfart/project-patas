package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {

	@Id
	@GeneratedValue
	private Long id;
	private Long dogId;
	private String responsibleName;
	private Date appointmentDate;
	private String location;
	private String vetName;
	private Float price;
	private String reason;
	private Boolean exam;
	private String examDescription;
	private String appointmentDescription;
	/*
	 * adicionar anexos
	 */
	
	public Appointment() {
	}
	
	public Appointment(Long id, Long dogId, String responsibleName,
			Date appointmentDate, String location, String vetName,
			Float price, String reason, Boolean exam,
			String examDescription, String appointmentDescription) {
		super();
		this.id = id;
		this.dogId = dogId;
		this.responsibleName = responsibleName;
		this.appointmentDate = appointmentDate;
		this.location = location;
		this.vetName = vetName;
		this.price = price;
		this.reason = reason;
		this.exam = exam;
		this.examDescription = examDescription;
		this.appointmentDescription = appointmentDescription;
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
	
	public String getResponsibleName() {
		return responsibleName;
	}
	
	public void setResponsibleName(String responsibleName) {
		this.responsibleName = responsibleName;
	}
	
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getVetName() {
		return vetName;
	}
	
	public void setVetName(String vetName) {
		this.vetName = vetName;
	}
	
	public Float getPrice() {
		return price;
	}
	
	public void setPrice(Float price) {
		this.price = price;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Boolean getExam() {
		return exam;
	}
	
	public void setExam(Boolean exam) {
		this.exam = exam;
	}
	
	public String getExamDescription() {
		return examDescription;
	}
	
	public void setExamDescription(String examDescription) {
		this.examDescription = examDescription;
	}
	
	public String getAppointmentDescription() {
		return appointmentDescription;
	}
	
	public void setAppointmentDescription(String appointmentDescription) {
		this.appointmentDescription = appointmentDescription;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", dogId=" + dogId
				+ ", responsibleName=" + responsibleName + ", appointmentDate="
				+ appointmentDate + ", location=" + location + ", vetName="
				+ vetName + ", price=" + price + ", reason=" + reason
				+ ", exam=" + exam + ", examDescription=" + examDescription
				+ ", appointmentDescription=" + appointmentDescription + "]";
	}
}
