package repository;

import java.util.Date;
import java.util.List;
import model.Antifleas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AntifleasRepository extends JpaRepository<Antifleas, Long>, JpaSpecificationExecutor<Antifleas> {
	public List<Antifleas> findByDogId(Long dogId);
	public Antifleas findByAntifleasName(String antifleasName);
	public Antifleas findByApplicationDate(Date applicationDate);
	public Antifleas findByNextApplicationDate(Date nextApplicationDate);  
} 
