package repository;

import model.User; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	 public User findById(Long id);
	 public User findByUsername(String username);
}  
