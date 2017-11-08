package repository;

import java.util.List;

import model.Dog; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DogRepository extends JpaRepository<Dog, Long>, JpaSpecificationExecutor<Dog> {
	 public Dog findByName(String name);
	 public Dog findById(Long id);
	 public List<Dog> findAll();
}  
