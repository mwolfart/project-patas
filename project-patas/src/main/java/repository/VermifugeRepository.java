package repository;

import java.util.Date;

import model.Vermifuge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VermifugeRepository extends JpaRepository<Vermifuge, Long>, JpaSpecificationExecutor<Vermifuge> {
	public Vermifuge findByVermifugeName(String vermifugeName);
	public Vermifuge findByApplicationDate(Date applicationDate);
	public Vermifuge findByNextApplicationDate(Date nextApplicationDate);
	public Vermifuge findByVermifugeNameAndDosage(String name, Double dosage);
}