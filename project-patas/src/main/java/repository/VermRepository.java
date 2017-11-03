package repository;

import model.Vermifuge;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VermRepository extends JpaRepository<Vermifuge, Long> {
	public Vermifuge findByVermifugeName(String vermifugeName);
	public Vermifuge findById(Long id);
	public Vermifuge findByDogId(Long dogId);
}