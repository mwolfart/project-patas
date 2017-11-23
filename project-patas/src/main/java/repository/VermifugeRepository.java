package repository;


import java.util.Date;

import model.Dog;
import model.Vermifuge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VermifugeRepository extends JpaRepository<Vermifuge, Long>,JpaSpecificationExecutor<Vermifuge> {
	public Vermifuge findByVermifugeName(String vermifugeName);
	public Vermifuge findByDog(Dog dog); //provavelmente tq pegar o nome dentro, passando o cao
	public Vermifuge findByApplicationDate(Date ApplicationDate);
	public Vermifuge findBynextapplicationDate(Date nextApplicationDate);
	public Vermifuge findByVermifugeNameAndDosage(String name,Double dosage);

	
}