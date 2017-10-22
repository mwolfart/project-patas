package controller;

import model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repository.DogRepository;


@RestController
public class DogController { 
	
	@Autowired 
	private DogRepository dogRepository;
		
	@RequestMapping(value = "/dog/register", method = RequestMethod.POST)
    public ResponseEntity<String> dogRegister(@RequestBody Dog dog) {
		
		if(dog.getName() == null){
			return new ResponseEntity<String>("Nome está em branco",HttpStatus.BAD_REQUEST);
		}
		
		Dog dogExist = dogRepository.findByName(dog.getName());
		if(dogExist != null){
			return new ResponseEntity<String>("Já existe um cachorro com este nome",HttpStatus.BAD_REQUEST);
		}
		
		if(dog.getArrivalDate() == null){
			return new ResponseEntity<String>("Data de chegada está em branco",HttpStatus.BAD_REQUEST);
		}
		
		if(dog.getCastrated() == null){
			return new ResponseEntity<String>("Flag de castrado está em branco",HttpStatus.BAD_REQUEST);
		}
		
		dogRepository.saveAndFlush(dog);  
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	//Must rework this
	@RequestMapping(value = "/dog/search", method = RequestMethod.GET)
	 public ResponseEntity<String> dogSearch(@RequestBody Dog dogCriteria) {
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
