package repository;

import model.Dog; 

import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
	 public Dog findByName(String name);
	 public Dog findById(Long id);
}  
