package repository;

import java.util.Date;

import model.Vaccination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long>, JpaSpecificationExecutor<Vaccination> {
	public Vaccination findByVaccineName(String vaccineName);
	public Vaccination findByApplicationDate(Date applicationDate);
	public Vaccination findByNextApplicationDate(Date nextApplicationDate);
}